import request from '@/utils/request'

export function listAdminFee(params) {
  return request({
    url: '/fee.listAdminFee',
    method: 'get',
    params
  }).then(res => {
    return res.data
  })
}

export function queryAdminOwnerCars(params) {
  return request({
    url: '/owner.queryAdminOwnerCars',
    method: 'get',
    params
  }).then(res => {
    return res.data
  })
}