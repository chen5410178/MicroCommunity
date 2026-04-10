import request from '@/utils/request'

// 查询管理员统计数据
export function queryAdminCount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/admin.queryAdminCount',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to query admin count'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询小区缴费数据
export function queryCommunityFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/admin.queryCommunityFee',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to query community fee'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询小区报修数据
export function queryCommunityRepair(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/admin.queryCommunityRepair',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to query community repair'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}