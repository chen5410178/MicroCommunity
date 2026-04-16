import request from '@/utils/request'

export function importTempData(data) {

  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/importRoomFee/importTempData',
      method: 'post',
      data
    }).then(res => {
      resolve(res.data)
    }).catch(err => {
      reject(err)
    })
  })
}
