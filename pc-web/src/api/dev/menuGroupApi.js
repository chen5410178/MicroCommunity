import request from '@/utils/request'

// 获取菜单组列表
export function getMenuGroupList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuGroup.listMenuGroups',
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

// 添加菜单组
export function addMenuGroup(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuGroup.saveMenuGroup',
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

// 更新菜单组
export function updateMenuGroup(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuGroup.updateMenuGroup',
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

// 删除菜单组
export function deleteMenuGroup(gId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuGroup.deleteMenuGroup',
      method: 'post',
      data: { gId }
    }).then(response => {
      const res = response.data

        resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}
