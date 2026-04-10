import request from '@/utils/request'

/**
 * 查询OA工作流列表
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function queryOaWorkflow(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/queryOaWorkflow',
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
 * 检查工作流是否部署
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function checkWorkflowDeployment(params) {
  return queryOaWorkflow(params)
}