<template>
  <div class="padding">

    <!-- 业主信息 -->
    <view-owner-info ref="viewOwnerInfo" :show-call-back-button="true" @notify="handleNotify" @callBackListOwner="goBack"/>


    <!-- 业主房屋信息 -->
    <show-owner-room :deleteOwnerRoomFlag="deleteOwnerRoomFlag" :ownerId="ownerId" :ownerName="ownerName" />

  </div>
</template>

<script>
import ViewOwnerInfo from '@/components/owner/viewOwnerInfo'
import ShowOwnerRoom from '@/components/owner/showOwnerRoom'

export default {
  name: 'DeleteOwnerRoomList',
  components: {
    ViewOwnerInfo,
    ShowOwnerRoom
  },
  data() {
    return {
      ownerId: '',
      ownerName: '',
      deleteOwnerRoomFlag: true
    }
  },
  created() {
    this.ownerId = this.$route.query.ownerId
  },
  methods: {
    handleNotify(ownerInfo) {
      // 处理返回通知
      console.log('Received notify:', ownerInfo)
      // 这里可以添加其他处理逻辑
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}
</style>