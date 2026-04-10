import request from '@/utils/request'

/**
 * 查询合同变更计划列表
 * @param {Object} params 查询参数
 * @returns {Promise} 返回Promise对象
 */
export function queryContractChangePlan(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractChangePlan',
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
 * 查询合同类型列表
 * @param {Object} params 查询参数
 * @returns {Promise} 返回Promise对象
 */
export function queryContractType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractType',
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
 * 删除合同变更计划
 * @param {Object} data 删除数据
 * @returns {Promise} 返回Promise对象
 */
export function deleteContractChangePlan(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/deleteContractChangePlan',
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