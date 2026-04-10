import request from '@/utils/request'

export function queryAdminOwners(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryAdminOwners',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get owners'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}