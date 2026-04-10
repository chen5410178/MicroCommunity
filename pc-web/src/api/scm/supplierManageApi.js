import request from '@/utils/request'

// 查询供应商列表
export function listSupplier(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplier.listSupplier',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询供应商列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加供应商
export function saveSupplier(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplier.saveSupplier',
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
export function updateSupplier(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplier.updateSupplier',
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
export function deleteSupplier(supplierId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplier.deleteSupplier',
      method: 'post',
      data: { supplierId }
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

// 查询供应商类型列表
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
        reject(new Error(res.msg || '查询供应商类型失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询供应商配置
export function listSupplierConfig(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplierType.listSupplierConfig',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询供应商配置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存供应商配置
export function saveSupplierConfig(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/supplierType.saveSupplierConfig',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存供应商配置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}