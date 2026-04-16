import request from '@/utils/request'

// 获取积分交易明细列表
export function getIntegralDetailList(params) {
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
        reject(new Error(res.msg || 'Failed to get points transaction details'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}