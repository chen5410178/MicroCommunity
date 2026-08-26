import request from '@/utils/request'

// 获取费用项详情
export function getFeeConfigDetail(params) {
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

// 查询费用项历史记录
export function queryHisFeeConfig(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryHisFeeConfig',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total,
        records: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询费用项对象列表
export function listConfigFeeObjs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.listConfigFeeObjs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total,
        records: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询费用项折扣
export function queryPayFeeConfigDiscount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeConfigDiscount/queryPayFeeConfigDiscount',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total,
        records: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除费用项折扣
export function deletePayFeeConfigDiscount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeConfigDiscount/deletePayFeeConfigDiscount',
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