import request from '@/utils/request'

// 获取服务列表
export function listServices(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/service.listServices',
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

// 保存服务
export function saveService(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/service.saveService',
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

// 保存服务提供
export function saveServiceProvide(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/serviceProvide.saveServiceProvide',
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