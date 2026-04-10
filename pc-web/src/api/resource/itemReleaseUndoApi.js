import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询待办放行单列表
export function queryUndoItemRelease(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/itemRelease.queryUndoItemRelease',
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

// 查询放行物品列表
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