import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取调拨仓库申请列表
export function listAllocationStorehouseApplys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listAllocationStorehouseApplys',
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

// 获取调拨仓库物品列表
export function listAllocationStorehouses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listAllocationStorehouses',
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

// 审核调拨仓库订单
export function auditAllocationStoreOrder(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.auditAllocationStoreOrder',
      method: 'post',
      data: {
        ...data,
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

// 查询工作流审核信息
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

// 查询组织树
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