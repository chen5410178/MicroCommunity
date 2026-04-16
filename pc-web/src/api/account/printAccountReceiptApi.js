import request from '@/utils/request'

/**
 * 获取账户收据列表
 * @param {Object} params 查询参数
 * @returns {Promise} 
 */
export function listAccountReceipt(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/receipt.listAccountReceipt',
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
 * 查询打印规格
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function queryFeePrintSpec(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feePrintSpec/queryFeePrintSpec',
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