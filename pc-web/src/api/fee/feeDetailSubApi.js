import request from '@/utils/request'

export function listPayFeeSub(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeSub.listPayFeeSub',
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