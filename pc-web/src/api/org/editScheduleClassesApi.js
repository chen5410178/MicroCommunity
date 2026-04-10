import request from '@/utils/request'

// 更新排班信息
export function updateScheduleClasses(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.updateScheduleClasses',
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

// 获取排班列表
export function listScheduleClasses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.listScheduleClasses',
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

// 获取排班日信息
export function listScheduleClassesDay(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.listScheduleClassesDay',
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

// 获取班次列表
export function listClasses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/classes.listClasses',
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