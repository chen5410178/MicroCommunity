import request from '@/utils/request'

export function queryCommunityOwnerAccount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/account.queryCommunityOwnerAccount',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取业主账户列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}