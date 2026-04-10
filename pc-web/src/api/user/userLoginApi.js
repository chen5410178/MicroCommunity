import request from '@/utils/request'

// 查询用户登录日志
export function queryUserLogin(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/userLogin/queryUserLogin',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          records: res.records,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || 'Failed to query user login logs'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取门店列表
export function listStores(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/store.listStores',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data
        })
      } else {
        reject(new Error(res.msg || 'Failed to get store list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}