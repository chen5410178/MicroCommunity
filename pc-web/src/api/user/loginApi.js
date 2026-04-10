import request from '@/utils/request'

// 登录接口
export function login(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/login.pcUserLogin',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        // 可以在这里添加其他登录成功后的处理
        resolve(res)
      } else {
        reject(res.msg || '登录失败')
      }
    }).catch(error => {
      reject(error.response.data)
    })
  })
}

// 获取验证码接口
export function getCaptcha() {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/validate-code/generateValidateCode',
      method: 'get'
    }).then(response => {
      const res = response.data
      // 获取验证码成功
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

export function updatePassword(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/user.changeStaffPwd',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新密码失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function _loadStaffPrivileges() {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.user.privilege',
      method: 'get'
    }).then(response => {
      const res = response.data

      let _privilege = [];
      res.datas.forEach(item => {
        _privilege.push(item.pId);
      });

      localStorage.setItem('hc_staff_privilege', _privilege);
      // 获取验证码成功
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}
