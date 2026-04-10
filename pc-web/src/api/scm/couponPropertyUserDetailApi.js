import request from '@/utils/request'

// 获取优惠券使用记录列表
export function listCouponPropertyUserDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponProperty.listCouponPropertyUserDetail',
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

// 核销优惠券
export function writeOffCouponPropertyUser(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponProperty.writeOffCouponPropertyUser',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}