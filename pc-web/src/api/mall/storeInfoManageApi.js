import request from '@/utils/request'

// 查询商户信息列表
export function queryStoreInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/storeInfo/queryStoreInfo',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || 'Failed to query store info'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询便民菜单列表
export function queryConvenienceMenus(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/convenienceMenus/queryConvenienceMenus',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || 'Failed to query convenience menus'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存商户信息
export function saveStoreInfo(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/storeInfo/saveStoreInfo',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to save store info'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新商户信息
export function updateStoreInfo(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/storeInfo/updateStoreInfo',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to update store info'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除商户信息
export function deleteStoreInfo(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/storeInfo/deleteStoreInfo',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to delete store info'))
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
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to upload file'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}