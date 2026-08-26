import request from '@/utils/request'

export function queryImportFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/importFee/queryImportFeeDetail',
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