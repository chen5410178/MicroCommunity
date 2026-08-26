import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取排班列表
export function listScheduleClasses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.listScheduleClasses',
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

// 删除排班
export function deleteScheduleClasses(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.deleteScheduleClasses',
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

// 更新排班状态
export function updateScheduleClassesState(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/scheduleClasses.updateScheduleClassesState',
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

