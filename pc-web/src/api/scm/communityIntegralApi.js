import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询小区积分信息
export function queryCommunityIntegral(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.getMallOpenApi',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId(),
        mallApiCode: 'queryCommunityIntegralBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询小区积分明细
export function queryCommunityIntegralDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.getMallOpenApi',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId(),
        mallApiCode: 'queryCommunityIntegralDetailBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询积分提现申请
export function queryCommunityIntegralApply(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.getMallOpenApi',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId(),
        mallApiCode: 'queryCommunityIntegralApplyBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 申请积分提现
export function applyIntegralWithdrawal(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.postMallOpenApi',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId(),
        mallApiCode: 'applyIntegralWithdrawalBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除积分提现申请
export function deleteIntegralWithdrawal(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.postMallOpenApi',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId(),
        mallApiCode: 'deleteIntegralWithdrawalBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}