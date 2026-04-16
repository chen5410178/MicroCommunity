import request from '@/utils/request'

// 获取退费申请列表
export function listReturnPayFees(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/returnPayFee.listReturnPayFees',
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

// 更新退费申请状态
export function updateReturnPayFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/returnPayFee.updateReturnPayFee',
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

// 审核预付单退款状态
export function auditRefundPrePayFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/refundFee.auditRefundPrePayFee',
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

// 审核押金退款状态
export function auditRefundDeposit(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/refundFee.auditRefundDeposit',
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

// 重试退款
export function tryRefundMoney(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/returnPayFee.tryRefundMoney',
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