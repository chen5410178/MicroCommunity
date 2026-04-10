import request from '@/utils/request'

// 获取服务注册列表
export function getRouteList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/serviceRegister.listServiceRegisters',
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

// 添加服务注册
export function addRoute(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/service.bindingService',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
        resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}

// 更新服务注册
export function updateRoute(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/route.updateRoute',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
        resolve(res)
  
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除服务注册
export function deleteRoute(id) {
  return new Promise((resolve, reject) => {
    request({
      url: '/serviceRegister.deleteServiceRegister',
      method: 'post',
      data: { id }
    }).then(response => {
      const res = response.data
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}
