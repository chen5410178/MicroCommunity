import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取优惠券规则列表
export function listCouponRule(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponRule.listCouponRule',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加优惠券规则
export function saveCouponRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponRule.saveCouponRule',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新优惠券规则
export function updateCouponRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponRule.updateCouponRule',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除优惠券规则
export function deleteCouponRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponRule.deleteCouponRule',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取优惠券规则关联的优惠券列表
export function listCouponRuleCpps(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponRule.listCouponRuleCpps',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加优惠券规则关联的优惠券
export function saveCouponRuleCpps(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponRule.saveCouponRuleCpps',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新优惠券规则关联的优惠券
export function updateCouponRuleCpps(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponRule.updateCouponRuleCpps',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除优惠券规则关联的优惠券
export function deleteCouponRuleCpps(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponRule.deleteCouponRuleCpps',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取优惠券规则关联的缴费赠送列表
export function listCouponRuleFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponRule.listCouponRuleFee',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加优惠券规则关联的缴费赠送
export function saveCouponRuleFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponRule.saveCouponRuleFee',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除优惠券规则关联的缴费赠送
export function deleteCouponRuleFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponRule.deleteCouponRuleFee',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取优惠券属性池列表
export function listCouponPropertyPool(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/couponProperty.listCouponPropertyPool',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取费用配置列表
export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.listFeeConfigs',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}