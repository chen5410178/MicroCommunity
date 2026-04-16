import request from '@/utils/request'

/**
 * 查询今日考勤列表
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function queryAttendanceClassesTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClass/queryAttendanceClassesTask',
      method: 'get',
      params
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