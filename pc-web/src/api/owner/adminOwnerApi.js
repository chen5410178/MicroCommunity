import request from '@/utils/request'

// 查询业主列表
export function queryAdminOwners(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryAdminOwners',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to query owners'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询管理小区列表
export function listAdminCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listAdminCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to list communities'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}