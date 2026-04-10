import request from '@/utils/request'

// 获取供应商列表
export function listSupplier(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplier.listSupplier',
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

// 获取优惠券列表
export function listSupplierCoupon(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplierCoupon.listSupplierCoupon',
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