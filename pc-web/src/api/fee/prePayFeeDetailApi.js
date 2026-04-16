import request from '@/utils/request'

// 获取预付费用列表
export function getPrePayFeeList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.listPrePayFee',
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

// 修改预付费用扣款方式
export function modifyPrePayFeeWay(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.modifyPrePayFeeWay',
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