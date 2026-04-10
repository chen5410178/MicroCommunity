import request from '@/utils/request'

export function listAdminMachineTranslates(params) {
  return new Promise((resolve, reject) => {
    return request({
    url: '/machineTranslate.listAdminMachineTranslates',
        method: 'get',
    params
  }).then(res => {
      resolve(res.data)
    }).catch(err => {
      reject(err)
    })
  })
}