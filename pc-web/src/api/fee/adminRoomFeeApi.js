import request from '@/utils/request'


export function queryCommunityUnitTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.queryCommunityUnitTree',
      method: 'get',
      params: { ...params, hc: 1.8 }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询单元树失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function  queryAdminRoom(params) {
    return new Promise((resolve, reject) => {
      request({
        url: '/room.queryAdminRoom',
        method: 'get',
        params
      }).then(response => {
        const res = response.data
        if (res.code === 0) {
          resolve(res)
        } else {
          reject(new Error(res.msg || '查询房屋失败'))
        }
      }).catch(error => {
        reject(error)
      })
    })
  }


  export function  queryAdminRoomsTree(params) {
    return new Promise((resolve, reject) => {
      request({
        url: '/room.queryAdminRoomsTree',
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

  export function   listAdminFee(params) {
    return new Promise((resolve, reject) => {
      request({
        url: '/fee.listAdminFee',
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

  export function  queryAdminFeeDetail(params) {
    return new Promise((resolve, reject) => {
      request({
        url: '/fee.queryAdminFeeDetail',
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

  export function  queryAdminOwnerRooms(params) {
    return new Promise((resolve, reject) => {
      request({
        url: '/room.queryAdminOwnerRooms',
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

  export function  queryAdminRoomOwner(params) {
    return new Promise((resolve, reject) => {
      request({
        url: '/owner.queryAdminRoomOwner',
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

  export function  listAdminMeterWaters(params) {
    return new Promise((resolve, reject) => {
      request({
        url: '/meterWater.listAdminMeterWaters',
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

  export function  queryAdminFeeReceipt(params) {
    return new Promise((resolve, reject) => {
      request({
        url: '/receipt.queryAdminFeeReceipt',
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

  export function  queryAdminOwnerAccount(params) {
    return new Promise((resolve, reject) => {
      request({
        url: '/account.queryAdminOwnerAccount',
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

  export function  listAdminAccountReceipt(params) {
    return new Promise((resolve, reject) => {
      request({
        url: '/receipt.listAdminAccountReceipt',
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

  export function  listAdminOweFeeCallable(params) {
    return new Promise((resolve, reject) => {
      request({
        url: '/oweFeeCallable.listAdminOweFeeCallable',
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

  export function  queryAdminFeeDeposit(params) {
    return new Promise((resolve, reject) => {
      request({
        url: '/fee.queryAdminFeeDeposit',
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
  // 获取费用详情
  export function  getFeeDetail(feeId) {
    return new Promise((resolve, reject) => {
      request({
        url: '/fee.getAdminFeeDetail',
        method: 'get',
        params: { feeId }
      }).then(response => {
        const res = response.data

          resolve(res)

      }).catch(error => {
        reject(error)
      })
    })
  }

  // 获取房间详情
  export function  getRoomDetail(roomId) {
    return new Promise((resolve, reject) => {
      request({
        url: '/room.getAdminRoomDetail',
        method: 'get',
        params: { roomId }
      }).then(response => {
        const res = response.data

          resolve(res)

      }).catch(error => {
        reject(error)
      })
    })
  }

  // 获取业主详情
  export function  getOwnerDetail(ownerId) {
    return new Promise((resolve, reject) => {
      request({
        url: '/owner.getAdminOwnerDetail',
        method: 'get',
        params: { ownerId }
      }).then(response => {
        const res = response.data

          resolve(res)

      }).catch(error => {
        reject(error)
      })
    })
  }
