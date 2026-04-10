import request from '@/utils/request'

// Query owner information
export function queryOwners(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryOwners',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to query owners'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}