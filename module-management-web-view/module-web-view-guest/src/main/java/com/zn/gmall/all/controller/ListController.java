package com.zn.gmall.all.controller;

import com.alibaba.fastjson.JSONObject;
import com.zn.gmall.list.client.ListFeignClient;
import com.zn.gmall.model.list.SearchParam;
import com.zn.gmall.model.list.vo.SearchResponseVo;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 赵念
 * @create-date: 2023/2/11/13:47
 */
@Controller
public class ListController {

    @Autowired
    private ListFeignClient listFeignClient;


    @RequestMapping("/list.html")
    public String doSearch(SearchParam searchParam, Model model) throws Throwable {

        // 1、调用远程接口获取搜索结果数据
        SearchResponseVo searchResponseVo = (SearchResponseVo) listFeignClient.list(searchParam).getData();

        // 2、把搜索结果数据转换为 Map
        String jsonSearchResponseVo = JSONObject.toJSONString(searchResponseVo);

        Map mapSearchResponseVo = JSONObject.parseObject(jsonSearchResponseVo, Map.class);

        // 3、把 Map 整体存入模型
        model.addAllAttributes(mapSearchResponseVo);

        // 4、生成拼接的 urlParam 并存入模型
        String urlParam = generateUrlParam(searchParam);
        model.addAttribute("urlParam", urlParam);

        // 5、生成拼接的 orderMap 并存入模型
        Map<String, String> orderMap = generateOrderMap(searchParam);
        model.addAttribute("orderMap", orderMap);

        // 6、把品牌名称作为 trademarkParam 存入模型
        String trademarkParam = generateTrademark(searchParam);
        model.addAttribute("trademarkParam", trademarkParam);

        // 7、生成页面上平台属性附加条件所需数据
        List<Map<String, String>> propsParamList = generatePropsParamList(searchParam);
        model.addAttribute("propsParamList", propsParamList);

        return "list/index";
    }

    // 获取品牌显示
    private String generateTrademark(SearchParam searchParam) {
        String trademark = searchParam.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            String[] split = trademark.split(":");
            if (split.length == 2) {
                return "品牌:" + split[1];
            }
        }
        return "";
    }


    /**
     * 方法作用：为了给页面显示平台属性附加条件，组装数据结构
     * 数据结构：每一个平台属性条件都包含两个值
     * attrName：平台属性名
     * attrValue：平台属性值
     * <p>
     * 每一个平台属性条件数据用一个 Map 保存，Map 放在 List 中
     *
     * @param searchParam
     * @return
     */
    private List<Map<String, String>> generatePropsParamList(SearchParam searchParam) {

        List<Map<String, String>> propMapList = new ArrayList<>();

        String[] props = searchParam.getProps();
        if (!ArrayUtils.isEmpty(props)) {
            for (String prop : props) {
                String[] split = prop.split(":");
                if (split.length == 3) {
                    String attrId = split[0];
                    String attrValue = split[1];
                    String attrName = split[2];

                    Map<String, String> propMap = new HashMap<>();
                    propMap.put("attrId", attrId);
                    propMap.put("attrName", attrName);
                    propMap.put("attrValue", attrValue);

                    propMapList.add(propMap);
                }
            }
        }

        return propMapList;
    }


    /**
     * 页面显示排序相关数据时，需要使用这个 OrderMap
     * Map 结构：
     * type：1 或 2
     * 1表示基于 hotScore 排序
     * 2表示基于 price 排序
     * sort：asc 或 desc
     *
     * @param searchParam
     * @return
     */
    private Map<String, String> generateOrderMap(SearchParam searchParam) {

        // 1、创建 Map 集合用于存储数据
        Map<String, String> orderMap = new HashMap<>();

        // 2、从 SearchParam 中读取排序信息
        String order = searchParam.getOrder();

        // 3、检查 order 是否有值
        if (!StringUtils.isEmpty(order)) {
            String[] split = order.split(":");
            if (split.length == 2) {
                String orderField = split[0];
                String orderDirection = split[1];

                orderMap.put("type", orderField);
                orderMap.put("sort", orderDirection);
            }
        } else {
            // 4、如果 order 没有值，则设置默认值
            orderMap.put("type", "1");
            orderMap.put("sort", "asc");
        }

        return orderMap;
    }


    /**
     * 方法作用：拼接 urlParam 字符串，因为目标页面需要
     * 功能说明：urlParam 写入到页面后，用户在页面上再点击新的条件会附加到 urlParam 后形成新条件
     * 格式说明：urlParam 字符串包含两部分
     * 第一部分是请求地址：/list.html
     * 第二部分是请求地址后附加的请求参数
     * 语法要求的格式：/list.html?名=值&名=值&名=值
     * 业务要求的格式：SearchParam 实体类中的参数都拼接进去
     *
     * @param searchParam
     * @return
     */
    private String generateUrlParam(SearchParam searchParam) {

        // 1、创建 StringBuilder 对象用于执行字符串拼接
        StringBuilder urlParamBuilder = new StringBuilder("/list.html?");

        // 2、拼接分类参数
        Long category1Id = searchParam.getCategory1Id();
        if (category1Id != null) {
            urlParamBuilder.append("&category1Id=").append(category1Id);
        }

        Long category2Id = searchParam.getCategory2Id();
        if (category2Id != null) {
            urlParamBuilder.append("&category2Id=").append(category2Id);
        }

        Long category3Id = searchParam.getCategory3Id();
        if (category3Id != null) {
            urlParamBuilder.append("&category3Id=").append(category3Id);
        }

        // 3、拼接品牌参数
        String trademark = searchParam.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            urlParamBuilder.append("&trademark=").append(trademark);
        }

        // 4、拼接关键词参数
        String keyword = searchParam.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            urlParamBuilder.append("&keyword=").append(keyword);
        }

        // 5、拼接平台属性
        // ※拼好之后的数据举例：
        // &props=5:6GB:运行内存&props=4:128GB:机身存储
        // ※格式说明：多个请求参数使用同一个名字才能把这里的多个值存入数组
        // ※SearchParam 提供的是一个数组，所以我们需要遍历数组再拼接
        String[] props = searchParam.getProps();
        if (!ArrayUtils.isEmpty(props)) {
            for (String prop : props) {
                urlParamBuilder.append("&props=").append(prop);
            }
        }

        // 6、去掉有可能多出来的 & 符号
        // 正确情况举例：/list.html?名字=值&名字=值&名字=值&名字=值
        // 错误情况举例：/list.html?&名字=值&名字=值&名字=值&名字=值
        // [1]判断拼接结果中是否包含 & 符号
        String urlParam = urlParamBuilder.toString();

        if (urlParam.contains("&")) {
            // [2]把第一个 & 符号替换为空字符串
            urlParam = urlParam.replaceFirst("&", "");
        }

        return urlParam;
    }
}
