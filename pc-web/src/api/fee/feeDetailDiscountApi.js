import request from '@/utils/request'

export function queryApplyRoomDiscount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscount/queryApplyRoomDiscount',
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