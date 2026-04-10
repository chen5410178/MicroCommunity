import request from '@/utils/request'

// 获取业主优惠券列表
export function listCouponPropertyUser(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponProperty.listCouponPropertyUser',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}