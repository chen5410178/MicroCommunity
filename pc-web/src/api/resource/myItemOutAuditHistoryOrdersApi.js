import request from '@/utils/request'

/**
 * 获取领用已办单列表
 * @param {Object} params 查询参数
 * @returns {Promise} 请求Promise
 */
export function listItemOutAuditHistoryOrders(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/auditUser.listItemOutAuditHistoryOrders',
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