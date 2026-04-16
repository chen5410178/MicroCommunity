import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取物品详情
export function getResourceStoreList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listResourceStores',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取采购记录
export function getPurchaseApplyDetailList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/purchaseApplyDetail.listPurchaseApplyDetails',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取盘点记录
export function getAssetInventoryList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/assetInventory.listAssetInventory',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取调拨记录
export function getAllocationStorehouseList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listAllocationStorehouses',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取转赠记录
export function getAllocationUserStorehouseList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listAllocationUserStorehouses',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取使用记录
export function getResourceStoreUseRecordList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listResourceStoreUseRecords',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}