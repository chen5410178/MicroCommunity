import request from '@/utils/request'

export function listAdminConfigFeeObjs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.listAdminConfigFeeObjs',
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