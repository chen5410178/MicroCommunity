import request from '@/utils/request'

// 获取营销文本列表
export function listMarketText(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketText.listMarketText',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销文本列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加营销文本
export function saveMarketText(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketText.saveMarketText',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加营销文本失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新营销文本
export function updateMarketText(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketText.updateMarketText',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新营销文本失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除营销文本
export function deleteMarketText(textId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketText.deleteMarketText',
      method: 'post',
      data: { textId }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除营销文本失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取营销图片列表
export function listMarketPic(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketPic.listMarketPic',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销图片列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加营销图片
export function saveMarketPic(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketPic.saveMarketPic',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加营销图片失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新营销图片
export function updateMarketPic(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketPic.updateMarketPic',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新营销图片失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除营销图片
export function deleteMarketPic(picId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketPic.deleteMarketPic',
      method: 'post',
      data: { picId }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除营销图片失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取营销商品列表
export function listMarketGoods(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketGoods.listMarketGoods',
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
export function saveMarketGoods(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketGoods.saveMarketGoods',
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
export function updateMarketGoods(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketGoods.updateMarketGoods',
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
export function deleteMarketGoods(goodsId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketGoods.deleteMarketGoods',
      method: 'post',
      data: { goodsId }
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

// 获取营销配置列表
export function listMarketSms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketSms.listMarketSms',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销配置列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}