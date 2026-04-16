import request from '@/utils/request'

// 获取问题答案列表
export function listQuestionAnswer(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.listQuestionAnswer',
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

// 获取问题标题列表
export function listQuestionTitle(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.listQuestionTitle',
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