import request from '@/utils/request'

export function listAdminOwnerRepairs(params) {
  return request({
    url: '/ownerRepair.listAdminOwnerRepairs',
    method: 'get',
    params
  }).then(res => {
    return res.data
  })
} 