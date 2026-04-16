import request from '@/utils/request'

// 获取月考勤列表
export function getMonthAttendance(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClass/getMonthAttendance',
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

// 获取考勤班次列表
export function getAttendanceClasses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClasses.listAttendanceClassess',
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

// 导出月考勤数据
export function exportMonthAttendance(params) {
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