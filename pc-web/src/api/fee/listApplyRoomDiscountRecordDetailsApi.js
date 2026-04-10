import request from '@/utils/request'

/**
 * 查询房间折扣记录详情
 * @param {Object} params 查询参数
 * @param {string} params.ardrId 折扣记录ID
 * @param {string} params.roomName 房间名称
 * @param {string} params.state 状态
 * @param {string} params.roomId 房间ID
 * @param {string} params.communityId 小区ID
 * @param {number} params.page 当前页码
 * @param {number} params.row 每页条数
 * @returns {Promise} 返回Promise对象
 */
export function queryApplyRoomDiscountRecordDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscountRecord/queryApplyRoomDiscountRecordDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}