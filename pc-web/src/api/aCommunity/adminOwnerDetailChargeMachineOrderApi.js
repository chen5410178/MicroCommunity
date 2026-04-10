import request from '@/utils/request'

export function listAdminMachineOrders(params) {
  return request({
    url: '/machineOrder.listAdminMachineOrders',
    method: 'get',
    params
  }).then(res => {
    return res.data
  })
}