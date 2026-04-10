import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

/**
 * 获取产权登记列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listPropertyRightRegistration(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/propertyRightRegistration.listPropertyRightRegistration',
      method: 'get',
      params: {
        ...params,
        communityId
      }
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
 * 添加产权登记
 * @param {Object} data 产权登记数据
 * @returns {Promise}
 */
export function savePropertyRightRegistration(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/propertyRightRegistration.savePropertyRightRegistration',
      method: 'post',
      data: {
        ...data,
        communityId
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
 * 更新产权登记
 * @param {Object} data 更新数据
 * @returns {Promise}
 */
export function updatePropertyRightRegistration(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/propertyRightRegistration.updatePropertyRightRegistration',
      method: 'post',
      data: {
        ...data,
        communityId
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
 * 删除产权登记
 * @param {String} prrId 产权登记ID
 * @returns {Promise}
 */
export function deletePropertyRightRegistration(prrId) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/propertyRightRegistration.deletePropertyRightRegistration',
      method: 'post',
      data: {
        prrId,
        communityId
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}