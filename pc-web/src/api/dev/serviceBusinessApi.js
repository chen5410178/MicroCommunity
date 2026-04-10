import request from '@/utils/request'

// 获取服务实现列表
export function getServiceBusinessList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/serviceImpl.listServiceImpls',
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

// 添加服务实现
export function addServiceBusiness(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/serviceImpl.saveServiceImpl',
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

// 更新服务实现
export function updateServiceBusiness(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/serviceImpl.updateServiceImpl',
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

// 删除服务实现
export function deleteServiceBusiness(serviceBusinessId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/serviceImpl.deleteServiceImpl',
      method: 'post',
      data: { serviceBusinessId }
    }).then(response => {
      const res = response.data

        resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}
