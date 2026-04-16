import request from '@/utils/request'

// 获取管理员进出记录列表
export function listAdminAccessControlInout(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.listAdminAccessControlInout',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get admin access control inout list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取管理员门禁列表
export function listAdminAccessControl(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.listAdminAccessControl',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get admin access control list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取管理员小区列表
export function listAdminCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listAdminCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get admin community list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}