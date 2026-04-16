import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取报修单详情
export function listOwnerRepairs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.listOwnerRepairs',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取维修人员处理轨迹
export function listRepairStaffs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.listRepairStaffs',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}