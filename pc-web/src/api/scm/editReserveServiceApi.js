import request from '@/utils/request'

/**
 * 更新预约服务信息
 * @param {Object} data 服务信息
 * @returns {Promise}
 */
export function updateReserveGoods(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.updateReserveGoods',
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

/**
 * 获取预约参数列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listReserveParams(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.listReserveParams',
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
 * 获取预约服务列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listReserveGoods(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.listReserveGoods',
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
 * @returns {Promise}
 */
export function getFileUrl(fileId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/download/getFile/file',
      method: 'get',
      params: {
        fileId,
        communityId: -1
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}