<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailReceiptDemo.openComponent') }}</el-button>
    <fee-detail-receipt ref="feeDetailReceipt"></fee-detail-receipt>
  </div>
</template>

<script>
import FeeDetailReceipt from './feeDetailReceipt'

export default {
  name: 'FeeDetailReceiptDemo',
  components: {
    FeeDetailReceipt
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailReceipt.open({
        feeId: '123'
      })
    }
  }
}
</script>