import request from '@/utils/request'

// 获取系统用户详情
export function getSystemUserDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.listSystemUsers',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取系统用户详情失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取系统用户属性列表
export function listSystemUserAttr(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.listSystemUserAttr',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取系统用户属性列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取管理员店铺用户列表
export function listAdminStoreUser(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/storeUser.listAdminStoreUser',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取管理员店铺用户列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取管理员应用用户业主列表
export function listAdminAppUserOwners(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.listAdminAppUserOwners',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取管理员应用用户业主列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新应用用户绑定业主
export function updateAppUserBindingOwner(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.updateAppUserBindingOwner',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新应用用户绑定业主失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除应用用户绑定业主
export function deleteAppUserBindingOwner(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.deleteAppUserBindingOwner',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除应用用户绑定业主失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}