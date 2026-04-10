import request from '@/utils/request'

/**
 * 获取公告详情
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getNoticeDetail(params) {
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