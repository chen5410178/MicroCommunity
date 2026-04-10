import request from '@/utils/request'
import config from '@/conf/config'

// 获取项目列表
export function getProjectList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: config.url.projectList,
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取项目列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加项目
export function addProject(data) {
  return new Promise((resolve, reject) => {
    request({
      url: config.url.projectAdd,
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新项目
export function updateProject(data) {
  return new Promise((resolve, reject) => {
    request({
      url: config.url.projectUpdate,
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除项目
export function deleteProject(projectId) {
  return new Promise((resolve, reject) => {
    request({
      url: config.url.projectDelete,
      method: 'post',
      data: { projectId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
} 