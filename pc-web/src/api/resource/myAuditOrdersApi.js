import request from '@/utils/request'

// 获取待审核订单列表
export function listAuditOrders(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/auditUser.listAuditOrders',
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

// 获取工作流步骤人员
export function listWorkflowStepStaffs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow.listWorkflowStepStaffs',
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

// 审核订单
export function auditOrderInfo(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/auditUser.notifyAudit',
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