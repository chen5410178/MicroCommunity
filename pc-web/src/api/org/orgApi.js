import request from '@/utils/request'

// 获取组织树
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取组织树失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 获取组织列表
 * @param {Object} params
 * @param {number} [params.pageIndex=1]
 * @param {number} [params.pageSize=10]
 * @param {string} [params.orgName]
 * @param {number} [params.orgType]
 * @param {string} [params.parentId]
 * @param {string} [params.sortField]
 * @param {string} [params.sortOrder]
 * @return {Promise}
 */
export function listOrgs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存组织
export function saveOrg(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.saveOrg',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存组织失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新组织
export function updateOrg(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.updateOrg',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新组织失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除组织
export function deleteOrg(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.deleteOrg',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(res.msg)
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取员工列表
export function listStaffs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
        resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}

// 获取未分配组织的员工列表
export function listStaffsNoInOrg(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.listStaffsNoInOrg',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取未分配组织的员工列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存员工与组织关系
export function saveStaffOrgRel(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.saveStaffOrgRel',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存员工与组织关系失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除员工与组织关系
export function deleteOrgRelStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.deleteOrgRelStaff',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(res.msg)
      }
    }).catch(error => {
      reject(error)
    })
  })
}