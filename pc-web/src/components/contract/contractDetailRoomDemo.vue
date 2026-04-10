<template>
  <div>
    <contract-detail-room ref="contractDetailRoom"></contract-detail-room>
    <el-button @click="switchContract">Switch Contract</el-button>
  </div>
</template>

<script>
import ContractDetailRoom from '@/components/contract/contractDetailRoom'

export default {
  components: {
    ContractDetailRoom
  },
  methods: {
    switchContract() {
      this.$refs.contractDetailRoom._loadContractDetailRoomData({
        contractId: '12345'
      })
    }
  }
}
</script>