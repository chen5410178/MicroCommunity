import request from '@/utils/request'

// 获取巡检任务明细列表
export function listInspectionTaskDetails(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionTaskDetail.listInspectionTaskDetails',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取巡检任务明细列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出巡检任务明细
export function exportInspectionTaskDetails(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '导出失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取巡检计划列表
export function listInspectionPlans(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionPlan.listInspectionPlans',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取巡检计划列表失败'))
      }
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
      params
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
      params
    }).then(response => {
      const res = response.data
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}