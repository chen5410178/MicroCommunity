import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取放行类型列表
export function listItemReleaseType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/itemRelease.listItemReleaseType',
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

// 添加放行类型
export function saveItemReleaseType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/itemRelease.saveItemReleaseType',
      method: 'post',
      data: {
        ...data,
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

// 修改放行类型
export function updateItemReleaseType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/itemRelease.updateItemReleaseType',
      method: 'post',
      data: {
        ...data,
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

// 删除放行类型
export function deleteItemReleaseType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/itemRelease.deleteItemReleaseType',
      method: 'post',
      data: {
        ...data,
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

// 部署流程
export function deployModel(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow/deployModel',
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