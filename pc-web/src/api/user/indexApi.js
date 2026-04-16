import request from '@/utils/request'


export function getStoreInfo() {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.store.byuser',
      method: 'get'
    }).then(response => {
      const res = response.data
      // 获取验证码成功
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

export function queryDevCount() {
  return new Promise((resolve, reject) => {
    request({
      url: '/admin.queryDevCount',
      method: 'get'
    }).then(response => {
      const res = response.data
      // 获取验证码成功
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

export function queryAppServiceData(){
  return new Promise((resolve, reject) => {
    request({
      url: '/admin.queryAppServiceData',
      method: 'get'
    }).then(response => {
      const res = response.data
      // 获取验证码成功
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}