import request from '@/utils/request'

/**
 * 获取班次列表
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function listScheduleClasses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.listScheduleClasses',
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
 * 获取员工月度排班表
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function staffMonthScheduleClasses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.staffMonthScheduleClasses',
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
 * 导出排班表数据
 * @param {Object} params 导出参数
 * @returns {Promise} Promise对象
 */
export function exportData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
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
 * 获取组织树
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
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