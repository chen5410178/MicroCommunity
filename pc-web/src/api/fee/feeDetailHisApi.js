import request from '@/utils/request'

export function queryHisFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryHisFee',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to query fee history'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}