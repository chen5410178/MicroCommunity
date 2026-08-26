import request from '@/utils/request'

// 获取优惠券详情列表
export function listCouponDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponDetail.listCouponDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加优惠券详情
export function saveCouponDetail(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponDetail.saveCouponDetail',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新优惠券详情
export function updateCouponDetail(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponDetail.updateCouponDetail',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除优惠券详情
export function deleteCouponDetail(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponDetail.deleteCouponDetail',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}