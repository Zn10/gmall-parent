import request from '@/utils/request'

const api_name = '/admin/product'

export default {

  getPageList(page, limit) {
    return request({
      url: `${api_name}/list/${page}/${limit}`,
      method: 'get'
    })
  },

  // 保存Sku
  saveSkuInfo(skuForm) {
    return request({
      url: `${api_name}/saveSkuInfo`,
      method: 'post',
      data: skuForm
    })
  },

  // 商品上架
  onSale(skuId) {
    return request({
      url: `${api_name}/onSale/${skuId}`,
      method: 'get'
    })
  },

  // 商品下架
  cancelSale(skuId) {
    return request({
      url: `${api_name}/cancelSale/${skuId}`,
      method: 'get'
    })
  },

  findSkuInfoByKeyword(keyword) {
    return request({
      url: `${api_name}/findSkuInfoByKeyword/${keyword}`,
      method: 'get'
    })
  }
}
