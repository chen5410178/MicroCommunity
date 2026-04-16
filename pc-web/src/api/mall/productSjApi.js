import request from '@/utils/request'

// 获取商品列表
export function getAdminProductList(params) {
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
        reject(new Error(res.msg || 'Failed to get product list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除商品
export function deleteProduct(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/product/deleteProduct',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to delete product'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}