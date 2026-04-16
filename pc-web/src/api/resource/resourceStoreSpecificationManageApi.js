import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询规格列表
export function listResourceStoreSpecifications(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/resourceStore.listResourceStoreSpecifications',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Query specification list failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加规格
export function saveResourceStoreSpecification(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/resourceStore.saveResourceStoreSpecification',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Add specification failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新规格
export function updateResourceStoreSpecification(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/resourceStore.updateResourceStoreSpecification',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Update specification failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除规格
export function deleteResourceStoreSpecification(rssId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.deleteResourceStoreSpecification',
      method: 'post',
      data: { 
        rssId,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Delete specification failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询物品类型列表
export function listResourceStoreTypes(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/resourceStoreType.listResourceStoreTypes',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Query item types failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}