import request from '@/utils/request'

// 查询工作流审核信息
export function listWorkflowAuditInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: 'workflow.listWorkflowAuditInfo',
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