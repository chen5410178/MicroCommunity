import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询押金报表
export function queryPayFeeDeposit(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/reportFeeMonthStatistics/queryPayFeeDeposit',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出押金报表数据
export function exportData(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/export.exportData',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询单元列表
export function queryUnits(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/unit.queryUnits',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}