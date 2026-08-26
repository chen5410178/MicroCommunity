import request from '@/utils/request'

/**
 * 保存催缴欠费信息
 * @param {Object} data 
 * @returns {Promise}
 */
export function saveOweFeeCallable(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oweFeeCallable.saveOweFeeCallable',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
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
 * 获取欠费列表
 * @param {Object} params 
 * @returns {Promise}
 */
export function listFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listFee',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}