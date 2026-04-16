import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询已办放行单列表
export function queryFinishItemRelease(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/itemRelease.queryFinishItemRelease',
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

// 查询放行物品列表
export function listItemReleaseRes(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/itemRelease.listItemReleaseRes',
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