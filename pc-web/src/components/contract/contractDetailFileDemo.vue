<template>
  <div>
    <contract-detail-file ref="contractDetailFile"></contract-detail-file>
    <el-button @click="switchContract">Switch Contract</el-button>
  </div>
</template>

<script>
import ContractDetailFile from '@/components/contract/contractDetailFile'

export default {
  components: {
    ContractDetailFile
  },
  methods: {
    switchContract() {
      this.$refs.contractDetailFile._loadContractDetailFileData({
        contractId: '12345'
      })
    }
  }
}
</script>