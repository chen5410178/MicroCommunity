import request from '@/utils/request'

// 获取员工详情
export function getStaffDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
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

// 获取员工组织
export function getStaffOrgs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.listStaffOrgs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取员工组织失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取员工角色
export function getStaffRoles(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.listStaffRoles',
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

// 获取员工权限
export function getStaffPrivileges(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.user.privilege',
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

// 获取工作许可证位置
export function listWorkLicensePos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workLicense.listWorkLicensePos',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取工作许可证位置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}