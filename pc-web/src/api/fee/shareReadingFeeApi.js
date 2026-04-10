import request from '@/utils/request'

/**
 * 获取公摊费用列表
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function listFloorShareFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meter.listFloorShareFee',
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

/**
 * 获取社区ID
 * @returns {Promise} Promise对象
 */
export function getCommunityId() {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.getCommunityId',
      method: 'get'
    }).then(response => {
      const res = response.data
      resolve(res.communityId)
    }).catch(error => {
      reject(error)
    })
  })
}