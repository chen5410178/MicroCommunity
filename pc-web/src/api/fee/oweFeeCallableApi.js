import request from '@/utils/request'

/**
 * 获取欠费催缴列表
 * @param {Object} params 查询参数
 * @returns {Promise} 
 */
export function listOweFeeCallable(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oweFeeCallable.listOweFeeCallable',
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
 * 登记欠费催缴
 * @param {Object} data 登记数据
 * @returns {Promise}
 */
export function writeOweFeeCallable(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oweFeeCallable.writeOweFeeCallable',
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
 * 删除欠费催缴记录
 * @param {Object} data 删除数据
 * @returns {Promise}
 */
export function deleteOweFeeCallable(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oweFeeCallable.deleteOweFeeCallable',
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
 * 获取欠费催缴详情
 * @param {String} ofcId 催缴ID
 * @returns {Promise}
 */
export function getOweFeeCallableDetail(ofcId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oweFeeCallable.getOweFeeCallable',
      method: 'get',
      params: { ofcId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 更新欠费催缴状态
 * @param {Object} data 更新数据
 * @returns {Promise}
 */
export function updateOweFeeCallableState(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oweFeeCallable.updateOweFeeCallableState',
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