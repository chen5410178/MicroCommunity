import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取停车场区域列表
export function listParkingAreas(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/parkingArea.listParkingAreas',
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

// 获取进出场记录列表
export function listCarInouts(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取进出场记录失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}