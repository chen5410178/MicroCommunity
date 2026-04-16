import request from '@/utils/request'

// 获取盘点信息列表
export function getAssetInventoryList(params) {
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

// 获取盘点商品详情列表
export function listAssetInventoryWholeDetail(params) {
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