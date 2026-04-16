import request from '@/utils/request'

// 获取营销黑白单列表
export function listMarketBlacklist(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketBlacklist.listMarketBlacklist',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销黑白单列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加营销黑白单
export function saveMarketBlacklist(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketBlacklist.saveMarketBlacklist',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加营销黑白单失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新营销黑白单
export function updateMarketBlacklist(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketBlacklist.updateMarketBlacklist',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新营销黑白单失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除营销黑白单
export function deleteMarketBlacklist(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketBlacklist.deleteMarketBlacklist',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除营销黑白单失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}