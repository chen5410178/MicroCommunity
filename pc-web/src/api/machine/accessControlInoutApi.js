import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取门禁进出记录列表
export function listAccessControlInouts(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
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

// 获取门禁设备列表
export function listAccessControls(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data
      })
    }).catch(error => {
      reject(error)
    })
  })
}