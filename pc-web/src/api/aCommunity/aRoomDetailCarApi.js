import request from '@/utils/request'

export function queryAdminOwnerCars(params) {
  return request({
    url: '/owner.queryAdminOwnerCars',
    method: 'get',
    params
  }).then(res => {
    return res.data
  })
}