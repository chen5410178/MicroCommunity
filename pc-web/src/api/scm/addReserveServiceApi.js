import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

/**
 * 保存预约服务信息
 * @param {Object} data 服务信息
 * @returns {Promise}
 */
export function saveReserveGoods(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.saveReserveGoods',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 获取预约参数列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listReserveParams(params = {}) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.listReserveParams',
      method: 'get',
      params: {
        page: 1,
        row: 100,
        communityId: getCommunityId(),
        ...params
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 上传文件
 * @param {FormData} formData 文件表单数据
 * @returns {Promise}
 */
export function uploadFile(formData) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadFile',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 获取文件下载URL
 * @param {String} fileId 文件ID
 * @returns {String} 文件下载URL
 */
export function getFileUrl(fileId) {
  return `/callComponent/download/getFile/file?fileId=${fileId}&communityId=${getCommunityId()}&time=${new Date().getTime()}`
}