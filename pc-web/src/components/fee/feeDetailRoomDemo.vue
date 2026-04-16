<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailRoomDemo.openComponent') }}</el-button>
    <fee-detail-room ref="feeDetailRoom"></fee-detail-room>
  </div>
</template>

<script>
import FeeDetailRoom from './feeDetailRoom'

export default {
  name: 'FeeDetailRoomDemo',
  components: {
    FeeDetailRoom
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailRoom.open({
        payerObjId: '123'
      })
    }
  }
}
</script>