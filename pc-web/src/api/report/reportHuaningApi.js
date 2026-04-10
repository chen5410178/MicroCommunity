import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询未收情况表数据
export function queryHuaningOweFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics/queryHuaningOweFee',
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

// 查询未收明细表数据
export function queryHuaningOweFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics/queryHuaningOweFeeDetail',
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

// 查询当月收费情况表数据
export function queryHuaningPayFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics/queryHuaningPayFee',
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

// 查询费用明细
export function queryFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryFeeDetail',
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

// 查询楼栋列表
export function listFloors(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloors',
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