import request from '@/utils/request'

export function queryRoomsByOwner(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRoomsByOwner',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}