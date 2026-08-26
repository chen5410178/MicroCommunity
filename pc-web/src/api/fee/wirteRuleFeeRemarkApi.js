import request from '@/utils/request'

// 修改规则费用备注
export function modifyRuleFeeRemark(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.modifyRuleFeeRemark',
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