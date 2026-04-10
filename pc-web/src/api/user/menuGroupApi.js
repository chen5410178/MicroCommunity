import request from '@/utils/request'

// 获取项目列表
export function getMenuGroupList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/menu.listMenuGroup',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(response)
      } else {
        reject(new Error(res.msg || '获取项目列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加项目
export function addMenuGroup(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/menu.saveMenuGroup',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新项目
export function updateMenuGroup(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/menu.updateMenuGroup',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除项目
export function deleteMenuGroup(menuGroupId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/menu.deleteMenuGroup',
      method: 'post',
      data: { menuGroupId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
} 