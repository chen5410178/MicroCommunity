import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询未收费房屋列表
export function queryNoFeeRooms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics/queryNoFeeRooms',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询楼栋列表
export function queryFloors(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloors',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res.apiFloorDataVoList || [])
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询单元列表
export function queryUnits(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/unit.queryUnits',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res || [])
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询房屋列表
export function queryRooms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRooms',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res || { rooms: [] })
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出数据
export function exportData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}