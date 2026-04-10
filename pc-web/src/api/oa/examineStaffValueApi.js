import request from '@/utils/request'

/**
 * 获取员工考核记录列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listExamineStaffValue(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/examine.listExamineStaffValue',
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

/**
 * 添加员工考核记录
 * @param {Object} data 考核数据
 * @returns {Promise}
 */
export function addExamineStaffValue(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/examine.saveExamineStaffValue',
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