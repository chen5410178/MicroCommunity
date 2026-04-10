import request from '@/utils/request'

// 获取项目列表
export function getHcAppList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/hcApp.listHcApp',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(response)
      } else {
        reject(new Error(res.msg || '获取项目列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加项目
export function addHcApp(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/hcApp.saveHcApp',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新项目
export function updateHcApp(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/hcApp.updateHcApp',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除项目
export function deleteHcApp(appId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/hcApp.deleteHcApp',
      method: 'post',
      data: { appId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
} 