<template>
  <div>
    <contract-detail-his-fee ref="contractDetailHisFee"></contract-detail-his-fee>
    <el-button @click="switchContract">Switch Contract</el-button>
  </div>
</template>

<script>
import ContractDetailHisFee from '@/components/contract/contractDetailHisFee'

export default {
  components: {
    ContractDetailHisFee
  },
  methods: {
    switchContract() {
      this.$refs.contractDetailHisFee._listContractDetailHisFeeDetails({
        contractId: '12345'
      })
    }
  }
}
</script>