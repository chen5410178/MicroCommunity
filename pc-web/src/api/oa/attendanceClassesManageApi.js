import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取考勤班组列表
export function listAttendanceClassess(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClasses.listAttendanceClassess',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加考勤班组
export function saveAttendanceClasses(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClasses.saveAttendanceClasses',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 修改考勤班组
export function updateAttendanceClasses(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClasses.updateAttendanceClasses',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除考勤班组
export function deleteAttendanceClasses(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClasses.deleteAttendanceClasses',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}