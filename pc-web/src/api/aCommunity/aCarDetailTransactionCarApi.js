import request from '@/utils/request'

export function listAdminMachineTranslates(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/machineTranslate.listAdminMachineTranslates',
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