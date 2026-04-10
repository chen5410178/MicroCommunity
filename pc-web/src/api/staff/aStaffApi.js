import request from '@/utils/request'

// 查询员工信息
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

// 重置员工密码
export function resetStaffPwd(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.resetStaffPwd',
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

// 修改员工信息
export function modifyStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.staff.modify',
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

// 删除员工
export function deleteStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.staff.delete',
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

// 获取组织列表
export function listOrgs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgs',
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

// 获取字典数据
export function getDict(dictName, dictType) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.getDict',
      method: 'get',
      params: { dictName, dictType }
    }).then(response => {
      const res = response.data

        resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}

// 上传文件
export function uploadImage(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadImage',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '上传文件失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}