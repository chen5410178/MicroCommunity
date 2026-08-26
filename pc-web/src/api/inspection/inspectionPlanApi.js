import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'



// 查询巡检计划列表
export function listInspectionPlans(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionPlan.listInspectionPlans',
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

// 更新巡检计划状态
export function updateInspectionPlanState(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionPlan.updateInspectionPlanState',
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

// 删除巡检计划
export function deleteInspectionPlan(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionPlan.deleteInspectionPlan',
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

// 更新巡检计划
export function updateInspectionPlan(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionPlan.updateInspectionPlan',
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

// 查询巡检路线列表
export function listInspectionRoutes(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.listInspectionRoutes',
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

// 查询巡检计划员工
export function listInspectionPlanStaffs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspection.listInspectionPlanStaffs',
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
export function listOrgTree() {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
      method: 'get',
      params: { 
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

// 根据组织ID查询员工
export function listStaffsByOrgId(params) {
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