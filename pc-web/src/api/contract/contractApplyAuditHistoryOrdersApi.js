import request from '@/utils/request'

/**
 * 查询合同历史任务列表
 * @param {Object} params 查询参数
 * @param {number} params.page 当前页码
 * @param {number} params.row 每页显示条数
 * @param {string} [params.AuditOrdersId] 审核订单ID
 * @param {string} [params.userName] 用户名
 * @param {string} [params.auditLink] 审核环节
 * @returns {Promise} 包含合同历史任务数据的Promise对象
 */
export function queryContractHistoryTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractHistoryTask',
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
 * 提交合同审核
 * @param {Object} data 审核数据
 * @param {number} data.state 审核状态 (1100: 同意, 1200: 拒绝)
 * @param {string} data.remark 审核备注
 * @param {string} data.contractId 合同ID
 * @returns {Promise} 包含审核结果的Promise对象
 */
export function submitContractAudit(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/submitContractAudit',
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
 * 获取合同详情
 * @param {string} contractId 合同ID
 * @returns {Promise} 包含合同详情的Promise对象
 */
export function getContractDetail(contractId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/getContractDetail',
      method: 'get',
      params: { contractId }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}