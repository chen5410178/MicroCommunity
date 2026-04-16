import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取优惠券赠送记录列表
export function listCouponPropertyPoolDetail(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/couponProperty.listCouponPropertyPoolDetail',
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

// 退回优惠券
export function deleteCouponPropertyPoolDetail(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/couponProperty.deleteCouponPropertyPoolDetail',
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