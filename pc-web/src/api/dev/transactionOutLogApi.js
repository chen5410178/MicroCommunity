import request from '@/utils/request'

// 查询调用外部系统日志
export function queryTransactionOutLog(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/log.queryTransactionOutLog',
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
        reject(new Error(res.msg || 'Failed to query transaction out log'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
