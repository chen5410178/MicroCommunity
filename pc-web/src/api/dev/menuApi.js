import request from '@/utils/request'

// 获取菜单列表
export function getMenuList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menu.listMenus',
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

// 添加菜单
export function addMenu(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menu.configMenu',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (!res.code || res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加菜单失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新菜单
export function updateMenu(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menu.updateMenu',
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

// 删除菜单
export function deleteMenu(mId,pId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menu.deleteMenu',
      method: 'post',
      data: { mId,pId }
    }).then(response => {
      const res = response.data
      if (!res.code || res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除菜单失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
