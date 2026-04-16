export function getUserId() {
    const userInfo = JSON.parse(localStorage.getItem('user'))

    return userInfo.userId
}

export function getUserName() {
    const userInfo = JSON.parse(localStorage.getItem('user'))

    return userInfo.name
}

export function getUserTel() {
    const userInfo = JSON.parse(localStorage.getItem('user'))

    return userInfo.tel
}