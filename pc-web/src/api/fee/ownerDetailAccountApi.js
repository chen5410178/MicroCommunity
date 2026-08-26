import request from '@/utils/request'

export function queryCommunityOwnerAccount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/account.queryCommunityOwnerAccount',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}