import request from '@/utils/request'

/**
 * 获取保养任务列表
 * @param {Object} params 查询参数
 * @returns {Promise} 
 */
export function listMaintainanceTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainanceTask.listMaintainanceTask',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total,
        records: res.records
      })
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 获取保养任务详情列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listMaintainanceTaskDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainanceTask.listMaintainanceTaskDetail',
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

/**
 * 更新保养任务信息
 * @param {Object} data 任务数据
 * @returns {Promise}
 */
export function updateMaintainanceTask(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainanceTask.updateMaintainanceTask',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 删除保养任务
 * @param {Object} data 任务数据
 * @returns {Promise}
 */
export function deleteMaintainanceTask(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainanceTask.deleteMaintainanceTask',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
