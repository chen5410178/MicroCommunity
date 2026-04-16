import request from '@/utils/request'

/**
 * 获取班次员工列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listScheduleClassesStaff(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.listScheduleClassesStaff',
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
 * 保存班次员工
 * @param {Object} data 保存数据
 * @returns {Promise}
 */
export function saveScheduleClassesStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.saveScheduleClassesStaff',
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
 * 删除班次员工
 * @param {Object} data 删除数据
 * @returns {Promise}
 */
export function deleteScheduleClassesStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.deleteScheduleClassesStaff',
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
 * 获取组织树
 * @returns {Promise}
 */
export function listOrgTree() {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
      method: 'get'
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 根据组织ID获取员工列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listStaffByOrg(params) {
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