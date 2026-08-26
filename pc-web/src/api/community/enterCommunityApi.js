import request from '@/utils/request'

// 查询入驻小区列表
export function listMyCommunity(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/communitys/queryStoreCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新小区信息
export function updateCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.updateCommunity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}