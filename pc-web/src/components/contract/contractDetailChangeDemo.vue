<template>
  <div>
    <contract-detail-change ref="contractDetailChange"></contract-detail-change>
    <el-button @click="switchContract">Switch Contract</el-button>
  </div>
</template>

<script>
import ContractDetailChange from '@/components/contract/contractDetailChange'

export default {
  components: {
    ContractDetailChange
  },
  methods: {
    switchContract() {
      this.$refs.contractDetailChange.switch({
        contractId: '12345',
        logStartTime: '2023-01-01',
        logEndTime: '2023-12-31',
        contractCode: 'CONTRACT001'
      })
    }
  }
}
</script>