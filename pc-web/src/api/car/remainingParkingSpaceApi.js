import request from '@/utils/request'

/**
 * 获取剩余车位信息
 * @param {Object} params 请求参数
 * @returns {Promise} Promise对象
 */
export function getRemainingParkingSpace(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get remaining parking space'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}