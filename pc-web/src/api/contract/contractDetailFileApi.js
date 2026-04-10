import request from '@/utils/request'

export function queryContractFile(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contractFile/queryContractFile',
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