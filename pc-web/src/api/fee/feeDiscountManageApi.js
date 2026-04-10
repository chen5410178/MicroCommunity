import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询折扣列表
export function queryFeeDiscount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeDiscount/queryFeeDiscount',
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

// 查询折扣规则
export function queryFeeDiscountRule(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeDiscount/queryFeeDiscountRule',
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

// 添加折扣
export function saveFeeDiscount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeDiscount/saveFeeDiscount',
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

// 更新折扣
export function updateFeeDiscount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeDiscount/updateFeeDiscount',
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

// 删除折扣
export function deleteFeeDiscount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeDiscount/deleteFeeDiscount',
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