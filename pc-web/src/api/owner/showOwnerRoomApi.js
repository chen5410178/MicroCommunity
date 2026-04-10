import request from '@/utils/request'

// 查询业主房间信息
export function queryRoomsByOwner(params) {
    return new Promise((resolve, reject) => {
        request({
            url: '/room.queryRoomsByOwner',
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

// 业主退房
export function exitRoom(data) {
    return new Promise((resolve, reject) => {
        request({
            url: '/room.exitRoom',
            method: 'post',
            data
        }).then(response => {
            const res = response.data
            if (res.code === 0) {
                resolve(res)
            } else {
                reject(new Error(res.msg || '退房操作失败'))
            }
        }).catch(error => {
            reject(error)
        })
    })
}