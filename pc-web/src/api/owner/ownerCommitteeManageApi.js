import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取业委会列表
export function listOwnerCommittee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerCommittee.listOwnerCommittee',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || '获取业委会列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除业委会成员
export function deleteOwnerCommittee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerCommittee.deleteOwnerCommittee',
      method: 'post',
      data: {
        ...data,
        communityId: data.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除业委会成员失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}