import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取活动列表
export function listActivities(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/activities.listActivitiess',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve({
        data: res.activitiess,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取活动详情
export function getActivitiesDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/activities.listActivitiess',
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

// 添加活动
export function saveActivities(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/activities.saveActivities',
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

// 更新活动
export function updateActivities(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/activities.updateActivities',
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

// 删除活动
export function deleteActivities(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/activities.deleteActivities',
      method: 'post',
      data: {
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

// 获取活动类型列表
export function listActivitiesType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/activitiesType/queryActivitiesType',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传图片
export function uploadImage(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadFile/uploadImage',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}