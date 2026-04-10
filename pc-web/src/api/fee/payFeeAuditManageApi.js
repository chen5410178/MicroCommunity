import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询缴费审核列表
export function getPayFeeAuditList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeAudit/queryPayFeeAudit',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存缴费审核
export function savePayFeeAudit(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeAudit/savePayFeeAudit',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存退费申请
export function saveReturnPayFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/returnPayFee.saveReturnPayFee',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}