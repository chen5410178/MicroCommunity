import request from '@/utils/request'

export function queryPointInspectionPlan(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspection.queryPointInspectionPlan',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取巡检点计划失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}