import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

/**
 * 查询费用导入列表
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function queryImportFee(params) {
  return new Promise((resolve, reject) => {
    const queryParams = {
      ...params,
      communityId: getCommunityId()
    }
    request({
      url: '/importFee/queryImportFee',
      method: 'get',
      params: queryParams
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total,
        records: res.records
      })
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 导入费用数据
 * @param {FormData} formData 表单数据
 * @returns {Promise} Promise对象
 */
export function importRoomFeeData(formData) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/upload/assetImport/importData',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
        resolve(res)
    }).catch(error => {
      console.log('error:', error.response.data)
      reject(error.response.data)
    })
  })
}

/**
 * 下载导入模板
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function downloadImportTemplate(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 获取费用导入详情
 * @param {String} importFeeId 导入ID
 * @returns {Promise} Promise对象
 */
export function getImportFeeDetail(importFeeId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/importFee/getImportFeeDetail',
      method: 'get',
      params: {
        importFeeId,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res.data)
    }).catch(error => {
      reject(error)
    })
  })
}