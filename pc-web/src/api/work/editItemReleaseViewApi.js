import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 更新放行信息
export function updateItemRelease(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/itemRelease.updateItemRelease',
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

// 获取放行列表
export function listItemRelease(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/itemRelease.listItemRelease',
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

// 获取放行物品列表
export function listItemReleaseRes(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/itemRelease.listItemReleaseRes',
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