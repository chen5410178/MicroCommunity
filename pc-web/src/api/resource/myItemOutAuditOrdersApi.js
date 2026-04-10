import request from '@/utils/request'

/**
 * 获取待审核订单列表
 * @param {Object} params 查询参数
 * @returns {Promise} 返回Promise对象
 */
export function getCollectionAuditOrder(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/collection/getCollectionAuditOrder',
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