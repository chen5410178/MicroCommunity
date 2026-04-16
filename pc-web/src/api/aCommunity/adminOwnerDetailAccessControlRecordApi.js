import request from '@/utils/request'

export function listAdminMachineRecords(params) {
  return request({
    url: '/machineRecord.listAdminMachineRecords',
    method: 'get',
    params
  }).then(res => {
    return res.data
  })
}