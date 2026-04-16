import request from '@/utils/request'

export function listComplaints(params) {
    return request({
      url: '/complaint/listComplaints',
      method: 'get',
      params
    })
  }