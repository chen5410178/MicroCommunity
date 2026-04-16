import request from '@/utils/request'

// 获取盘点详情
export function getAssetInventoryDetail(params) {
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

// 获取盘点商品详情
export function getAssetInventoryProduct(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/assetInventoryDetail.listAssetInventoryWholeDetail',
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

// 更新盘点状态
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