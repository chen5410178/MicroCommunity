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
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}