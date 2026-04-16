import request from '@/utils/request'

export function listOwnerRepairs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.listOwnerRepairs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询业主维修失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}