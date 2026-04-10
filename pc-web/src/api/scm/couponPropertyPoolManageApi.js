import request from '@/utils/request'

// 获取优惠券池列表
export function listCouponPropertyPool(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponProperty.listCouponPropertyPool',
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

// 新增优惠券池
export function saveCouponPropertyPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponProperty.saveCouponPropertyPool',
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

// 更新优惠券池
export function updateCouponPropertyPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponProperty.updateCouponPropertyPool',
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

// 删除优惠券池
export function deleteCouponPropertyPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponProperty.deleteCouponPropertyPool',
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

// 赠送优惠券
export function giftCouponProperty(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponProperty.giftCouponProperty',
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

// 获取优惠券属性
export function listCouponKey(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponKey.listCouponKey',
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