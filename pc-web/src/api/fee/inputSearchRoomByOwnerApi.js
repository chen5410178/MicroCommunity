import request from '@/utils/request'

export function queryRoomsByOwner(params) {
  return request({
    url: '/room.queryRoomsByOwner',
    method: 'get',
    params
  })
}