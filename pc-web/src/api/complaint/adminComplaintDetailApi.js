import request from '@/utils/request'

// 获取投诉详情
export function getComplaintDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaint.listAdminComplaints',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取投诉详情失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取投诉事件列表
export function listComplaintEvents(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaint.listAdminComplaintEvent',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取投诉事件列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取投诉评价列表
export function listComplaintAppraises(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaintAppraise.listAdminComplaintAppraise',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取投诉评价列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取投诉类型列表
export function listComplaintTypes(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaintType.listAdminComplaintType',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取投诉类型列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}