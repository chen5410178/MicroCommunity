import request from '@/utils/request'

export function listMeterWaters(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meterWater.listMeterWaters',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to list meter waters'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}