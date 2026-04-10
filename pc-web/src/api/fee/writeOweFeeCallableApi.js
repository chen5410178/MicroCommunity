import request from '@/utils/request'

export function writeOweFeeCallable(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oweFeeCallable.writeOweFeeCallable',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to write owe fee callable'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function listFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listFee',
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