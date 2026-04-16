import request from '@/utils/request'

export function listAccountReceipt(params) {
  return request({
    url: '/receipt.listAccountReceipt',
    method: 'get',
    params
  })
}