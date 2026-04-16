<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailMonthFeeDemo.openComponent') }}</el-button>
    <fee-detail-month-fee ref="feeDetailMonthFee"></fee-detail-month-fee>
  </div>
</template>

<script>
import FeeDetailMonthFee from './feeDetailMonthFee'

export default {
  name: 'FeeDetailMonthFeeDemo',
  components: {
    FeeDetailMonthFee
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailMonthFee.open({
        feeId: '123'
      })
    }
  }
}
</script>