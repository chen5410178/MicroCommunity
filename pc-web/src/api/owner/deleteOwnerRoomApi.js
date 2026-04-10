import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询业主信息
export function queryOwners(params) {
  return new Promise((resolve, reject) => {
    // 确保包含社区ID
    if (!params.communityId) {
      params.communityId = getCommunityId()
    }
    
    request({
      url: '/owner.queryOwners',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || '查询业主信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除业主房屋
export function deleteOwnerRoom(data) {
  return new Promise((resolve, reject) => {
    // 确保包含社区ID
    if (!data.communityId) {
      data.communityId = getCommunityId()
    }
    
    request({
      url: '/owner.deleteOwnerRoom',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除业主房屋失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}