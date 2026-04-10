import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 保存考核员工信息
export function saveExamineStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/examine.saveExamineStaff',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取考核项目列表
export function listExamineProject(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/examine.listExamineProject',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取组织树
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询员工信息
export function queryStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传图片
export function uploadImage(data) {
  return new Promise((resolve, reject) => {
    const formData = new FormData()
    formData.append('uploadFile', data)
    formData.append('communityId', getCommunityId())

    request({
      url: '/uploadFile/uploadImage',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}