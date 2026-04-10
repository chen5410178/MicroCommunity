import request from '@/utils/request'

// 综合查询
export function comprehensiveQuery(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerApi/comprehensiveQuery',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '综合查询失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询押金费用
export function queryFeeDeposit(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryFeeDeposit',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询押金费用失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}