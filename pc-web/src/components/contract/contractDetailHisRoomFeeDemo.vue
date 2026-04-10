<template>
  <div>
    <contract-detail-his-room-fee ref="contractDetailHisRoomFee"></contract-detail-his-room-fee>
    <el-button @click="switchContract">Switch Contract</el-button>
  </div>
</template>

<script>
import ContractDetailHisRoomFee from '@/components/contract/contractDetailHisRoomFee'

export default {
  components: {
    ContractDetailHisRoomFee
  },
  methods: {
    switchContract() {
      this.$refs.contractDetailHisRoomFee.switch({
        ownerId: '12345'
      })
    }
  }
}
</script>