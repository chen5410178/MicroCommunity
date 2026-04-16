import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
//import 'formiojs/dist/formio.full.min.css'
import i18n from './i18n'
import {getCommunityName,getCommunityId} from '@/api/community/communityApi'

// 验证全局脚本是否正确加载

Vue.prototype.getCommunityId = function(){
  return getCommunityId()
}
Vue.prototype.getCommunityName = function(){
  return getCommunityName()
}

Vue.prototype.hasPrivilege = function(_privaleges) {
  // 确保 _privaleges 是数组，如果不是则转换为数组
  const privilegesToCheck = Array.isArray(_privaleges) ? _privaleges : [_privaleges];
  
  // 从本地存储获取用户权限
  let userPrivileges = localStorage.getItem('hc_staff_privilege');
  
  // 如果存在用户权限，则转换为数组
  if (userPrivileges) {
    userPrivileges = userPrivileges.split(',');
  } else {
    // 如果没有权限数据，直接返回 false
    return false;
  }
  
  // 检查用户是否有任一所需权限
  return privilegesToCheck.some(item => userPrivileges.includes(item));
}
Vue.prototype.toDoc = function(url) {
  window.open('http://www.homecommunity.cn/'+url, '_blank')
}

Vue.prototype.$getUser = function() {
  let user = JSON.parse(localStorage.getItem('user'))
  return user
}


Vue.use(ElementUI)
Vue.config.productionTip = false

new Vue({
  router,
  i18n,
  render: h => h(App)
}).$mount('#app') 