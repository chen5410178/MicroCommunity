<template>
    <div >
      
        <div v-if="storeInfo.storeTypeCd == '800900000000'">
            <index-dev></index-dev>
        </div>
        <div v-if="storeInfo.storeTypeCd == '800900000001'">
            <index-admin></index-admin>
        </div>
        <div v-if="storeInfo.storeTypeCd == '800900000003'">
            <index-property></index-property>
        </div>
    </div>
  </template>
  
  <script>
  import {getStoreInfo} from "@/api/user/indexApi"
  import {deepCopy} from "@/utils/vc"

  // 首页按门店类型异步加载，未命中的首页组件（及其 echarts 依赖）不会下载
  const asyncIndexLoading = {
    name: 'asyncIndexLoading',
    render: function (h) {
      return h('div', {
        style: 'padding: 60px 0; text-align: center; color: #909399; font-size: 13px;'
      }, [
        h('i', { class: 'el-icon-loading' })
      ])
    }
  }

  const asyncIndexComponent = function (loader) {
    return function () {
      return {
        component: loader(),
        loading: asyncIndexLoading,
        delay: 200,
        timeout: 30000
      }
    }
  }
  
  export default {
    name: 'index',
    components: {
        indexDev: asyncIndexComponent(function () { return import('@/components/index/index-dev.vue') }),
        indexAdmin: asyncIndexComponent(function () { return import('@/components/index/index-admin.vue') }),
        indexProperty: asyncIndexComponent(function () { return import('@/components/index/index-property.vue') })
    },
    data() {
      return {
         storeInfo:{
            storeTypeCd:'',
            storeId:''
         }
      }
    },
    created() {
        this._loadStoreInfo()
    },
    methods: {
      async _loadStoreInfo(){
        this.loading = true
          try {
            const res = await getStoreInfo()
            deepCopy(res,this.storeInfo);
          } catch (error) {
            console.error('登录失败：', error)
          } finally {
            this.loading = false
          }
        
      }
    }
  }
  </script>
  
  <style scoped>
  </style> 
