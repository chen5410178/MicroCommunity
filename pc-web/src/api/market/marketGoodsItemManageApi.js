import request from '@/utils/request'

// 获取营销商品列表
export function listMarketGoodsItem(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketGoods.listMarketGoodsItem',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销商品列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加营销商品
export function saveMarketGoodsItem(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketGoods.saveMarketGoodsItem',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加营销商品失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新营销商品
export function updateMarketGoodsItem(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketGoods.updateMarketGoodsItem',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新营销商品失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除营销商品
export function deleteMarketGoodsItem(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketGoods.deleteMarketGoodsItem',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除营销商品失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}