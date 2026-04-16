import request from '@/utils/request'

export function listFee(params) {
  return request({
    url: '/fee.listFee',
    method: 'get',
    params
  })
}

export function queryOwnerCars(params) {
  return request({
    url: '/owner.queryOwnerCars',
    method: 'get',
    params
  })
}