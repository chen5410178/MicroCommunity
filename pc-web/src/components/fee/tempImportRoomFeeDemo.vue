<template>
  <div>
    <el-button @click="openModal">{{$t('tempImportRoomFeeDemo.openModal')}}</el-button>
    <temp-import-room-fee ref="tempImportRoomFee"></temp-import-room-fee>
  </div>
</template>

<script>
import TempImportRoomFee from './tempImportRoomFee'

export default {
  name: 'TempImportRoomFeeDemo',
  components: {
    TempImportRoomFee
  },
  methods: {
    openModal() {
      this.$refs.tempImportRoomFee.open({
        roomId: '123',
        roomName: '测试房间',
        ownerName: '测试业主'
      })
    }
  }
}
</script>