import request from '@/utils/request'

export function queryApplyRoomDiscount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscount/queryApplyRoomDiscount',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to query apply room discount'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}