import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取巡检路线列表
export function listInspectionRoutes(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.listInspectionRoutes',
      method: 'get',
      params: {
        communityId: getCommunityId(),
        ...params
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加巡检路线
export function saveInspectionRoute(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.saveInspectionRoute',
      method: 'post',
      data: {
        communityId: getCommunityId(),
        ...data
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 修改巡检路线
export function updateInspectionRoute(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.updateInspectionRoute',
      method: 'post',
      data: {
        communityId: getCommunityId(),
        ...data
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除巡检路线
export function deleteInspectionRoute(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.deleteInspectionRoute',
      method: 'post',
      data: {
        communityId: getCommunityId(),
        ...data
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取巡检路线下的巡检点
export function listInspectionRoutePoints(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.listInspectionRoutePoints',
      method: 'get',
      params: {
        communityId: getCommunityId(),
        ...params
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取所有巡检点(用于选择)
export function listInspectionPoints(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionPoint.listInspectionPoints',
      method: 'get',
      params: {
        communityId: getCommunityId(),
        ...params
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加巡检点到路线
export function saveInspectionRoutePoint(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.saveInspectionRoutePoint',
      method: 'post',
      data: {
        communityId: getCommunityId(),
        ...data
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 修改巡检点关联信息
export function updateInspectionRoutePointRel(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.updateInspectionRoutePointRel',
      method: 'post',
      data: {
        communityId: getCommunityId(),
        ...data
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除巡检点关联
export function deleteInspectionRoutePoint(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.deleteInspectionRoutePoint',
      method: 'post',
      data: {
        communityId: getCommunityId(),
        ...data
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询路线下的巡检计划
export function queryRouteInspectionPlan(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspection.queryRouteInspectionPlan',
      method: 'get',
      params: {
        communityId: getCommunityId(),
        ...params
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询路线下的巡检任务
export function queryRouteInspectionTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspection.queryRouteInspectionTask',
      method: 'get',
      params: {
        communityId: getCommunityId(),
        ...params
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
        communityId: getCommunityId(),
        ...params
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}