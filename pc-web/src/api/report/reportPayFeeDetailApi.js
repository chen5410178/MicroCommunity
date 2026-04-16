import request from '@/utils/request'

// 获取缴费订单列表
export function listPayFeeOrder(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeOrder.listPayFeeOrder',
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

// 查询缴费明细
export function queryPayFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics/queryPayFeeDetail',
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

// 获取预付费用列表
export function listPrePayFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.listPrePayFee',
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

// 导出缴费订单
export function exportPayFeeOrder(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
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

// 导出缴费明细
export function exportPayFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
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

// 获取费用配置列表
export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.listFeeConfigs',
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