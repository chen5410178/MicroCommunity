import request from '@/utils/request'

/**
 * 获取费用详情
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function getFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listFee',
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
 * 获取缴费记录列表
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function getPaymentRecords(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listPaymentRecords',
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
 * 获取按月展示数据
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function getMonthlyFeeData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listMonthlyFee',
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
 * 获取关联房屋数据
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function getRelatedRooms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listRelatedRooms',
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
 * 获取关联车辆数据
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function getRelatedCars(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listRelatedCars',
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