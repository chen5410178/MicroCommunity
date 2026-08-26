import request from '@/utils/request'

// 查询管理员社区列表
export function listAdminCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listAdminCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询管理员业主账户
export function queryAdminOwnerAccount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/account.queryAdminOwnerAccount',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}