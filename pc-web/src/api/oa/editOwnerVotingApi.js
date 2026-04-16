import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取投票详情
export function listOwnerVote(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.listOwnerVote',
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

// 更新投票信息
export function updateOwnerVote(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.updateOwnerVote',
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

// 查询楼栋和单元信息
export function queryFloorAndUnits(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloorAndUnits',
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

// 查询房屋信息
export function queryRooms(params) {
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