import request from '@/utils/request'

/**
 * 获取小区设置项列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listCommunitySettingKey(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/communitySettingKey.listCommunitySettingKey',
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
 * 保存小区设置
 * @param {Object} data 保存数据
 * @returns {Promise}
 */
export function saveCommunitySetting(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.saveCommunitySetting',
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