import request from '@/utils/request'

// 获取订单列表
export function listCorders(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/corders.listCorders',
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

// 获取应用列表
export function listPublicApps(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/app.listPublicApps',
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

// 获取订单日志
export function listUnitemLog(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/corders.listUnitemLog',
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