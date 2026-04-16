import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取记事本列表
export function listNotepad(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/notepad.listNotepad',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新记事本
export function updateNotepad(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/notepad.updateNotepad',
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

// 删除记事本
export function deleteNotepad(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/notepad.deleteNotepad',
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

// 保存记事本详情
export function saveNotepadDetail(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/notepad.saveNotepadDetail',
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

// 获取记事本详情列表
export function listNotepadDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/notepad.listNotepadDetail',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除记事本详情
export function deleteNotepadDetail(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/notepad.deleteNotepadDetail',
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

// 保存业主报修信息
export function saveOwnerRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.saveOwnerRepair',
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

// 获取报修设置列表
export function listRepairSettings(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/repair.listRepairSettings',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}