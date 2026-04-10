import request from '@/utils/request'

export function queryCommunityOwnerAccount(params) {
  return request({
    url: '/account.queryCommunityOwnerAccount',
    method: 'get',
    params
  })
}