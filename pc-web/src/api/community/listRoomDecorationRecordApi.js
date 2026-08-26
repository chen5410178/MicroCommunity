import request from '@/utils/request'

// 查询装修跟踪记录列表
export function queryRoomRenovationRecord(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roomRenovation/queryRoomRenovationRecord',
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

// 更新装修跟踪记录
export function updateRoomDecorationRecord(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roomRenovation/updateRoomDecorationRecord',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除装修跟踪记录
export function deleteRoomRenovationRecord(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roomRenovation/deleteRoomRenovationRecord',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传图片
export function uploadImage(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadImage',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传视频
export function uploadVedio(data, config) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadVedio',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      ...config
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}