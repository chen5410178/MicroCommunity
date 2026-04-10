import request from '@/utils/request'

export function listAdminCouponPropertyUser(params) {
  return new Promise((resolve, reject) => {
    return request({
    url: '/couponProperty.listAdminCouponPropertyUser',
    method: 'get',
    params
  }).then(res => {
      resolve(res.data)
    }).catch(err => {
      reject(err)
    })
  })
}