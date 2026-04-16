import request from '@/utils/request'

// 查询商家列表
export function queryMerchant(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.getAdminMallOpenApi',
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
        reject(new Error(res.msg || 'Failed to query merchant list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}