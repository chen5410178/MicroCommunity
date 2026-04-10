import request from '@/utils/request'

/**
 * 查询资产导入日志详情
 * @param {Object} params 查询参数
 * @returns {Promise} 请求Promise
 */
export function queryAssetImportLogDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/assetImportLogDetail/queryAssetImportLogDetail',
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
 * 查询资产导入日志类型
 * @param {Object} params 查询参数
 * @returns {Promise} 请求Promise
 */
export function queryAssetImportLogType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/log.queryAssetImportLogType',
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