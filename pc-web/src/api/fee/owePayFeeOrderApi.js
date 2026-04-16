import request from '@/utils/request'

/**
 * 获取欠费列表
 * @param {Object} params 查询参数
 * @returns {Promise} 
 */
export function listOweFees(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeApi/listOweFees',
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

/**
 * 缴费
 * @param {Object} data 缴费数据
 * @returns {Promise}
 */
export function payOweFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.payOweFee',
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

/**
 * 扫码支付
 * @param {Object} data 支付数据
 * @returns {Promise}
 */
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

/**
 * 检查支付状态
 * @param {Object} data 检查参数
 * @returns {Promise}
 */
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

/**
 * 获取打印页面列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listFeePrintPages(params) {
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