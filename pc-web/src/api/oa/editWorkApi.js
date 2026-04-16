import request from '@/utils/request'

// 获取工作单类型列表
export function listWorkType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workType.listWorkType',
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

// 查询工作单详情
export function queryStartWork(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.queryStartWork',
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

// 获取工作单处理人列表
export function listWorkTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listWorkTask',
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

// 获取工作单抄送人列表
export function listWorkCopy(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listWorkCopy',
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

// 获取工作单周期信息
export function listWorkCycle(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workCycle.listWorkCycle',
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

// 更新工作单
export function updateWorkPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.updateWorkPool',
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

// 查询员工信息
export function queryStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
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

// 获取组织树
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
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

// 上传文件
export function uploadFile(data, config) {
  return new Promise((resolve, reject) => {
    request({
      url: '/upload',
      method: 'post',
      data,
      onUploadProgress: config.onUploadProgress,
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

// 上传图片
export function uploadImage(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadImage',
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