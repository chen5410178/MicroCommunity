<template>
  <div>
    <contract-detail-receipt ref="contractDetailReceipt"></contract-detail-receipt>
    <el-button @click="switchOwner">Switch Owner</el-button>
  </div>
</template>

<script>
import ContractDetailReceipt from '@/components/contract/contractDetailReceipt'

export default {
  components: {
    ContractDetailReceipt
  },
  methods: {
    switchOwner() {
      this.$refs.contractDetailReceipt.switch({
        ownerId: '12345'
      })
    }
  }
}
</script>