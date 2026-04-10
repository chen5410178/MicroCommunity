import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 搜索社区数据
export function searchCommunityData(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/search.searchCommunityData',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取电话机信息
export function getTelMachineInfo() {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params: {
        page: 1,
        row: 1,
        communityId,
        iotApiCode: 'queryTelMachineBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存电话机消息
export function saveTelMachineMsg(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/iot.postOpenApi',
      method: 'post',
      data: {
        ...data,
        communityId,
        iotApiCode: 'telMachineMsgBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}