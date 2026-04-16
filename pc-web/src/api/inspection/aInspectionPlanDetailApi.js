import request from '@/utils/request'

// 获取巡检计划详情
export function getInspectionPlanDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionPlan.listAdminInspectionPlans',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取巡检计划详情失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取巡检人员列表
export function listAdminInspectionPlanStaffs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspection.listAdminInspectionPlanStaffs',
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

// 获取巡检路线列表
export function listAdminInspectionRoutes(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.listAdminInspectionRoutes',
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

// 获取巡检路线点列表
export function listAdminInspectionRoutePoints(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionRoute.listAdminInspectionRoutePoints',
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

// 获取巡检任务列表
export function queryAdminRouteInspectionTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspection.queryAdminRouteInspectionTask',
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

// 获取巡检任务详情
export function listAdminInspectionTaskDetails(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionTaskDetail.listAdminInspectionTaskDetails',
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