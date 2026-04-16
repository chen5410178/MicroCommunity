import request from '@/utils/request'

// 获取巡检任务详情列表
export function getAdminInspectionTaskDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionTask.listAdminInspectionTasks',
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

// 获取巡检点任务详情列表
export function listAdminInspectionTaskDetails(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionTaskDetail.listAdminInspectionTaskDetails',
      method: 'get',
      params
    }).then(response => {
      const res = response.data

      resolve({
        data: res.inspectionTaskDetails,
        records: res.records,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询巡检任务地图数据
export function queryAdminInspectionTaskDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspection.queryAdminInspectionTaskDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data

      resolve({
        data: res.data
      })

    }).catch(error => {
      reject(error)
    })
  })
}

// 查询巡检路线计划
export function queryAdminRouteInspectionPlan(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspection.queryAdminRouteInspectionPlan',
      method: 'get',
      params
    }).then(response => {
      const res = response.data

      resolve({
        data: res.data,
        records: res.records,
        total: res.total
      })
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

        resolve({
          data: res.inspectionRoutes
        })
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

        resolve({
          data: res.inspectionPoints,
          records: res.records,
          total: res.total
        })
    }).catch(error => {
      reject(error)
    })
  })
}