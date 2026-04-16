import request from '@/utils/request'

export function resyncMachineTranslate(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/machineTranslate.resyncMachineTranslate',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '重新同步失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}