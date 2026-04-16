import request from '@/utils/request'

// 获取权限列表
export function getPrivilegeList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/basePrivilege.listBasePrivileges',
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

// 添加权限
export function addPrivilege(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/basePrivilege.saveBasePrivilege',
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

// 更新权限
export function updatePrivilege(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/basePrivilege.updateBasePrivilege',
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

// 删除权限
export function deletePrivilege(pId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/basePrivilege.deleteBasePrivilege',
      method: 'post',
      data: { pId }
    }).then(response => {
      const res = response.data

        resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}
