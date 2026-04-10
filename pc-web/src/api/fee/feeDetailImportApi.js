import request from '@/utils/request'

export function queryImportFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/importFee/queryImportFeeDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to query import fee detail'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}