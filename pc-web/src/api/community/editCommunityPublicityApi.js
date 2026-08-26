import request from '@/utils/request'

/**
 * 更新社区公示信息
 * @param {Object} data 公示信息
 * @returns {Promise}
 */
export function updateCommunityPublicity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/publicity.updateCommunityPublicity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 获取社区公示列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listCommunityPublicity(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/publicity.listCommunityPublicity',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data || [],
        total: res.total || 0
      })
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
 * 获取单个公示详情
 * @param {String} pubId 公示ID
 * @returns {Promise}
 */
export function getCommunityPublicityDetail(pubId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/publicity.getCommunityPublicity',
      method: 'get',
      params: { pubId }
    }).then(response => {
      const res = response.data
      resolve(res.data)
    }).catch(error => {
      reject(error)
    })
  })
}