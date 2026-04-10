import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取业委会成员详情
export function getOwnerCommitteeDetail(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    const queryParams = {
      ...params,
      communityId
    }
    
    request({
      url: '/ownerCommittee.listOwnerCommittee',
      method: 'get',
      params: queryParams
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get owner committee details'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取业委会成员紧急联系人
export function listOwnerCommitteeContracts(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    const queryParams = {
      ...params,
      communityId
    }
    
    request({
      url: '/ownerCommittee.listOwnerCommitteeContract',
      method: 'get',
      params: queryParams
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get emergency contacts'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}