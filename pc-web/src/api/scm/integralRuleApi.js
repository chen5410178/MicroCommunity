import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 积分规则相关API
export function listIntegralRule(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.listIntegralRule',
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

export function saveIntegralRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.saveIntegralRule',
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

export function updateIntegralRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.updateIntegralRule',
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

export function deleteIntegralRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.deleteIntegralRule',
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

// 积分标准配置相关API
export function listIntegralRuleConfig(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.listIntegralRuleConfig',
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

export function saveIntegralRuleConfig(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.saveIntegralRuleConfig',
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

export function deleteIntegralRuleConfig(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.deleteIntegralRuleConfig',
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

// 积分标准池相关API
export function listIntegralConfig(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.listIntegralConfig',
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

// 缴费时间相关API
export function listIntegralRuleFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.listIntegralRuleFee',
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

export function saveIntegralRuleFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.saveIntegralRuleFee',
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

export function updateIntegralRuleFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.updateIntegralRuleFee',
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

export function deleteIntegralRuleFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.deleteIntegralRuleFee',
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