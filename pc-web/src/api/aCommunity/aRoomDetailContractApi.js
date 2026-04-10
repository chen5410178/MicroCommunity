import request from '@/utils/request'

export function queryAdminOwnerContract(params) {
  return request({
    url: '/contract.queryAdminOwnerContract',
    method: 'get',
    params
  }).then(res => {
    return res.data
  })
}