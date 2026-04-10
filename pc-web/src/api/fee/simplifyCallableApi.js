import request from '@/utils/request'

export function listOweFeeCallable(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oweFeeCallable.listOweFeeCallable',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to list owe fee callable'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}