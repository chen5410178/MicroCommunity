import request from '@/utils/request'

/**
 * 查询考勤记录列表
 * @param {Object} params 查询参数
 * @param {string} params.classesName 班组名称
 * @param {string} params.departmentName 部门名称
 * @param {string} params.date 打卡日期
 * @param {string} params.staffName 员工名称
 * @param {number} params.page 当前页码
 * @param {number} params.row 每页条数
 * @returns {Promise} 考勤记录列表
 */
export function queryAttendanceLog(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClass/queryAttendanceLog',
      method: 'get',
      params: {
        ...params,
        classesName: params.classesName.trim() || '',
        departmentName: params.departmentName.trim() || ''
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}