import request from '@/utils/request'

// 获取系统用户列表
export function listSystemUsers(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.listSystemUsers',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || 'Failed to get user list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除系统用户
export function deleteSystemUser(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.deleteSystemUser',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to delete user'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 重置系统用户密码
export function resetSystemUserPwd(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.resetSystemUserPwd',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to reset password'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
