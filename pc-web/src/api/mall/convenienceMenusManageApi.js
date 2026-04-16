import request from '@/utils/request'

// 获取便民服务菜单列表
export function getConvenienceMenusList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/convenienceMenus/queryConvenienceMenus',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || '获取便民服务菜单列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加便民服务菜单
export function addConvenienceMenus(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/convenienceMenus/saveConvenienceMenus',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加便民服务菜单失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除便民服务菜单
export function deleteConvenienceMenus(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/convenienceMenus/deleteConvenienceMenus',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除便民服务菜单失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传文件
export function uploadFile(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadFile',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '上传文件失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}