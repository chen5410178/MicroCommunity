<template>
  <div>
    <contract-detail-flow ref="contractDetailFlow"></contract-detail-flow>
    <el-button @click="switchContract">Switch Contract</el-button>
  </div>
</template>

<script>
import ContractDetailFlow from '@/components/contract/contractDetailFlow'

export default {
  components: {
    ContractDetailFlow
  },
  methods: {
    switchContract() {
      this.$refs.contractDetailFlow.switch({
        contractId: '12345'
      })
    }
  }
}
</script>