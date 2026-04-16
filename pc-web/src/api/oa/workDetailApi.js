import request from '@/utils/request'

// 获取工作单详情
export function getWorkPoolDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listWorkPool',
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

// 获取工作单任务列表
export function listWorkTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listWorkTask',
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

// 获取工作单任务项列表
export function listWorkTaskItem(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/task.listWorkTaskItem',
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

// 获取工作单抄送人列表
export function listWorkCopy(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listWorkCopy',
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

// 获取工作单流转事件列表
export function listWorkEvent(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workEvent.listWorkEvent',
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

// 获取工作单流转附件列表
export function listWorkPoolFile(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listWorkPoolFile',
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

// 获取工作单类型列表
export function listWorkType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workType.listWorkType',
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

// 获取工作单周期列表
export function listWorkCycle(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workCycle.listWorkCycle',
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