import request from '@/utils/request'

export function queryAdminRoomHisOwner(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryAdminRoomHisOwner',
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