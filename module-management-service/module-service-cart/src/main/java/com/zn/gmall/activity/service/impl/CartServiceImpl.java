package com.zn.gmall.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zn.gmall.activity.mapper.CartInfoMapper;
import com.zn.gmall.activity.service.api.CartAsyncService;
import com.zn.gmall.activity.service.api.CartService;
import com.zn.gmall.common.constant.RedisConst;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.cart.CartInfo;
import com.zn.gmall.model.product.SkuInfo;
import com.zn.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 购物车实现
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartInfoMapper cartInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private CartAsyncService cartAsyncService;

    /**
     * 拼接购物车在 Redis 中保存时使用 key
     *
     * @param userId
     * @return
     */
    private String getCartKey(String userId) {
        return RedisConst.USER_KEY_PREFIX + userId + RedisConst.USER_CART_KEY_SUFFIX;
    }

    /**
     * 给购物车数据设置过期时间
     *
     * @param cartKey
     */
    private void setCartKeyExpire(String cartKey) {
        redisTemplate.expire(cartKey, RedisConst.USER_CART_EXPIRE, TimeUnit.SECONDS);
    }

    @Override
    public List<CartInfo> getCartListFromDBToCache(String userId) {

        // 1、查询数据库
        QueryWrapper<CartInfo> queryWrapper = new QueryWrapper<CartInfo>().eq("user_id", userId);

        List<CartInfo> cartInfoList = cartInfoMapper.selectList(queryWrapper);

        // 2、不管 cartInfoList 是否包含有效数据，都清空 Redis 缓存
        String key = getCartKey(userId);
        redisTemplate.delete(key);

        // 3、如果 cartInfoList 包含有效数据则将有效数据存入 Redis 缓存
        if (!CollectionUtils.isEmpty(cartInfoList)) {

            HashOperations<String, Object, Object> operator = redisTemplate.opsForHash();

            // 3、为了每一个 cartInfo 对象存入 Redis，作为 hash 结构中的 value 部分，需要遍历 cartInfoList
            for (CartInfo cartInfo : cartInfoList) {

                Long skuId = cartInfo.getSkuId();

                // -------------附加功能：重新读取正确的价格-------------
                BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId).getData();
                cartInfo.setCartPrice(skuPrice);
                cartInfo.setSkuPrice(skuPrice);

                // ---------------------------------------------------

                operator.put(key, skuId.toString(), cartInfo);
            }

            // 4、设置缓存过期时间
            setCartKeyExpire(key);

        }

        // 5、集合判定为无效，就返回一个空集合
        return cartInfoList;
    }

    @Override
    @Transactional(readOnly = false)
    public List<CartInfo> getCartList(String userId, String userTempId) {

        // 1、根据 userId 查询购物车中数据列表
        List<CartInfo> cartInfoList_Login = getCartListFromCacheFirst(userId);

        // 2、根据 userTempId 查询购物车中数据列表
        List<CartInfo> cartInfoList_Not_Login = getCartListFromCacheFirst(userTempId);

        // 3、返回合并结果
        return mergeCartInfoList(cartInfoList_Login, cartInfoList_Not_Login, userId, userTempId);
    }

    /**
     * 执行购物车数据的合并
     *
     * @param cartInfoList_login     登录状态下添加到购物车的集合数据
     * @param cartInfoList_not_login 未登录状态下添加到购物车的集合数据
     * @param userId                 用户正式登录的 id
     * @param userTempId             用户临时登录的 id
     * @return 合并之后的购物车集合数据
     */
    @Transactional
    public List<CartInfo> mergeCartInfoList(
            List<CartInfo> cartInfoList_login,
            List<CartInfo> cartInfoList_not_login,
            String userId,
            String userTempId) {

        // 第一种情况：登录集合为空，未登录集合为空
        if (CollectionUtils.isEmpty(cartInfoList_login) && CollectionUtils.isEmpty(cartInfoList_not_login)) {
            return new ArrayList<>();
        }

        // 第二种情况：登录集合非空，未登录集合为空
        if (!CollectionUtils.isEmpty(cartInfoList_login) && CollectionUtils.isEmpty(cartInfoList_not_login)) {
            return cartInfoList_login;
        }

        // 第三种情况：登录集合为空，未登录集合非空
        if (CollectionUtils.isEmpty(cartInfoList_login) && CollectionUtils.isEmpty(cartInfoList_not_login)) {
            return cartInfoList_not_login;
        }

        // 第四种情况：登录集合非空，未登录集合非空
        // 设定目标：把未登录集合数据合并进已经登录的集合中
        // 关注点：未登录集合中的每一个数据 cartInfo_Not_Login
        // cartInfo_Not_Login 无非是两种情况：
        //      第一种情况：skuId 值在已登录集合中存在——skuNum 叠加操作
        //      第二种情况：skuId 值在已登录集合中不存在
        //          第一步：把 cartInfo_Not_Login 存入已登录集合
        //          第二步：把 cartInfo_Not_Login 对象的 userId 属性值设置成正式的 userId
        if (!CollectionUtils.isEmpty(cartInfoList_login) && !CollectionUtils.isEmpty(cartInfoList_not_login)) {

            // --------------------------- Java 内存中的操作 ---------------------------
            // 把已登录集合转换为 Map 形式
            // Map 的键：skuId
            // Map 的值：CartInfo 对象
            // ※友情提示：不管是常规办法还是高(装)端(逼)办法，只要能够实现功能就是好办法。工作中要“先完成再完美”。
            // 调用 StreamAPI 实现 List 转 Map 操作，关键就是告诉 StreamAPI：谁是键，谁是值即可。
            // 下面的转换代码就是两个层面：
            //      第一个层面：API 限定的固定写法（记住）
            //      第二个层面：思路（理解：遍历 List，提供 Map 所需要的 key、value 即可）
            Map<Long, CartInfo> cartInfoMap_Login =
                    cartInfoList_login
                            .stream()
                            .collect(
                                    Collectors.toMap(
                                            // 给新 Map 提供 Key：
                                            CartInfo::getSkuId,

                                            // 给新 Map 提供 Value：
                                            (CartInfo cartInfo) -> cartInfo)
                            );

            // 遍历未登录集合，从而得到每一个未登录的购物车对象：cartInfo_Not_Login
            for (CartInfo cartInfo_Not_Login : cartInfoList_not_login) {

                // 从 cartInfo_Not_Login 对象中获取 skuId
                Long skuId = cartInfo_Not_Login.getSkuId();

                // 检查未登录的 skuId 在已登录的 Map 集合中是否存在
                if (cartInfoMap_Login.containsKey(skuId)) {

                    // 存在：叠加数量（从未登录往已登录叠加）
                    // [1]获取未登录 skuNum
                    Integer skuNum_not_login = cartInfo_Not_Login.getSkuNum();

                    // [2]获取已登录 skuNum
                    CartInfo cartInfo_login = cartInfoMap_Login.get(skuId);
                    Integer skuNum_login = cartInfo_login.getSkuNum();

                    // [3]加
                    Integer skuNumMerged = skuNum_login + skuNum_not_login;

                    // [4]设
                    cartInfo_login.setSkuNum(skuNumMerged);

                    // 设置选中状态：只要未登录购物车是选中的，就把已登录购物车也设置选中
                    Integer isChecked = cartInfo_Not_Login.getIsChecked();

                    if (isChecked == 1) {
                        // 将已登录购物车数据设置为选中
                        cartInfo_login.setIsChecked(isChecked);
                    }

                } else {

                    // 不存在
                    // [1]把 cartInfo_Not_Login 的 userId 属性改成正式用户 id
                    cartInfo_Not_Login.setUserId(userId);

                    // [2]把 cartInfo_Not_Login 存入已登录集合
                    cartInfoList_login.add(cartInfo_Not_Login);

                }

            }

            // --------------------------- 把合并之后的数据重新写入 MySQL 数据库 ---------------------------
            // 1、把正式用户 id 和临时用户 id 对应的购物车数据全部删除
            // [1]封装 delete 语句对应的查询条件。
            // ※说明：因为 user_id 字段不可能既等于正式 id，又等于临时 id，所以两个 eq() 之间是 or 的关系
            QueryWrapper<CartInfo> cartInfoQueryWrapper = new QueryWrapper<CartInfo>()
                    .eq("user_id", userId)
                    .or()
                    .eq("user_id", userTempId);

            // [2]执行删除操作
            cartInfoMapper.delete(cartInfoQueryWrapper);

            // 2、把已合并的已登录的购物车集合数据来一个批量保存
            cartInfoMapper.batchInsert(cartInfoList_login);

            // --------------------------- 把合并之后的数据重新写入 Redis ---------------------------
            // 1、获取 Redis 的 Hash 类型的操作器对象
            HashOperations<String, Object, Object> operator = redisTemplate.opsForHash();

            // 2、删除正式用户对应的缓存数据
            // ※说明：写入合并后数据时，旧的数据都会被覆盖，所以不需要执行删除

            // 3、删除临时用户对应的缓存数据
            String cartKeyTemp = getCartKey(userTempId);
            redisTemplate.delete(cartKeyTemp);

            // 4、写入合并后的购物车数据
            String cartKey = getCartKey(userId);
            for (CartInfo cartInfo : cartInfoList_login) {
                operator.put(cartKey, cartInfo.getSkuId().toString(), cartInfo);
            }

            // 5、重新设置过期时间
            setCartKeyExpire(cartKey);

            return cartInfoList_login;

        }

        // 前面四种情况已经涵盖了所有可能性，所以实际执行的时候执行不到这里
        // 所以这里只要有一个 return 即可（编译的要求）
        return null;
    }

    @Override
    public List<CartInfo> getCartListFromCacheFirst(String userId) {

        // 0、对 userId 进行判空
        // 解释：用户没有登录，也没有添加购物车，直接访问查看购物车页面，此时正式和临时的 id 都没有
        if (StringUtils.isEmpty(userId)) {
            // 此时返回空集合
            return new ArrayList<>();
        }

        // 1、拼接 CartKey 字符串
        String cartKey = getCartKey(userId);

        // 2、尝试查询缓存
        HashOperations operator = redisTemplate.opsForHash();

        List<CartInfo> cartInfoList = operator.values(cartKey);

        // 3、对 cartInfoList 执行判空操作
        if (!CollectionUtils.isEmpty(cartInfoList)) {
            cartInfoList = getCartListFromDBToCache(userId);

            // 考虑到从数据库查询之后仍然有可能查不到数据（用户登录后没有添加购物车直接进入购物车页面）
            // 所以再次判空，如果有值则排序
            if (!CollectionUtils.isEmpty(cartInfoList)) {
                // 想代码简洁一点，可以借助 IDEA 进行转换
                cartInfoList.sort(new Comparator<CartInfo>() {
                    @Override
                    public int compare(CartInfo o1, CartInfo o2) {
                        return o1.getId().compareTo(o2.getId());
                    }
                });
            }
        }

        return cartInfoList;
    }

    @Override
    public void addToCart(Long skuId, String userId, Integer skuNum) {

        // 一、根据 skuId 和 userId 到数据库查询具体的 CartInfo 对象
        // 1、封装查询条件
        QueryWrapper<CartInfo> queryWrapper = new QueryWrapper<CartInfo>()
                .eq("sku_id", skuId)
                .eq("user_id", userId);

        // 2、执行查询
        CartInfo cartInfo = cartInfoMapper.selectOne(queryWrapper);

        if (cartInfo != null) {
            // 二、CartInfo 对象存在的情况（数量合并，价格重新查询）
            // 1、获取当前 CartInfo 对象中的商品数量
            Integer skuNumFromDB = cartInfo.getSkuNum();

            // 2、执行商品数量的累加
            Integer skuNumNew = skuNumFromDB + skuNum;

            // 3、给 CartInfo 对象设置最新的商品数量
            cartInfo.setSkuNum(skuNumNew);

            // 4、重新查询价格
            Result<BigDecimal> skuPriceResult = productFeignClient.getSkuPrice(skuId);
            BigDecimal skuPrice = skuPriceResult.getData();

            // 5、给 CartInfo 对象设置最新的商品价格
            cartInfo.setSkuPrice(skuPrice);

            // 6、更新数据库中的对应记录
            // 同步操作
            // cartInfoMapper.updateById(cartInfo);

            // 异步操作
            cartAsyncService.updateCartInfo(cartInfo);

        } else {
            // 三、CartInfo 对象不存在的情况（添加到购物车）
            // 1、创建新的 CartInfo 对象赋值 cartInfo 变量
            cartInfo = new CartInfo();

            // 2、根据 skuId 查询 SkuInfo 对象
            Result<SkuInfo> skuInfoResult =
                    productFeignClient.getSkuInfo(skuId);
            SkuInfo skuInfo = skuInfoResult.getData();

            // 3、根据远程接口查询到的 SkuInfo 对象设置 CartInfo 对象的属性
            cartInfo.setUserId(userId);
            cartInfo.setSkuId(skuId);
            cartInfo.setCartPrice(skuInfo.getPrice());

            // 当前 cartInfo 在数据库不存在，所以 skuNum 就是初始数量值
            cartInfo.setSkuNum(skuNum);

            cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setSkuPrice(skuInfo.getPrice());

            // 4、执行数据库保存操作
            // 同步操作
            // cartInfoMapper.insert(cartInfo);

            // 异步操作 --> 在数据库执行保存操作未完成时，Redis已经保存。
            // 导致 Redis 保存的 CartInfo 没有 id 值
            // cartAsyncService.saveCartInfo(cartInfo);
            cartInfoMapper.insert(cartInfo);
        }

        // 四、更新缓存、重新设置过期时间
        // 1、更新缓存
        // 不管前面走的是哪个分支，代码执行到这里，CartInfo 对象保证是有值的
        // 所以这里就拿 CartInfo 对象执行更新缓存操作即可
        // 原来如果有值：覆盖
        // 原来如果无值：保存
        String cartKey = getCartKey(userId);

        HashOperations<String, Object, Object> operator = redisTemplate.opsForHash();

        operator.put(cartKey, skuId.toString(), cartInfo);

        // 2、设置过期时间
        setCartKeyExpire(cartKey);

    }

    @Override
    @Transactional(readOnly = false)
    public void modifyCartCheckStatus(String userId, Long skuId, Integer isChecked) {
        // 1、数据库修改
        cartInfoMapper.updateCartStatus(userId, skuId, isChecked);

        // 2、Redis 修改
        // [1]拼接得到 Redis 中数据的 Key
        String cartKey = getCartKey(userId);

        // [2]获取操作器对象
        BoundHashOperations<String, Object, Object> boundOperator = redisTemplate.boundHashOps(cartKey);

        // [3]根据 skuId 从购物车数据中取出一条具体的购物项数据
        CartInfo cartInfo = (CartInfo) boundOperator.get(skuId.toString());

        // [4]做判空操作，如果 CartInfo 对象为空则当前方法停止执行
        if (cartInfo == null) {
            return;
        }

        // [5]修改 CartInfo 对象的选中状态
        cartInfo.setIsChecked(isChecked);

        // [6]存回去
        boundOperator.put(skuId.toString(), cartInfo);

        // [7]重新设置缓存数据的过期时间
        setCartKeyExpire(cartKey);

    }

    @Override
    @Transactional(readOnly = false)
    public void removeCartItem(String userId, Long skuId) {
        // 1、到数据库删除
        cartInfoMapper
                .delete(
                        new QueryWrapper<CartInfo>()
                                .eq("user_id", userId)
                                .eq("sku_id", skuId));

        // 2、到 Redis 删除
        // [1]拼接购物车数据的 Key
        String cartKey = getCartKey(userId);

        // [2]获取操作器对象
        BoundHashOperations<String, Object, Object> boundOperator = redisTemplate.boundHashOps(cartKey);

        // [3]执行删除
        boundOperator.delete(skuId.toString());
    }

    @Override
    @Transactional
    public void clearCheckedCartItem(String userId) {
        // 1、删除数据库中的数据
        cartInfoMapper.delete(
                new QueryWrapper<CartInfo>()
                        .eq("user_id", userId)
                        .eq("is_checked", 1));

        // 2、到数据库重新查询，然后存入 Redis 缓存
        // ※上面经过数据库删除之后，剩下的都是没有勾选的购物项
        getCartListFromDBToCache(userId);

    }
}
