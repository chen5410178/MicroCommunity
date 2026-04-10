import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

/**
 * 查询房屋装修记录详情
 * @param {Object} params 查询参数
 * @param {string} params.recordId 记录ID
 * @param {string} params.roomName 房间名称
 * @param {string} params.state 状态
 * @param {string} params.roomId 房间ID
 * @param {number} params.page 页码
 * @param {number} params.row 每页条数
 * @returns {Promise} 返回Promise对象
 */
export function queryRoomRenovationRecordDetail(params) {
  return new Promise((resolve, reject) => {
    // 确保有communityId
    const queryParams = {
      ...params,
      communityId: params.communityId || getCommunityId()
    }

    request({
      url: '/roomRenovation/queryRoomRenovationRecordDetail',
      method: 'get',
      params: queryParams
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