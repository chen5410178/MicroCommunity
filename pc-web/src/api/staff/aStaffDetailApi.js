import request from '@/utils/request'

// 获取员工信息
export function getStaffInfo(params) {
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

// 获取员工组织信息
export function getStaffOrgs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.listStaffOrgs',
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

// 获取员工角色信息
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

// 获取员工权限信息
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

// 获取员工关联小区
export function listStaffCommunities(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/role.listAStaffCommunity',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取员工小区失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 重置员工密码
export function resetStaffPassword(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.resetStaffPwd',
      method: 'post',
      data
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

// 更新员工信息
export function updateStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.staff.modify',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新员工信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取字典数据
export function getDict(dictType, dictCd) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict/getDict',
      method: 'get',
      params: {
        dictType,
        dictCd
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取字典数据失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取待关联小区列表
export function listWaitStaffCommunities(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/role.listWaitAStaffCommunity',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取待关联小区失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存员工小区关联
export function saveStaffCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/role.saveStaffCommunity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存员工小区失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除员工小区关联
export function deleteStaffCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/role.deleteStaffCommunity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除员工小区失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}