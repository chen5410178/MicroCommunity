import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取金币账户信息
export function getPropertyGoldInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.getMallOpenApi',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId(),
        mallApiCode: 'queryPropertyGoldBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取金币交易明细
export function getPropertyGoldDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.getMallOpenApi',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId(),
        mallApiCode: 'queryPropertyGoldDetailBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取金币提现申请列表
export function getPropertyGoldApplys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.getMallOpenApi',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId(),
        mallApiCode: 'queryPropertyGoldApplysBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 申请金币提现
export function applyGoldWithHold(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.postMallOpenApi',
      method: 'post',
      data: {
        ...data,
        communityId: data.communityId || getCommunityId(),
        mallApiCode: 'applyGoldWithHoldBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除金币提现申请
export function deleteGoldWithHold(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.postMallOpenApi',
      method: 'post',
      data: {
        ...data,
        communityId: data.communityId || getCommunityId(),
        mallApiCode: 'deleteGoldWithHoldBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}