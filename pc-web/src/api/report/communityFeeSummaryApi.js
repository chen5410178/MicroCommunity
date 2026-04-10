import request from '@/utils/request'

// 获取费用汇总数据
export function getCommunityFeeSummary(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/admin.getCommunityFeeSummary',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取费用汇总数据失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取管理员社区列表
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
        reject(new Error(res.msg || '获取管理员社区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出数据
export function exportCommunityFeeSummary(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
      method: 'get',
      params: {
        ...params,
        pagePath: 'communityFeeSummary'
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '导出费用汇总数据失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}