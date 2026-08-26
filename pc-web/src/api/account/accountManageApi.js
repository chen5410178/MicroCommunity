import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询账户列表
export function queryCommunityOwnerAccount(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/account.queryCommunityOwnerAccount',
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

// 预存账户
export function ownerPrestoreAccount(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/account.ownerPrestoreAccount',
      method: 'post',
      data
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

// 删除账户
export function deleteAccount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/account.deleteAccount',
      method: 'post',
      data
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

// 查询业主列表
export function queryOwners(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    params.ownerTypeCd = '1001'
    request({
      url: '/owner.queryOwners',
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

// 查询业主房间
export function queryRoomsByOwner(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/room.queryRoomsByOwner',
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

