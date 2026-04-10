import request from '@/utils/request'

/**
 * 保存排班信息
 * @param {Object} data 排班数据
 * @returns {Promise}
 */
export function saveScheduleClasses(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.saveScheduleClasses',
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
 * 获取班次列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listClasses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/classes.listClasses',
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
 * 获取排班详情
 * @param {String} scheduleId 排班ID
 * @returns {Promise}
 */
export function getScheduleClassesDetail(scheduleId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.getScheduleClasses',
      method: 'get',
      params: { scheduleId }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}