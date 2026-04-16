import request from '@/utils/request'

// 查询合同信息
export function queryContract(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContract',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询合同信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}