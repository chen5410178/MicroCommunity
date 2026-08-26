import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 业主退房
export function ownerExitRoom(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.ownerExitRoom',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询业主房屋
export function queryRoomsByOwner(params) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      communityId: getCommunityId(),
      page: 1,
      row: 100
    }
    request({
      url: '/room.queryRoomsByOwner',
      method: 'get',
      params: { ...defaultParams, ...params }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询费用列表
export function listFee(params) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      communityId: getCommunityId(),
      page: 1,
      row: 500
    }
    request({
      url: '/fee.listFee',
      method: 'get',
      params: { ...defaultParams, ...params }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询费用配置
export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      communityId: getCommunityId(),
      page: 1,
      row: 500
    }
    request({
      url: '/feeConfig.listFeeConfigs',
      method: 'get',
      params: { ...defaultParams, ...params }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}