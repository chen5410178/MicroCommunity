import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取采购申请详情列表
export function listPurchaseApplys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/purchaseApply.listPurchaseApplys',
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

// 审核采购申请
export function auditApplyOrder(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/purchaseApply.auditApplyOrder',
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

export function auditAllocationStoreOrder(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.auditAllocationStoreOrder',
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

// 获取工作流审核信息
export function listWorkflowAuditInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow.listWorkflowAuditInfo',
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

// 查询下一处理人
export function queryNextDealUser(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow.queryNextDealUser',
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

// 获取组织树
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
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

// 查询员工信息
export function queryStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
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