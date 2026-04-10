<template>
  <div>
    <contract-detail-sub ref="contractDetailSub"></contract-detail-sub>
    <el-button @click="switchContract">Switch Contract</el-button>
  </div>
</template>

<script>
import ContractDetailSub from '@/components/contract/contractDetailSub'

export default {
  components: {
    ContractDetailSub
  },
  methods: {
    switchContract() {
      this.$refs.contractDetailSub.switch({
        contractId: '12345'
      })
    }
  }
}
</script>