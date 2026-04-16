<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailContractDemo.openComponent') }}</el-button>
    <fee-detail-contract ref="feeDetailContract"></fee-detail-contract>
  </div>
</template>

<script>
import FeeDetailContract from './feeDetailContract'

export default {
  name: 'FeeDetailContractDemo',
  components: {
    FeeDetailContract
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailContract.open({
        payerObjId: '123'
      })
    }
  }
}
</script>