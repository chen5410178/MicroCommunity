import request from '@/utils/request'

export function queryContractRoom(params) {
  return new Promise((resolve, reject) => {
    request({
    url: '/contract/queryContractRoom',
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