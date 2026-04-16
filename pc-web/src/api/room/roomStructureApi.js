import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

/**
 * 查询楼层和单元树数据
 * @returns {Promise}
 */
export function queryFloorAndUnits() {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloorAndUnits',
      method: 'get',
      params: {
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 查询房间结构列表
 * @param {Object} params 查询参数
 * @param {number} params.page 页码
 * @param {number} params.row 每页条数
 * @param {string} params.unitId 单元ID
 * @returns {Promise}
 */
export function listRoomStructure(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.listRoomStructure',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
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

/**
 * 获取房间状态字典
 * @returns {Promise}
 */
export function getRoomStateDict() {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.getDict',
      method: 'get',
      params: {
        dictType: 'room_state'
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}