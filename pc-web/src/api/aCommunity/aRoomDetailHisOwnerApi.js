import request from '@/utils/request'

export function queryAdminRoomHisOwner(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryAdminRoomHisOwner',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get owner history'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}