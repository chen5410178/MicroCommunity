import request from '@/utils/request'

// 获取MQTT日志列表
export function listMqttLog(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/log.listMqttLog',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}
