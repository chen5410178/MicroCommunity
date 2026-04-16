import request from '@/utils/request'

// 获取考勤班次列表
export function listAttendanceClassess(params) {
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

// 获取考勤班次员工列表
export function listAttendanceClassesStaff(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClasses.listAttendanceClassesStaff',
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

// 删除考勤班次员工
export function deleteAttendanceClassesStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClasses.deleteAttendanceClassesStaff',
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