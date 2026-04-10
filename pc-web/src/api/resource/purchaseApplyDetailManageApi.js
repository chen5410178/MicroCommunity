import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取采购申请明细列表
export function listPurchaseApplyDetails(params) {
  return new Promise((resolve, reject) => {
    params.params.communityId = getCommunityId()
    request({
      url: '/purchaseApplyDetail.listPurchaseApplyDetails',
      method: 'get',
      params: params.params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.purchaseApplyDetails,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取物品类型列表
export function listResourceStoreTypes(params) {
  return new Promise((resolve, reject) => {
    params.params.communityId = getCommunityId()
    request({
      url: '/resourceStoreType.listResourceStoreTypes',
      method: 'get',
      params: params.params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取物品规格列表
export function listResourceStoreSpecifications(params) {
  return new Promise((resolve, reject) => {
    params.params.communityId = getCommunityId()
    request({
      url: '/resourceStore.listResourceStoreSpecifications',
      method: 'get',
      params: params.params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取供应商列表
export function listResourceSuppliers(params) {
  return new Promise((resolve, reject) => {
    params.params.communityId = getCommunityId()
    request({
      url: '/resourceSupplier.listResourceSuppliers',
      method: 'get',
      params: params.params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取仓库列表
export function listStorehouses(params) {
  return new Promise((resolve, reject) => {
    params.params.communityId = getCommunityId()
    request({
      url: '/resourceStore.listStorehouses',
      method: 'get',
      params: params.params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出数据
export function exportData(params) {
  return new Promise((resolve, reject) => {
    params.params.communityId = getCommunityId()
    request({
      url: '/export.exportData',
      method: 'get',
      params: params.params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}