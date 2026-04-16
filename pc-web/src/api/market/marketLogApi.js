import request from '@/utils/request'

// List market logs
export function listMarketLog(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.listMarketLog',
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
        reject(new Error(res.msg || 'Failed to get market logs'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}