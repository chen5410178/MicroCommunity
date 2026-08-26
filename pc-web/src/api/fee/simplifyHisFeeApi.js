import request from '@/utils/request'

export function queryFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryFeeDetail',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

export function generatorReceipt(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/receipt.generatorReceipt',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}