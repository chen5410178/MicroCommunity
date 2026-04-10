import request from '@/utils/request'

// 获取调拨申请列表
export function listAllocationStorehouseApply(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listAllocationStorehouseApplys',
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

// 获取调拨物品列表
export function listAllocationStorehouses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listAllocationStorehouses',
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

// 获取仓库列表
export function listStorehouses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listStorehouses',
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

// 获取物品列表
export function listResourceStores(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listResourceStores',
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

// 更新调拨申请
export function updateAllocationStorehouse(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.updateAllocationStorehouse',
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