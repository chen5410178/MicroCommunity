import request from '@/utils/request'

// 获取运营分析数据
export function getCommunityOperationalAnalysis(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/admin.getCommunityOperationalAnalysis',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取运营分析数据失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取管理员小区列表
export function listAdminCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listAdminCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取小区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}