import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

/**
 * 查询押金费用列表
 * @param {Object} params 查询参数
 * @returns {Promise} 
 */
export function queryFeeDeposit(params) {
  return new Promise((resolve, reject) => {
    // 确保社区ID存在
    const communityId = params.communityId || getCommunityId()
    if (!communityId) {
      reject(new Error('Community ID is required'))
      return
    }

    request({
      url: '/fee.queryFeeDeposit',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total,
        records: res.records
      })
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 保存退费申请
 * @param {Object} data 退费数据
 * @returns {Promise}
 */
export function saveReturnPayFee(data) {
  return new Promise((resolve, reject) => {
    // 确保社区ID存在
    const communityId = data.communityId || getCommunityId()
    if (!communityId) {
      reject(new Error('Community ID is required'))
      return
    }

    request({
      url: '/returnPayFee.saveReturnPayFee',
      method: 'post',
      data: {
        ...data,
        communityId
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
