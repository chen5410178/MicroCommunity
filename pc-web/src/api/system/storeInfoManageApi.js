import request from '@/utils/request'

// 获取商户列表
export function listStores(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/store.listStores',
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

// 更新商户信息
export function updateStoreInfo(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/update.store.info',
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

// 更新商户属性
export function updateStoreAttr(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/storeAttr.updateStoreAttr',
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