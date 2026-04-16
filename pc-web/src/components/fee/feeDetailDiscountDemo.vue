<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailDiscountDemo.openComponent') }}</el-button>
    <fee-detail-discount ref="feeDetailDiscount"></fee-detail-discount>
  </div>
</template>

<script>
import FeeDetailDiscount from './feeDetailDiscount'

export default {
  name: 'FeeDetailDiscountDemo',
  components: {
    FeeDetailDiscount
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailDiscount.open({
        feeId: '123'
      })
    }
  }
}
</script>