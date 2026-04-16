import request from '@/utils/request'

/**
 * 获取历史投诉单列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listAuditHistoryComplaints(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/auditUser.listAuditHistoryComplaints',
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