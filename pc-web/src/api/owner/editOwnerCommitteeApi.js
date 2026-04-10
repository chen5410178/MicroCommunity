import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取业委会详情
export function getOwnerCommitteeDetail(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/ownerCommittee.listOwnerCommittee',
      method: 'get',
      params: {
        ...params,
        communityId,
        page: 1,
        row: 1
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0 && res.data && res.data.length > 0) {
        resolve({ data: res.data[0] })
      } else {
        reject(new Error(res.msg || 'Failed to get owner committee details'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新业委会信息
export function updateOwnerCommittee(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/ownerCommittee.updateOwnerCommittee',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to update owner committee'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取业委会紧急联系人列表
export function listOwnerCommitteeContract(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/ownerCommittee.listOwnerCommitteeContract',
      method: 'get',
      params: {
        ...params,
        communityId,
        page: 1,
        row: 100
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({ 
          data: res.data.map(item => ({
            ...item,
            id: item.contractId
          }))
        })
      } else {
        reject(new Error(res.msg || 'Failed to get emergency contacts'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}