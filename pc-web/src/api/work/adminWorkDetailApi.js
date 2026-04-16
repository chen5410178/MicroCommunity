import request from '@/utils/request'

// 获取工作单详情
export function getWorkDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listAdminWorkPool',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get work detail'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取工作单任务列表
export function listAdminWorkTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listAdminWorkTask',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get work tasks'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取工作单任务项列表
export function listAdminWorkTaskItem(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/task.listAdminWorkTaskItem',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get work task items'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取工作单抄送人列表
export function listAdminWorkCopy(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listAdminWorkCopy',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get work copy list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取工作单事件列表
export function listAdminWorkEvent(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workEvent.listAdminWorkEvent',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get work events'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取工作单文件列表
export function listAdminWorkPoolFile(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listAdminWorkPoolFile',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get work files'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取工作单类型列表
export function listAdminWorkType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workType.listAdminWorkType',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get work types'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取工作单周期列表
export function listAdminWorkCycle(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workCycle.listAdminWorkCycle',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get work cycles'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}