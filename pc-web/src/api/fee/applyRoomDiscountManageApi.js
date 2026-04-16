import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询优惠申请列表
export function queryApplyRoomDiscount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscount/queryApplyRoomDiscount',
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



// 查询优惠申请列表
export function listRoomsWhereFeeSet(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listRoomsWhereFeeSet',
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

// 查询优惠申请类型
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

// 查询费用折扣
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

// 保存优惠申请
export function saveApplyRoomDiscount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscount/saveApplyRoomDiscount',
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

// 更新优惠申请
export function updateApplyRoomDiscount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscount/updateApplyRoomDiscount',
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

// 更新审核优惠申请
export function updateReviewApplyRoomDiscount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscount/updateReviewApplyRoomDiscount',
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

// 删除优惠申请
export function deleteApplyRoomDiscount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscount/deleteApplyRoomDiscount',
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

// 处理返还金额计算
export function dealWithRefundAmount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscount/dealWithRefundAmount',
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