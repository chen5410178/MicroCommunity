import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询商铺列表
export function queryShops(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/room.queryRooms',
      method: 'get',
      params: {
        ...params,
        communityId,
        roomType: '2020602'
      }
    }).then(response => {
      const res = response.data
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加商铺
export function saveShops(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/room.saveShops',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to add shop'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新商铺
export function updateShops(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/room.updateRoom',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to update shop'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 绑定业主商铺
export function saveOwnerShops(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/room.saveOwnerShops',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to bind owner'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除商铺
export function deleteShops(roomId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.deleteRoom',
      method: 'post',
      data: {
        roomId,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to delete shop'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}