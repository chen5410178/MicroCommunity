import request from '@/utils/request'

export function queryContractRoom(params) {
  return new Promise((resolve, reject) => {
    request({
    url: '/contract/queryContractRoom',
    method: 'get',
    params
  }).then(response => {
    const res = response.data
    if (res.code === 0) {
      resolve(res)
    } else {
      reject(new Error(res.msg || '查询合同信息失败'))
    }
  }).catch(error => {
      reject(error)
    })
  })
}