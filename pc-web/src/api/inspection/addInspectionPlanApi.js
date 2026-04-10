import request from '@/utils/request'

// 获取组织树
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取组织树失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询员工信息
export function listStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取员工信息失败'))
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
        resolve(res.inspectionRoutes)
 
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存巡检计划
export function saveInspectionPlan(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionPlan.saveInspectionPlan',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存巡检计划失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}