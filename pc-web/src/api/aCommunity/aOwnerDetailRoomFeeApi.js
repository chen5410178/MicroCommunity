import request from '@/utils/request'

export function listAdminFee(params) {
    return new Promise((resolve, reject) => {
    return request({
    url: '/fee.listAdminFee',
      method: 'get',
      params
    }).then(res => {
      resolve(res.data)
    }).catch(err => {
      reject(err)
    })
  })
}

export function queryAdminOwnerRooms(params) {
  return new Promise((resolve, reject) => {
    return request({
    url: '/room.queryAdminOwnerRooms',
    method: 'get',
    params
  }).then(res => {
      resolve(res.data)
    }).catch(err => {
      reject(err)
    })
  })
}