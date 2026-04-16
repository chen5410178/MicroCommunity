import request from '@/utils/request'

/**
 * 查询待办访客列表
 * @param {Object} params 查询参数
 * @returns {Promise} 返回Promise对象
 */
export function queryUndoVisit(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/visit.queryUndoVisit',
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