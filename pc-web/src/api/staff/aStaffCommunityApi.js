import request from '@/utils/request'

// 获取员工列表
export function getStaffInfos(params) {
  return request({
    url: '/query.staff.infos',
    method: 'get',
    params
  })
}

// 获取员工关联的小区列表
export function listAStaffCommunity(params) {
  return request({
    url: '/role.listAStaffCommunity',
    method: 'get',
    params
  })
}

// 获取待关联的小区列表
export function listWaitAStaffCommunity(params) {
  return request({
    url: '/role.listWaitAStaffCommunity',
    method: 'get',
    params
  })
}

// 保存员工小区关联
export function saveStaffCommunity(data) {
  return request({
    url: '/role.saveStaffCommunity',
    method: 'post',
    data
  })
}

// 删除员工小区关联
export function deleteStaffCommunity(data) {
  return request({
    url: '/role.deleteStaffCommunity',
    method: 'post',
    data
  })
}

// 获取组织树
export function listOrgTree(params) {
  return request({
    url: '/org.listOrgTree',
    method: 'get',
    params
  })
}