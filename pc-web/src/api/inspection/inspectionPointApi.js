import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取巡检点列表
export function listInspectionPoints(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionPoint.listInspectionPoints',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存巡检点
export function saveInspectionPoint(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionPoint.saveInspectionPoint',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新巡检点
export function updateInspectionPoint(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionPoint.updateInspectionPoint',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除巡检点
export function deleteInspectionPoint(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionPoint.deleteInspectionPoint',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取巡检明细列表
export function listInspectionTaskDetails(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionTaskDetail.listInspectionTaskDetails',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取巡检路线列表
export function listInspectionRoutes(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionRoute.listInspectionRoutes',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取巡检路线点列表
export function listInspectionRoutePoints(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionRoute.listInspectionRoutePoints',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取巡检计划列表
export function queryPointInspectionPlan(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspection.queryPointInspectionPlan',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取巡检任务列表
export function queryPointInspectionTask(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspection.queryPointInspectionTask',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取巡检任务明细
export function queryInspectionTaskDetail(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspection.queryInspectionTaskDetail',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取巡检项目列表
export function listInspectionItems() {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionItem.listInspectionItem',
      method: 'get',
      params: {
        page: 1,
        row: 100,
        communityId
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}