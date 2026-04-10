import request from '@/utils/request'

/**
 * 查询抄送工单列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function queryCopyWork(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.queryCopyWork',
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
 * 提交抄送工单处理结果
 * @param {Object} data 处理数据
 * @returns {Promise}
 */
export function finishWorkCopy(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.finishWorkCopy',
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