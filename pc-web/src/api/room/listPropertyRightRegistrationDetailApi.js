import request from '@/utils/request'

/**
 * 获取产权登记详情列表
 * @param {Object} params 查询参数
 * @returns {Promise} 
 */
export function listPropertyRightRegistrationDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/propertyRightRegistrationDetail.listPropertyRightRegistrationDetail',
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
 * 更新产权登记详情
 * @param {Object} data 更新数据
 * @returns {Promise}
 */
export function updatePropertyRightRegistrationDetail(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/propertyRightRegistrationDetail.updatePropertyRightRegistrationDetail',
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
 * 获取产权登记详情
 * @param {String} prrdId 详情ID
 * @returns {Promise}
 */
export function getPropertyRightRegistrationDetail(prrdId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/propertyRightRegistrationDetail.getPropertyRightRegistrationDetail',
      method: 'get',
      params: { prrdId }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}