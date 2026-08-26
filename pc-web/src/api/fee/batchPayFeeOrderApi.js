import request from '@/utils/request'

// 获取业主费用列表
export function listOwnerFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listOwnerFee',
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

// 批量支付费用
export function payBatchFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.payBatchFee',
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

// 扫码支付
export function qrCodePayment(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.qrCodePayment',
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

// 检查支付完成状态
export function checkPayFinish(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.checkPayFinish',
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

// 查询业主账户
export function queryCommunityOwnerAccount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/account.queryCommunityOwnerAccount',
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

// 查询预付款费用规则
export function listPrePayFeeRule(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.listPrePayFeeRule',
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

// 查询费用收据
export function queryFeeReceipt(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeReceipt/queryFeeReceipt',
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

// 查询费用押金
export function queryFeeDeposit(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryFeeDeposit',
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

// 计算费用折扣
export function computeFeeDiscount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeDiscount.computeFeeDiscount',
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

// 计算预付款折扣
export function computePreFeeDiscount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeDiscount.computePreFeeDiscount',
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

// 获取打印页面列表
export function listFeePrintPage(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feePrintPage.listFeePrintPage',
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