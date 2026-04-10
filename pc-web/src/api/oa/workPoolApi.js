import request from '@/utils/request'

// 获取工作单池列表
export function listWorkTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.listWorkTask',
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