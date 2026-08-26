import request from '@/utils/request'

// 获取服务提供列表
export function listServiceProvides(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/serviceProvide.listServiceProvides',
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

// 更新服务提供
export function updateServiceProvide(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/serviceProvide.updateServiceProvide',
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

export function addServiceProvide(data) {
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

// 获取服务提供详情
export function getServiceProvideDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/serviceProvide.getServiceProvide',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res.data)
    }).catch(error => {
      reject(error)
    })
  })
}
export function deleteServiceProvide(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/serviceProvide.deleteServiceProvide',
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