import request from '@/utils/request'

export function saveRoomCreateFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.saveRoomCreateFee',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
        resolve(res)

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