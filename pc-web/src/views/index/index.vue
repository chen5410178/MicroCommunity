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
  import indexDev from "@/components/index/index-dev.vue"
  import indexAdmin from "@/components/index/index-admin.vue"
  import indexProperty from '@/components/index/index-property.vue'
  
  export default {
    name: 'index',
    components: {
        indexDev,
        indexAdmin,
        indexProperty
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