import request from '@/utils/request'

// 查询预缴费提醒列表
export function queryPrePayment(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics.queryPrePayment',
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

// 查询到期提醒列表
export function queryDeadlineFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics.queryDeadlineFee',
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

// 导出数据
export function exportData(params) {
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

// 查询费用项列表
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

// 查询已入驻小区列表
export function listMyEnteredCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listMyEnteredCommunitys',
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