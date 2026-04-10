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
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}