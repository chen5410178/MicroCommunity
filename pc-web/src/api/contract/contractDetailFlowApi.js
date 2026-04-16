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
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询工作流审核信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}