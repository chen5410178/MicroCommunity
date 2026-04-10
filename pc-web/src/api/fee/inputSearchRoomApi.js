import request from '@/utils/request'

export function queryRooms(params) {
  return request({
    url: '/room.queryRooms',
    method: 'get',
    params
  })
}