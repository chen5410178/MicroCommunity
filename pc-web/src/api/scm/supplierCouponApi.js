import request from '@/utils/request'

// 获取供应商列表
export function getSupplierList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplier.listSupplier',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get supplier list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取优惠券列表
export function getSupplierCouponList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplierCoupon.listSupplierCoupon',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get coupon list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加优惠券
export function addSupplierCoupon(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplierCoupon.saveSupplierCoupon',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to add coupon'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新优惠券
export function updateSupplierCoupon(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplierCoupon.updateSupplierCoupon',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to update coupon'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除优惠券
export function deleteSupplierCoupon(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplierCoupon.deleteSupplierCoupon',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to delete coupon'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}