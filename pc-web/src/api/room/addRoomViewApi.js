import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 保存房屋信息
export function saveRoom(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/room.saveRoom',
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

// 查询楼栋信息
export function queryFloors(params) {
  return new Promise((resolve, reject) => {
    params.communityId = params.communityId || getCommunityId()
    request({
      url: '/floor.queryFloors',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询单元信息
export function queryUnits(params) {
  return new Promise((resolve, reject) => {
    params.communityId = params.communityId || getCommunityId()
    request({
      url: '/unit.queryUnits',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询业主信息
export function queryOwners(params) {
  return new Promise((resolve, reject) => {
    params.communityId = params.communityId || getCommunityId()
    request({
      url: '/owner.queryOwners',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}