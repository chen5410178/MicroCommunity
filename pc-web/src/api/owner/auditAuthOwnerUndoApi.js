import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取房间信息
export function getRoomInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRooms',
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

// 获取业主列表
export function getOwnerList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryOwners',
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

// 提交审核
export function auditAuthOwner(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.auditAuthOwner',
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