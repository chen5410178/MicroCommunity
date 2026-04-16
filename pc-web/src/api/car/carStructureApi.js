import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询楼栋和单元树形数据
export function queryFloorAndUnits(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/floor.queryFloorAndUnits',
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

// 查询车位结构列表
export function listCarStructure(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/car.listCarStructure',
      method: 'get',
      params: {
        ...params,
        communityId
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

// 获取当前小区ID
export function getCurrentCommunityId() {
  return getCommunityId()
}