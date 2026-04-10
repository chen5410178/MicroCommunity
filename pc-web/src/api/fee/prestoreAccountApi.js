import request from '@/utils/request'

export function ownerPrestoreAccount(data) {
  return request({
    url: '/account.ownerPrestoreAccount',
    method: 'post',
    data
  })
}

export function queryOwners(params) {
  return request({
    url: '/owner.queryOwners',
    method: 'get',
    params
  })
}

export function queryRoomsByOwner(params) {
  return request({
    url: '/room.queryRoomsByOwner',
    method: 'get',
    params
  })
}