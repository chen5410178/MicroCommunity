<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailHisFeeDemo.openComponent') }}</el-button>
    <fee-detail-his-fee ref="feeDetailHisFee"></fee-detail-his-fee>
  </div>
</template>

<script>
import FeeDetailHisFee from './feeDetailHisFee'

export default {
  name: 'FeeDetailHisFeeDemo',
  components: {
    FeeDetailHisFee
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailHisFee.open({
        feeId: '123'
      })
    }
  }
}
</script>