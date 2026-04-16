import request from '@/utils/request'

/**
 * 获取用户菜单列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listMenuUser(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuUser.listMenuUser',
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

/**
 * 添加用户菜单
 * @param {Object} data 菜单数据
 * @returns {Promise}
 */
export function saveMenuUser(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuUser.saveMenuUser',
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

/**
 * 删除用户菜单
 * @param {Object} data 删除参数
 * @returns {Promise}
 */
export function deleteMenuUser(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuUser.deleteMenuUser',
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

/**
 * 获取目录菜单列表
 * @returns {Promise}
 */
export function listCatalogMenus() {
  return new Promise((resolve, reject) => {
    request({
      url: '/menu.listCatalogMenus',
      method: 'get'
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}