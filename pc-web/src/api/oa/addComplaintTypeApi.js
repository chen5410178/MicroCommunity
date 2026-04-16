import request from '@/utils/request'

/**
 * 获取组织树
 * @param {Object} params 
 * @returns {Promise}
 */
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
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
 * 查询员工信息
 * @param {Object} params 
 * @returns {Promise}
 */
export function getStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
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
 * 保存投诉类型
 * @param {Object} data 
 * @returns {Promise}
 */
export function saveComplaintType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaintType.saveComplaintType',
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