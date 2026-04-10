import request from '@/utils/request'

/**
 * 获取合同打印模板
 * @param {Object} params 请求参数
 * @returns {Promise} Promise对象
 */
export function getPrintContractTemplate(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/printContractTemplate',
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