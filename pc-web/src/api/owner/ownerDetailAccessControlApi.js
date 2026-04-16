import request from '@/utils/request'

export function listMachineTranslates(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/machineTranslate.listMachineTranslates',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取门禁同步记录失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}