import request from '@/utils/request'

// 获取合同变更详情
export function getContractChangeDetails(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractChangePlanDetail',
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

// 获取审核用户列表
export function getAuditUsers(params) {
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