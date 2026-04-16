import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取工单详情
export function getWorkDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listWorkPool',
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

// 获取工单项列表
export function getWorkTaskItems(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/task.listWorkTaskItem',
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

// 获取工单事件列表
export function getWorkEvents(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workEvent.listWorkEvent',
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

// 获取工单文件列表
export function getWorkFiles(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listWorkPoolFile',
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

// 完成工单任务
export function finishWorkTask(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.finishWorkTask',
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

// 获取组织树
export function getOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
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

// 获取员工列表
export function getStaffList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
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

// 上传文件
export function uploadFile(data, config) {
  return new Promise((resolve, reject) => {
    const formData = new FormData()
    formData.append('uploadFile', data.get('uploadFile'))
    
    request({
      url: '/uploadVedio/upload',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      ...config
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}