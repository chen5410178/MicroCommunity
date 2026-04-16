import request from '@/utils/request'

// 获取优惠券池列表
export function listCouponPool(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponPool.listCouponPool',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取优惠券池列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加优惠券池
export function saveCouponPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponPool.saveCouponPool',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加优惠券池失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新优惠券池
export function updateCouponPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponPool.updateCouponPool',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新优惠券池失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除优惠券池
export function deleteCouponPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponPool.deleteCouponPool',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除优惠券池失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}