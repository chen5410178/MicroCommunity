import request from '@/utils/request'

export function queryAdminImportFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryAdminImportFeeDetail',
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