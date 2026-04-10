import request from '@/utils/request'

// 查询工单列表
export function queryTaskWork(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.queryTaskWork',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询工单详情
export function getWorkInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listWorkPool',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询工单任务明细
export function getWorkTaskItems(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/task.listWorkTaskItem',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 完成抄送工单
export function finishWorkCopy(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.finishWorkCopy',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '操作失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}