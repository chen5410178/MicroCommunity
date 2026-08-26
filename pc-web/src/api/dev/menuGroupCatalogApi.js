import request from '@/utils/request'

// 获取菜单组列表
export function getMenuGroupCatalogList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuGroupCatalog.listMenuGroupCatalog',
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
export function addMenuGroupCatalog(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuGroupCatalog.saveMenuGroupCatalog',
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

// 更新菜单组
export function updateMenuGroupCatalog(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuGroupCatalog.updateMenuGroupCatalog',
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

// 删除菜单组
export function deleteMenuGroupCatalog(gcId,storeType) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuGroupCatalog.deleteMenuGroupCatalog',
      method: 'post',
      data: { gcId,storeType }
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
