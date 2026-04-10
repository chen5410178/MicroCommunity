import request from '@/utils/request'

/**
 * Get purchase apply list
 * @param {Object} params
 * @param {number} params.page - Page number
 * @param {number} params.row - Page size
 * @param {string} params.applyOrderId - Apply order ID
 * @param {string} params.resOrderType - Resource order type
 * @returns {Promise}
 */
export function listPurchaseApplys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/purchaseApply.listPurchaseApplys',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * Submit resource out
 * @param {Object} data - Resource out data
 * @param {string} data.applyOrderId - Apply order ID
 * @param {string} data.taskId - Task ID
 * @param {string} data.resOrderType - Resource order type
 * @param {Array} data.purchaseApplyDetailVo - Purchase apply details
 * @returns {Promise}
 */
export function submitResourceOut(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/collection/resourceOut',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}