import request from '@/utils/request'

// 保存业主发票信息
export function saveOwnerInvoice(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/invoice.saveOwnerInvoice',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}