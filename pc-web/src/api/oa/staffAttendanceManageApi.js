import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询员工信息列表
export function queryStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询考勤任务
export function queryAttendanceClassesTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClass/queryAttendanceClassesTask',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询考勤日志
export function queryAttendanceLog(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClass/queryAttendanceLog',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 补考勤
export function attendanceReplenishCheckIn(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClasses.attendanceReplenishCheckIn',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询组织树
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}