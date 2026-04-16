import request from '@/utils/request'

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

// 添加工作单类型
export function saveWorkType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workType.saveWorkType',
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

// 更新工作单类型
export function updateWorkType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workType.updateWorkType',
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

// 删除工作单类型
export function deleteWorkType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workType.deleteWorkType',
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