import request from '@/utils/request'

/**
 * 获取用户菜单列表
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
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