import request from '@/utils/request'

// 获取营销短信列表
export function listMarketSms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketSms.listMarketSms',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销短信列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存营销短信
export function saveMarketSms(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketSms.saveMarketSms',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存营销短信失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新营销短信
export function updateMarketSms(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketSms.updateMarketSms',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新营销短信失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除营销短信
export function deleteMarketSms(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketSms.deleteMarketSms',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除营销短信失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取营销短信键列表
export function listMarketSmsKey(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketSms.listMarketSmsKey',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销短信键列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}