import request from '@/utils/request'

// 查询合同变更计划
export function queryContractChangePlan(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractChangePlan',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询合同变更计划失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}