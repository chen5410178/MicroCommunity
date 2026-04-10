import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询费用折扣列表
export function getPayFeeConfigDiscountList(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/payFeeConfigDiscount/queryPayFeeConfigDiscount',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || '获取费用折扣列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询折扣列表
export function getFeeDiscountList(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feeDiscount/queryFeeDiscount',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || '获取折扣列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存费用折扣
export function savePayFeeConfigDiscount(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/payFeeConfigDiscount/savePayFeeConfigDiscount',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存费用折扣失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除费用折扣
export function deletePayFeeConfigDiscount(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/payFeeConfigDiscount/deletePayFeeConfigDiscount',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除费用折扣失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}