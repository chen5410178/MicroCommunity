import request from '@/utils/request'

// 获取预付费用规则信息
export function getFeeRuleInfo(params) {
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

// 获取水电费预付规则信息
export function getMeterFeeRuleInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.listMeterPayFeeRule',
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

// 计算预付费用折扣
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

// 二维码支付
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

// 预付费用
export function prePayFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.prePayFee',
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

// 获取收据信息
export function getReceiptInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/receipt.getReceiptInfo',
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

// 获取支付方式字典
export function getPaymentMethods() {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.getDictByType',
      method: 'get',
      params: {
        dictType: 'pay_fee_detail',
        dictCode: 'prime_rate'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取小区ID
export function getCommunityId() {
  const communityInfo = JSON.parse(localStorage.getItem('communityInfo') || '{}')
  return communityInfo.communityId || ''
}

// 导出收据
export function exportReceipt(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/receipt.exportReceipt',
      method: 'post',
      data,
      responseType: 'blob'
    }).then(response => {
      resolve(response)
    }).catch(error => {
      reject(error)
    })
  })
}

// 打印收据
export function printReceipt(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/receipt.printReceipt',
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

// 获取预付费用历史记录
export function getPrePayFeeHistory(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.listPrePayFeeHistory',
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

// 取消预付费用
export function cancelPrePayFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.cancelPrePayFee',
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

// 获取预付费用详情
export function getPrePayFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.getPrePayFeeDetail',
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