import request from '@/utils/request'

/**
 * 获取工单扣款列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listWorkDeduction(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listWorkDeduction',
      method: 'get',
      params
    })
      .then(response => {
        const res = response.data
        resolve(res)
      })
      .catch(error => {
        reject(error)
      })
  })
}

/**
 * 添加工单扣款
 * @param {Object} data 扣款数据
 * @returns {Promise}
 */
export function addWorkDeduction(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.saveWorkDeduction',
      method: 'post',
      data
    })
      .then(response => {
        const res = response.data
        resolve(res)
      })
      .catch(error => {
        reject(error)
      })
  })
}

/**
 * 更新工单扣款
 * @param {Object} data 扣款数据
 * @returns {Promise}
 */
export function updateWorkDeduction(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.updateWorkDeduction',
      method: 'post',
      data
    })
      .then(response => {
        const res = response.data
        resolve(res)
      })
      .catch(error => {
        reject(error)
      })
  })
}

/**
 * 删除工单扣款
 * @param {String} workId 工单ID
 * @returns {Promise}
 */
export function deleteWorkDeduction(workId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.deleteWorkDeduction',
      method: 'post',
      data: { workId }
    })
      .then(response => {
        const res = response.data
        resolve(res)
      })
      .catch(error => {
        reject(error)
      })
  })
}