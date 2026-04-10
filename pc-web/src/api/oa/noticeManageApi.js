import request from '@/utils/request'

/**
 * 获取公告列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listNotices(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/notice.listNotices',
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
 * 删除公告
 * @param {Object} data 删除参数
 * @returns {Promise}
 */
export function deleteNotice(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/notice.deleteNotice',
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
 * 获取字典数据
 * @param {String} dictType 字典类型
 * @param {String} dictName 字典名称
 * @returns {Promise}
 */
export function getDict(dictType, dictName) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.getDict',
      method: 'get',
      params: {
        dictType,
        dictName
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}