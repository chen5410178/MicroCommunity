import request from '@/utils/request'

export function queryAdminOwners(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryAdminOwners',
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