import request from '@/utils/request'

// 查询交互日志列表
export function queryTransactionLog(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/transactionLog/queryTransactionLog',
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

// 查询交互日志报文
export function queryTransactionLogMessage(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/transactionLog/queryTransactionLogMessage',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data
      })
    }).catch(error => {
      reject(error)
    })
  })
}
