import request from '@/utils/request'

// 获取用户物品列表
export function listUserStorehouses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listUserStorehouses',
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

// 获取物品类型列表
export function listResourceStoreTypes(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStoreType.listResourceStoreTypes',
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

// 获取物品规格列表
export function listResourceStoreSpecifications(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listResourceStoreSpecifications',
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

// 导出数据
export function exportData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
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