import request from '@/utils/request'

// 查询订单列表
export function queryAdminStoreOrderCart(params) {
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
        reject(new Error(res.msg || 'Failed to query order list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询商铺列表
export function queryShopsByAdmin(params) {
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
        reject(new Error(res.msg || 'Failed to query shops'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}