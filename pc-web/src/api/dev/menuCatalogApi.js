import request from '@/utils/request'

// 获取菜单目录列表
export function getMenuCatalogList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuCatalog.listMenuCatalog',
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

// 添加菜单目录
export function addMenuCatalog(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuCatalog.saveMenuCatalog',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新菜单目录
export function updateMenuCatalog(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuCatalog.updateMenuCatalog',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除菜单目录
export function deleteMenuCatalog(caId,storeType) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuCatalog.deleteMenuCatalog',
      method: 'post',
      data: { caId ,storeType}
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
