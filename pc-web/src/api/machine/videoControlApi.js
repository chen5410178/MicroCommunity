import request from '@/utils/request'

// 获取监控区域列表
export function getMonitorAreas(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params: {
        ...params,
        iotApiCode: 'listMonitorAreaBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取监控设备列表
export function getMonitorMachines(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params: {
        ...params,
        iotApiCode: 'listMonitorMachineBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取视频播放地址
export function getPlayVideoUrl(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params: {
        ...params,
        iotApiCode: 'getPlayVideoUrlBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}