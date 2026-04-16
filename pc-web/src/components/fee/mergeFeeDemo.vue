<template>
  <div>
    <el-button @click="openComponent">{{ $t('mergeFeeDemo.openComponent') }}</el-button>
    <merge-fee ref="mergeFee" @merge-success="handleMergeSuccess"></merge-fee>
  </div>
</template>

<script>
import MergeFee from './mergeFee'

export default {
  name: 'MergeFeeDemo',
  components: {
    MergeFee
  },
  methods: {
    openComponent() {
      this.$refs.mergeFee.open({
        feeId: '123'
      })
    },
    handleMergeSuccess() {
      this.$message.success(this.$t('common.operationSuccess'))
    }
  }
}
</script>