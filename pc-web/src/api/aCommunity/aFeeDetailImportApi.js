import request from '@/utils/request'

export function queryAdminImportFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryAdminImportFeeDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取导入费用明细数据失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}