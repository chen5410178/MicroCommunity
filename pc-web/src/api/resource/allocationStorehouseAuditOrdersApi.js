import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取调拨仓库审核订单列表
export function listAllocationStoreAuditOrders(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listAllocationStoreAuditOrders',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取工作流步骤员工
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

// 审核订单信息
export function auditOrderInfo(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.auditAllocationStoreAuditOrder',
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