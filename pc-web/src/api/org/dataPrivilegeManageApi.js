import request from '@/utils/request'

// 获取数据权限列表
export function listDataPrivilege(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataPrivilege.listDataPrivilege',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加数据权限
export function saveDataPrivilege(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataPrivilege.saveDataPrivilege',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新数据权限
export function updateDataPrivilege(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataPrivilege.updateDataPrivilege',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除数据权限
export function deleteDataPrivilege(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataPrivilege.deleteDataPrivilege',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取数据权限单元列表
export function listDataPrivilegeUnit(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataPrivilegeUnit.listDataPrivilegeUnit',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取未关联的单元列表
export function listUnitsNotInDataPrivilege(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataPrivilegeUnit.listUnitsNotInDataPrivilege',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存数据权限单元
export function saveDataPrivilegeUnit(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataPrivilegeUnit.saveDataPrivilegeUnit',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除数据权限单元
export function deleteDataPrivilegeUnit(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataPrivilegeUnit.deleteDataPrivilegeUnit',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取数据权限员工列表
export function listDataPrivilegeStaff(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataPrivilegeStaff.listDataPrivilegeStaff',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取未关联的员工列表
export function listStaffNotInDataPrivilege(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataPrivilegeStaff.listStaffNotInDataPrivilege',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存数据权限员工
export function saveDataPrivilegeStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataPrivilegeStaff.saveDataPrivilegeStaff',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除数据权限员工
export function deleteDataPrivilegeStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataPrivilegeStaff.deleteDataPrivilegeStaff',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}