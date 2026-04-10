import request from '@/utils/request'

/**
 * 购买月卡订单
 * @param {Object} data 订单数据
 * @returns {Promise}
 */
export function buyCarMonthOrder(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/carMonth.buyCarMonthOrder',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '购买月卡失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 查询车主车辆
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function queryOwnerCars(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryOwnerCars',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询车主车辆失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 获取月卡列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getOpenApi(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取月卡列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}