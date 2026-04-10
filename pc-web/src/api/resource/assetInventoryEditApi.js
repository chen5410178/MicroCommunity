import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取盘点详情
export function getAssetInventoryDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/assetInventory.listAssetInventory',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取盘点商品列表
export function listAssetInventoryWholeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/assetInventoryDetail.listAssetInventoryWholeDetail',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新盘点信息
export function updateAssetInventory(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/assetInventory.updateAssetInventory',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
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
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
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
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
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
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}