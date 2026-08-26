import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

/**
 * 查询优惠类型列表
 * @param {Object} params 查询参数
 * @returns {Promise} 
 */
export function queryApplyRoomDiscountType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscount/queryApplyRoomDiscountType',
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

/**
 * 添加优惠类型
 * @param {Object} data 优惠类型数据
 * @returns {Promise}
 */
export function saveApplyRoomDiscountType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscount/saveApplyRoomDiscountType',
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
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 更新优惠类型
 * @param {Object} data 优惠类型数据
 * @returns {Promise}
 */
export function updateApplyRoomDiscountType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscount/updateApplyRoomDiscountType',
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
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 删除优惠类型
 * @param {Object} data 删除参数
 * @returns {Promise}
 */
export function deleteApplyRoomDiscountType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscount/deleteApplyRoomDiscountType',
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
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}