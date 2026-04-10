import request from '@/utils/request'

// 获取项目列表
export function getAppList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/app.listApps',
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

// 添加项目
export function addApp(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/app.saveApp',
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

// 更新项目
export function updateApp(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/app.updateApp',
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

// 删除项目
export function deleteApp(appId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/app.deleteApp',
      method: 'post',
      data: { appId }
    }).then(response => {
      const res = response.data

        resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
} 