import request from '@/utils/request'

// 获取业主认证列表
export function listAdminAppUserOwners(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.listAdminAppUserOwners',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || '获取业主认证列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取管理员小区列表
export function listAdminCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listAdminCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data
        })
      } else {
        reject(new Error(res.msg || '获取管理员小区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}