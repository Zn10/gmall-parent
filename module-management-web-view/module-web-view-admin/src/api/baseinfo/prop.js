import request from '@/utils/request'

const api_name = '/admin/product'

export default {

  // 查找一级分类
  getCategory1() {
    return request({
      url: `${api_name}/getCategory1`,
      method: 'get'
    })
  },

  // 查找二级分类
  getCategory2(category1Id) {
    return request({
      url: `${api_name}/getCategory2/` + category1Id,
      method: 'get'
    })
  },

  // 查找三级分类
  getCategory3(category2Id) {
    return request({
      url: `${api_name}/getCategory3/` + category2Id,
      method: 'get'
    })
  },

  // 查找品牌
  getTrademarkList() {
    return request({
      url: `${api_name}/baseTrademark/getTrademarkList`,
      method: 'get'
    })
  },

  // 根据分类id获取属性列表
  getAttrInfoList(category1Id, category2Id, category3Id) {
    return request({
      url: `${api_name}/attrInfoList/` + category1Id + '/' + category2Id + '/' + category3Id,
      method: 'get'
    })
  },

  // 根据属性id获取属性值列表
  getAttrValueList(attrId) {
    return request({
      url: `${api_name}/getAttrValueList/` + attrId,
      method: 'get'
    })
  },

  // 保存属性
  saveAttrInfo(attrForm) {
    return request({
      url: `${api_name}/saveAttrInfo`,
      method: 'post',
      data: attrForm
    })
  },
  // 根据属性id删除属性
  remove(attrId) {
    return request({
      url: `${api_name}/remove/${attrId}`,
      method: 'delete'
    })
  }
}
