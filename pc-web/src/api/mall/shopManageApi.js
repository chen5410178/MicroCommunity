import request from '@/utils/request'

// 获取店铺列表
export function getShopList(params) {
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
        reject(new Error(res.msg || '获取店铺列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 审核店铺
export function auditShop(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop/auditShop',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '审核店铺失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}