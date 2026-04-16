import request from '@/utils/request'

export function listPayFeeSub(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeSub.listPayFeeSub',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get fee sub list'))
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
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get fee list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}