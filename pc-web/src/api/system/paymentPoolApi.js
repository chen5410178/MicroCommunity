import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取支付池列表
export function listPaymentPool(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.listPaymentPool',
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

// 添加支付配置
export function savePaymentPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.savePaymentPool',
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

// 修改支付配置
export function updatePaymentPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.updatePaymentPool',
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

// 删除支付配置
export function deletePaymentPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.deletePaymentPool',
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

// 获取支付适配器列表
export function listPaymentAdapt(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.listPaymentAdapt',
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

// 获取支付密钥列表
export function listPaymentKey(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.listPaymentKey',
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

// 获取支付配置详情
export function getPaymentPoolDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.listPaymentPool',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId(),
        page: 1,
        row: 1
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

// 上传文件
export function uploadFile(data, config) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/upload/uploadVedio/upload',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      ...config
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}