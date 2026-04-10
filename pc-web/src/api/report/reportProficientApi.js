import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

/**
 * 查询费用台账数据
 * @param {Object} params 查询参数
 * @returns {Promise} 
 */
export function queryReportFeeYear(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/reportFeeYearCollection/queryReportFeeYear',
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

/**
 * 查询费用详情
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function queryFeeDetail(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/fee.queryFeeDetail',
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

/**
 * 获取收费项列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getFeeConfigList(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feeConfig.listFeeConfigs',
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

/**
 * 导出报表数据
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportReportFee(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/export.exportData',
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