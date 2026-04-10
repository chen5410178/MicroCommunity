import request from '@/utils/request'

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

// 获取用户可操作的物品列表
export function listUserStorehouses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listUserStorehouses',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total,
        records: res.records
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 提交退还申请
export function saveAllocationStorehouse(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.saveAllocationStorehouse',
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

// 获取当前社区ID
export function getCommunityId() {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.getCommunityId',
      method: 'get'
    }).then(response => {
      const res = response.data
      resolve(res.data || '')
    }).catch(error => {
      reject(error)
    })
  })
}