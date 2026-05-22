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
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || '获取MQTT日志列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
