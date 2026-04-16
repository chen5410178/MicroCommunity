import request from '@/utils/request'

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

// 添加班次
export function saveClasses(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/classes.saveClasses',
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

// 更新班次
export function updateClasses(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/classes.updateClasses',
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

// 删除班次
export function deleteClasses(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/classes.deleteClasses',
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

// 更新班次状态
export function updateClassesState(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/classes.updateClassesState',
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