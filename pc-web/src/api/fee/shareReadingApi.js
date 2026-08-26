import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取公摊抄表列表
export function listFloorShareReading(params) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      communityId: getCommunityId(),
      page: 1,
      row: 10
    }
    request({
      url: '/meter.listFloorShareReading',
      method: 'get',
      params: { ...defaultParams, ...params }
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

// 获取公摊表列表
export function listFloorShareMeter(params) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      communityId: getCommunityId(),
      page: 1,
      row: 300
    }
    request({
      url: '/meter.listFloorShareMeter',
      method: 'get',
      params: { ...defaultParams, ...params }
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

// 添加公摊抄表
export function saveFloorShareReading(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meter.saveFloorShareReading',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 审核公摊抄表
export function auditFloorShareReading(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meter.auditFloorShareReading',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除公摊抄表
export function deleteFloorShareReading(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meter.deleteFloorShareReading',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}