import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取费用套餐成员列表
export function listFeeComboMember(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeComboMember.listFeeComboMember',
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

// 添加费用套餐成员
export function saveFeeComboMember(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeComboMember.saveFeeComboMember',
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

// 删除费用套餐成员
export function deleteFeeComboMember(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeComboMember.deleteFeeComboMember',
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

// 获取费用配置列表
export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.listFeeConfigs',
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