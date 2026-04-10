import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

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

// 保存业主投票
export function saveOwnerVote(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.saveOwnerVote',
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