import request from '@/utils/request'

/**
 * 查询积分赠送明细列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listIntegralGiftDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.listIntegralGiftDetail',
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
 * 添加积分赠送明细
 * @param {Object} data 添加数据
 * @returns {Promise}
 */
export function addIntegralGiftDetail(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.saveIntegralGiftDetail',
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

/**
 * 删除积分赠送明细
 * @param {String} detailId 明细ID
 * @returns {Promise}
 */
export function deleteIntegralGiftDetail(detailId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/integral.deleteIntegralGiftDetail',
      method: 'post',
      data: { detailId }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}