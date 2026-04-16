import request from '@/utils/request'

// 获取物业公司列表
export function getPropertyList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/property.listProperty',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total,
          records: res.records
        })
      } else {
        reject(new Error(res.msg || '获取物业公司列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加物业公司
export function saveProperty(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/property.saveProperty',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加物业公司失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新物业公司
export function updateProperty(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/property.updateProperty',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新物业公司失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除物业公司
export function deleteProperty(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/property.deleteProperty',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除物业公司失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取物业公司员工
export function getPropertyStaffs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/storeStaff/getPropertyStaffs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data
        })
      } else {
        reject(new Error(res.msg || '获取物业公司员工失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 管理员登录物业
export function adminLoginProperty(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/login.adminLoginProperty',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '管理员登录物业失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新物业状态
export function updateStoreState(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/storeApi/updateStoreState',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新物业状态失败'))
      }
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
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '重置员工密码失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取菜单组
export function listMenuGroup(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuGroup.listMenuGroup',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data
        })
      } else {
        reject(new Error(res.msg || '获取菜单组失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取未入驻小区
export function listNoEnterCommunity(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/storeEnterCommunity/listNoEnterCommunity',
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