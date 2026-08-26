import request from '@/utils/request'

// 获取小区支付列表
export function listAdminPayment(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.listAdminPayment',
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

// 保存支付配置
export function saveAdminPaymentPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.saveAdminPaymentPool',
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

// 更新支付配置
export function updateAdminPaymentPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.updateAdminPaymentPool',
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

// 删除支付配置
export function deleteAdminPaymentPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.deleteAdminPaymentPool',
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

// 获取支付厂商列表
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

// 获取费用项列表
export function queryAdminFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.queryAdminFeeConfigs',
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