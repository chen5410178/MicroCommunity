import request from '@/utils/request'

// 获取刷新缓存列表
export function getCacheList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.console.caches',
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

// 添加刷新缓存
export function flushCache(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/cacheManage/flushCache',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '刷新缓存失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新刷新缓存
export function updateCache(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/cache.updateCache',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新刷新缓存失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除刷新缓存
export function deleteCache() {
  return new Promise((resolve, reject) => {
    request({
      url: '/cache.deleteCache',
      method: 'post',
      data: {  }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除刷新缓存失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
