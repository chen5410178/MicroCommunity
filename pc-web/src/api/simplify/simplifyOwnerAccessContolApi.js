import request from '@/utils/request'

export function listMachineTranslates(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/machineTranslate.listMachineTranslates',
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