import request from '@/utils/request'

// 查询积分列表
export function queryIntegralList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.getAdminMallOpenApi',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询积分列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}