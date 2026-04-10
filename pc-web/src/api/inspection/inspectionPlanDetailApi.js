import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取巡检计划详情
export function getInspectionPlanDetail(params) {
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

// 获取巡检计划人员列表
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

// 获取巡检路线列表
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

// 获取巡检路线点列表
export function listInspectionRoutePoints(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.listInspectionRoutePoints',
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

// 获取巡检点列表
export function listInspectionPoints(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionPoint.listInspectionPoints',
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

// 保存巡检路线点
export function saveInspectionRoutePoint(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.saveInspectionRoutePoint',
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

// 删除巡检路线点
export function deleteInspectionRoutePoint(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.deleteInspectionRoutePoint',
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

// 更新巡检路线点关系
export function updateInspectionRoutePointRel(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.updateInspectionRoutePointRel',
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

// 查询路线巡检任务
export function queryRouteInspectionTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspection.queryRouteInspectionTask',
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

// 查询巡检任务详情
export function queryInspectionTaskDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspection.queryInspectionTaskDetail',
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

// 获取巡检任务详情列表
export function listInspectionTaskDetails(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionTaskDetail.listInspectionTaskDetails',
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