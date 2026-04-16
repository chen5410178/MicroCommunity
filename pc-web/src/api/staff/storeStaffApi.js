import request from '@/utils/request'

// 查询组织树
export function queryAdminOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.queryAdminOrgTree',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get org tree'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询物业员工
export function queryAdminPropertyStaff(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.queryAdminPropertyStaff',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
        resolve({
          data: res.staffs,
          total: res.total
        })
    }).catch(error => {
      reject(error)
    })
  })
}