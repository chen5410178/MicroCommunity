import request from '@/utils/request'

// 获取供应商列表
export function listResourceSuppliers(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceSupplier.listResourceSuppliers',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取供应商列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加供应商
export function saveResourceSupplier(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceSupplier.saveResourceSupplier',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加供应商失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新供应商
export function updateResourceSupplier(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceSupplier.updateResourceSupplier',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新供应商失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除供应商
export function deleteResourceSupplier(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceSupplier.deleteResourceSupplier',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除供应商失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}