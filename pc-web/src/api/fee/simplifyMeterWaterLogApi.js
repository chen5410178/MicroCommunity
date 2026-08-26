import request from '@/utils/request'

export function listMeterWaters(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meterWater.listMeterWaters',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

export function listMeterTypes(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meterType.listMeterType',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}