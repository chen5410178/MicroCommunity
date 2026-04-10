import request from '@/utils/request'

// 查询费用明细
export function queryFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryFeeDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询费用明细失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}