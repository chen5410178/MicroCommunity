import request from '@/utils/request'

export function saveProxyFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meterWater.saveProxyFee',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to save proxy fee'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.listFeeConfigs',
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