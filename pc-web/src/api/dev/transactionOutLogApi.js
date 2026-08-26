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
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}
