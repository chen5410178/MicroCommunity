import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询楼栋公摊列表
export function listFloorShareMeter(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meter.listFloorShareMeter',
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

// 添加楼栋公摊
export function saveFloorShareMeter(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meter.saveFloorShareMeter',
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

// 修改楼栋公摊
export function updateFloorShareMeter(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meter.updateFloorShareMeter',
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

// 删除楼栋公摊
export function deleteFloorShareMeter(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meter.deleteFloorShareMeter',
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

// 查询楼栋列表
export function queryFloors(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloors',
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

// 查询费用项列表
export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.listFeeConfigs',
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