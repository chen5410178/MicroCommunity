import request from '@/utils/request'

export function queryAdminApplyRoomDiscount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryAdminApplyRoomDiscount',
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