import request from '@/utils/request'

// 获取项目列表
export function getTenantList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/tenant.listTenant',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(response)
      } else {
        reject(new Error(res.msg || '获取项目列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加项目
export function addTenant(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/tenant.saveTenant',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新项目
export function updateTenant(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/tenant.updateTenant',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除项目
export function deleteTenant(tenantId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/tenant.deleteTenant',
      method: 'post',
      data: { tenantId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
} 
export function resetTenantPassword(tenantId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/tenant.resetTenantPassword',
      method: 'post',
      data: { tenantId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '重置密码失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
