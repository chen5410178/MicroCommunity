import request from '@/utils/request'

// Save privilege group
export function savePrivilegeGroup(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/save.privilegeGroup.info',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Save privilege group failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// Update privilege group
export function updatePrivilegeGroup(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/edit.privilegeGroup.info',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Update privilege group failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// Delete privilege group
export function deletePrivilegeGroup(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/delete.privilegeGroup.info',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Delete privilege group failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// Query privilege groups
export function queryPrivilegeGroup(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.store.privilegeGroup',
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

// Query privilege group no add privilege
export function queryPrivilegeGroupNoAddPrivilege(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.privilegeGroup.noAddPrivilege',
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

// Add privilege to privilege group
export function addPrivilegeToPrivilegeGroup(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/add.privilege.PrivilegeGroup',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Add privilege to privilege group failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// Delete privilege from privilege group
export function deletePrivilegeFromPrivilegeGroup(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/delete.privilege.PrivilegeGroup',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Delete privilege from privilege group failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// Save role community
export function saveRoleCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roleCommunity.saveRoleCommunity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Save role community failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// Delete role community
export function deleteRoleCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roleCommunity.deleteRoleCommunity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Delete role community failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// List role communities
export function listRoleCommunity(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roleCommunity.listRoleCommunity',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'List role communities failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// List org no communities
export function listOrgNoCommunities(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgNoCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'List org no communities failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// Save role staff
export function saveRoleStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/role.saveRoleStaff',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Save role staff failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// Delete role staff
export function deleteRoleStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/role.deleteRoleStaff',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Delete role staff failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// List role staffs
export function listRoleStaff(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/role.listRoleStaff',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'List role staffs failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// List staffs no role
export function listStaffsNoRole(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/role.listStaffsNoRole',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'List staffs no role failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}