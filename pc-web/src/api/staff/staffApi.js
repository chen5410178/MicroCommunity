// api/staff/staffApi.js
import request from '@/utils/request'

// Query staff list
export function queryStaffInfos(params) {
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

// Reset staff password
export function resetStaffPwd(data) {
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

// Modify staff info
export function modifyStaff(data) {
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
        reject(new Error(res.msg || '修改员工信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// Delete staff
export function deleteStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.staff.delete',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除员工失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// Get orgs by level
export function listOrgs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取组织列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}