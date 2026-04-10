import request from '@/utils/request'

// 获取供应商类型列表
export function listSupplierType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplierType.listSupplierType',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取供应商类型列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加供应商类型
export function saveSupplierType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplierType.saveSupplierType',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加供应商类型失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新供应商类型
export function updateSupplierType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplierType.updateSupplierType',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新供应商类型失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除供应商类型
export function deleteSupplierType(stId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplierType.deleteSupplierType',
      method: 'post',
      data: { stId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除供应商类型失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}