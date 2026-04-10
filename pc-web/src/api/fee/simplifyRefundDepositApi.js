import request from '@/utils/request'

export function queryFeeDeposit(params) {
  return request({
    url: '/fee.queryFeeDeposit',
    method: 'get',
    params
  })
}