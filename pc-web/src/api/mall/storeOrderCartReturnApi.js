import request from '@/utils/request'

// 获取退货订单列表
export function getStoreOrderCartReturnList(params) {
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
        reject(new Error(res.msg || 'Failed to get store order cart return list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}