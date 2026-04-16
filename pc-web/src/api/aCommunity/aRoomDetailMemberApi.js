import request from '@/utils/request'

export function queryAdminOwnerMembers(params) {
  return request({
    url: '/owner.queryAdminOwnerMembers',
    method: 'get',
    params
  }).then(res => {
    return res.data
  })
}