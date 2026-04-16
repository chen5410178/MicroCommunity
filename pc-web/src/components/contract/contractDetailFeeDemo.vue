<template>
  <div>
    <contract-detail-fee ref="contractDetailFee"></contract-detail-fee>
    <el-button @click="switchContract">Switch Contract</el-button>
  </div>
</template>

<script>
import ContractDetailFee from '@/components/contract/contractDetailFee'

export default {
  components: {
    ContractDetailFee
  },
  methods: {
    switchContract() {
      this.$refs.contractDetailFee._loadContractDetailFeeData({
        contractId: '12345'
      })
    }
  }
}
</script>