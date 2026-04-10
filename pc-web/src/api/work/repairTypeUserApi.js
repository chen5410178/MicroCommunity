import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取报修师傅列表
export function listRepairTypeUsers(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/repair.listRepairTypeUsers',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存报修师傅
export function saveRepairTypeUser(data) {
  data.communityId = getCommunityId()
  return new Promise((resolve, reject) => {
    request({
      url: '/repair.saveRepairTypeUser',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新报修师傅
export function updateRepairTypeUser(data) {
  data.communityId = getCommunityId()

  return new Promise((resolve, reject) => {
    request({
      url: '/repair.updateRepairTypeUser',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除报修师傅
export function deleteRepairTypeUser(data) {
  data.communityId = getCommunityId()

  return new Promise((resolve, reject) => {
    request({
      url: '/repair.deleteRepairTypeUser',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
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
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取员工信息
export function getStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}