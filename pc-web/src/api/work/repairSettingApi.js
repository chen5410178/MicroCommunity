import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取报修设置列表
export function listRepairSettings(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/repair.listRepairSettings',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取报修设置列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加报修设置
export function saveRepairSetting(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/repair.saveRepairSetting',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加报修设置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新报修设置
export function updateRepairSetting(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/repair.updateRepairSetting',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新报修设置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除报修设置
export function deleteRepairSetting(settingId) {
  return new Promise((resolve, reject) => {
    const data = {
      settingId,
      communityId: getCommunityId()
    }
    request({
      url: '/repair.deleteRepairSetting',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除报修设置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取字典数据
export function getDict(dictType, state) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.getDict',
      method: 'get',
      params: {
        dictType,
        state,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取字典数据失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}