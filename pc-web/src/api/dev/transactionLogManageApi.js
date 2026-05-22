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
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || 'Failed to query transaction logs'))
      }
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
      if (res.code === 0) {
        resolve({
          data: res.data
        })
      } else {
        reject(new Error(res.msg || 'Failed to query transaction log message'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
