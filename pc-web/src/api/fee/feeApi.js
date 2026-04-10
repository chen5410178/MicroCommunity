import request from '@/utils/request'

// 查询费用列表
export function listFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listFee',
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
