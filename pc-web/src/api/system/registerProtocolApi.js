import request from '@/utils/request'

// 获取注册协议列表
export function listRegisterProtocols(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/system.listRegisterProtocol',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取注册协议列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新注册协议
export function updateRegisterProtocol(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/system.updateRegisterProtocol',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新注册协议失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}