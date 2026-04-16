import request from '@/utils/request'

/**
 * 查询合同变更历史任务
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function queryContractChangeHistoryTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractChangeHistoryTask',
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