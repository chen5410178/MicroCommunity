import request from '@/utils/request'

// 查询管理员小区列表
export function listAdminCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listAdminCommunitys',
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

// 查询小程序列表
export function listAdminSmallWeChats(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/smallWeChat.listAdminSmallWeChats',
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

// 添加小程序
export function saveAdminSmallWeChat(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/smallWeChat.saveAdminSmallWeChat',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新小程序
export function updateAdminSmallWeChat(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/smallWeChat.updateAdminSmallWeChat',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}