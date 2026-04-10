import request from '@/utils/request'

// 获取管理员小区列表
export function listAdminCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listAdminCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取小区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取公众号列表
export function listAdminSmallWeChats(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/smallWeChat.listAdminSmallWeChats',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取公众号列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加公众号
export function saveAdminSmallWeChat(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/smallWeChat.saveAdminSmallWeChat',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加公众号失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新公众号
export function updateAdminSmallWeChat(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/smallWeChat.updateAdminSmallWeChat',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新公众号失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}