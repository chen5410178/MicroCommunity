import request from '@/utils/request'

// 获取定时任务列表
export function getTaskList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/task.listTasks',
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

// 添加定时任务
export function addTask(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/task.saveTask',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新定时任务
export function updateTask(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/task.updateTask',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除定时任务
export function deleteTask(taskId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/task.deleteTask',
      method: 'post',
      data: { taskId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function startTask(taskId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/task.startTask',
      method: 'post',
      data: { taskId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function stopTask(taskId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/task.stopTask',
      method: 'post',
      data: { taskId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
export function runTask(taskId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/job.runJob',
      method: 'post',
      data: { taskId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取定时任务列表
export function getTaskTemplateList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/task.listTaskTemplate',
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
