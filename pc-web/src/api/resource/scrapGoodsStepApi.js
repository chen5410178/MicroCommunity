import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取用户仓库物品列表
export function listUserStorehouses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listUserStorehouses',
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

// 保存物品分配
export function saveAllocationUserStorehouse(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.saveAllocationUserStorehouse',
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