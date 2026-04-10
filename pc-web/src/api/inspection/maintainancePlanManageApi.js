import request from '@/utils/request'

// 获取保养计划列表
export function listMaintainancePlan(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainancePlan.listMaintainancePlan',
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

// 删除保养计划
export function deleteMaintainancePlan(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainancePlan.deleteMaintainancePlan',
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

// 更新保养计划状态
export function updateMaintainancePlanState(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainancePlan.updateMaintainancePlanState',
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