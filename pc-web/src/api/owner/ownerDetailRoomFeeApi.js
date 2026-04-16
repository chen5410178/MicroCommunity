import request from '@/utils/request'

export function listRoomsByOwner(params) {
  return new Promise((resolve, reject) => {
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