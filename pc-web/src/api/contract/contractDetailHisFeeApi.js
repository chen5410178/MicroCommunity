import request from '@/utils/request'

export function queryFeeDetail(params) {
  return request({
    url: '/fee.queryFeeDetail',
    method: 'get',
    params
  })
}