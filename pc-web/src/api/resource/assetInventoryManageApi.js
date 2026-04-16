import request from '@/utils/request'

// 获取资产盘点列表
export function listAssetInventory(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/assetInventory.listAssetInventory',
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

// 删除资产盘点
export function deleteAssetInventory(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/assetInventory.deleteAssetInventory',
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

// 更新资产盘点
export function updateAssetInventory(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/assetInventory.updateAssetInventory',
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

// 导出资产盘点
export function exportAssetInventory(params) {
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