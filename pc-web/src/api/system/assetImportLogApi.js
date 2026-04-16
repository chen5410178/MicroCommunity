import request from '@/utils/request'

/**
 * Query asset import logs
 * @param {Object} params
 * @param {number} params.page - Page number
 * @param {number} params.row - Page size
 * @param {string} params.communityId - Community ID
 * @returns {Promise} Promise object represents the import logs
 */
export function queryAssetImportLog(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/assetImportLog/queryAssetImportLog',
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