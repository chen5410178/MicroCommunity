import request from '@/utils/request'

export function listMeterWaters(params) {
  return request({
    url: '/meterWater.listMeterWaters',
    method: 'get',
    params
  })
}

export function listMeterTypes(params) {
  return request({
    url: '/meterType.listMeterType',
    method: 'get',
    params
  })
}