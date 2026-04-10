<template>
  <div>
    <el-button @click="openModal">{{ $t('roomCreateFeeAddDemo.openModal') }}</el-button>
    <room-create-fee-add ref="roomCreateFeeAdd" @success="handleSuccess"></room-create-fee-add>
  </div>
</template>

<script>
import RoomCreateFeeAdd from './roomCreateFeeAdd'

export default {
  name: 'RoomCreateFeeAddDemo',
  components: {
    RoomCreateFeeAdd
  },
  methods: {
    openModal() {
      this.$refs.roomCreateFeeAdd.open({
        roomId: '123',
        floorNum: '1',
        unitNum: '2',
        roomNum: '101',
        ownerName: 'Test Owner',
        roomType: '2020601'
      })
    },
    handleSuccess() {
      this.$message.success(this.$t('common.operationSuccess'))
    }
  }
}
</script>