import request from '@/utils/request'

// 获取项目列表
export function getAppApiList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/hcAppApi.listHcAppApi',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(response)
      } else {
        reject(new Error(res.msg || '获取项目列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加项目
export function addAppApi(data) {


  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/hcAppApi.saveHcAppApi',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}



// 删除项目
export function deleteAppApi(aaId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/hcAppApi.deleteHcAppApi',
      method: 'post',
      data: { aaId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
} 