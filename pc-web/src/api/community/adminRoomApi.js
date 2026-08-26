import request from '@/utils/request'

// 查询小区房屋列表
export function queryAdminRoom(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryAdminRoom',
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

// 查询小区单元树
export function queryCommunityUnitTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.queryCommunityUnitTree',
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

// 获取房屋状态字典
export function getRoomStates() {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/core/list',
      method: 'get',
      params: {
        type: 'state',
        name: 'building_room'
      }
    }).then(response => {
      const res = response.data
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取房屋类型字典
export function getRoomSubTypes() {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/core/list',
      method: 'get',
      params: {
        name: 'building_room',
        type: 'room_sub_type'
      }
    }).then(response => {
      const res = response.data
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}