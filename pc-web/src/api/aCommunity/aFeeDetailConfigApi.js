import request from '@/utils/request'

export function queryAdminFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.queryAdminFeeConfigs',
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