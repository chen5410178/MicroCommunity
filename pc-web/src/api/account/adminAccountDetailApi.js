import request from '@/utils/request'

// 查询管理员账户明细
export function queryAdminAccountDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/account.queryAdminAccountDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to query admin account details'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}