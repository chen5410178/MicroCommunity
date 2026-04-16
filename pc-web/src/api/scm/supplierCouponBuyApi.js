import request from '@/utils/request'

/**
 * 获取优惠券购买记录列表
 * @param {Object} params 查询参数
 * @returns {Promise} 包含优惠券购买记录列表的Promise
 */
export function getSupplierCouponBuyList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplierCoupon.listSupplierCouponBuy',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total,
          records: res.records
        })
      } else {
        reject(new Error(res.msg || 'Failed to get coupon purchase records'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}