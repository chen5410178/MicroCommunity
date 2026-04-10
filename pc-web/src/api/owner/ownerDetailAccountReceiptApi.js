import request from '@/utils/request'

export function listAccountReceipt(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/receipt.listAccountReceipt',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取账户收据列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}