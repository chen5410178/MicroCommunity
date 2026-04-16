import request from '@/utils/request'

/**
 * 获取调拨已办单列表
 * @param {Object} params 查询参数
 * @returns {Promise} 返回Promise对象
 */
export function listAllocationStoreHisAuditOrders(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listAllocationStoreHisAuditOrders',
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