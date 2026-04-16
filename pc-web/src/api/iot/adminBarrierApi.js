import request from '@/utils/request'

// 获取道闸设备列表
export function listAdminBarrier(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.listAdminBarrier',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取道闸设备列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取管理小区列表
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
        reject(new Error(res.msg || '获取管理小区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}