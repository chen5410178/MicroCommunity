import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取业主认证列表
export function listAuditAppUserBindingOwners(params) {
  return new Promise((resolve, reject) => {
    // 确保有社区ID
    if (!params.communityId) {
      params.communityId = getCommunityId()
    }
    
    request({
      url: '/owner.listAuditAppUserBindingOwners',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取业主认证列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除业主绑定
export function deleteAppUserBindingOwner(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.deleteAppUserBindingOwner',
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
        reject(new Error(res.msg || '删除业主绑定失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 重置密码
export function resetStaffPwd(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.resetStaffPwd',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '重置密码失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}