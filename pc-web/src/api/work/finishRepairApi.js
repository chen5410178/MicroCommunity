import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取资源类型列表
export function listResourceStoreTypes(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/resourceStoreType.listResourceStoreTypes',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取用户仓库商品列表
export function listUserStorehouses(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/resourceStore.listUserStorehouses',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 完成报修
export function repairFinish(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/ownerRepair.repairFinish',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传文件
export function uploadFile(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    const formData = new FormData()
    formData.append('uploadFile', data.file)
    formData.append('communityId', communityId)
    
    request({
      url: '/uploadFile',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}