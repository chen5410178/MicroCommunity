<template>
  <div>
    <contract-detail-owner ref="contractDetailOwner"></contract-detail-owner>
    <el-button @click="switchOwner">Switch Owner</el-button>
  </div>
</template>

<script>
import ContractDetailOwner from '@/components/contract/contractDetailOwner'

export default {
  components: {
    ContractDetailOwner
  },
  methods: {
    switchOwner() {
      this.$refs.contractDetailOwner.switch({
        ownerId: '12345'
      })
    }
  }
}
</script>