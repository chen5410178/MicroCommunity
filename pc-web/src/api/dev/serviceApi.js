import request from '@/utils/request'

// 获取服务列表
export function getServiceList(params) {
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

// 添加服务
export function addService(data) {
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

// 更新服务
export function updateService(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/service.updateService',
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

// 删除服务
export function deleteService(serviceId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/service.deleteService',
      method: 'post',
      data: { serviceId }
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
