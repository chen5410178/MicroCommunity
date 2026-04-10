import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询房屋列表
export function queryRooms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRooms',
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

// 查询楼栋列表
export function getFloors(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloors',
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
export function getUnits(params) {
  return new Promise((resolve, reject) => {
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

export function queryRoomsWithSell(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRoomsWithSell',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询已售房屋信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询未售房屋信息
export function queryRoomsWithOutSell(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRoomsWithOutSell',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if(res.rooms){
        res.rooms.forEach(room => {
          room.roomName = room.floorNum+"-"+room.unitNum+"-"+room.roomNum
        });
      }
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询房间树形数据
export function queryRoomsTree(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/room.queryRoomsTree',
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