import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取费用套餐列表
export function listFeeCombo(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feeCombo.listFeeCombo',
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

// 获取费用套餐成员列表
export function listFeeComboMember(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeComboMember.listFeeComboMember',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 根据费用套餐创建费用
export function createFeeByCombo(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/fee.createFeeByCombo',
      method: 'post',
      data: {
        ...data,
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