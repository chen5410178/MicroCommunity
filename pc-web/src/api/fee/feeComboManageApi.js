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

// 添加费用套餐
export function saveFeeCombo(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feeCombo.saveFeeCombo',
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

// 更新费用套餐
export function updateFeeCombo(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feeCombo.updateFeeCombo',
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

// 删除费用套餐
export function deleteFeeCombo(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feeCombo.deleteFeeCombo',
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

// 获取费用套餐详情
export function getFeeComboDetail(comboId) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feeCombo.getFeeComboDetail',
      method: 'get',
      params: {
        comboId,
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