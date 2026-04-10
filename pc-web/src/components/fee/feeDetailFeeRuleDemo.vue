<template>
  <div>
    <el-button @click="switchData">{{ $t('feeDetailFeeRuleDemo.switchData') }}</el-button>
    <fee-detail-fee-rule ref="feeDetailFeeRule"></fee-detail-fee-rule>
  </div>
</template>

<script>
import FeeDetailFeeRule from './feeDetailFeeRule'

export default {
  name: 'FeeDetailFeeRuleDemo',
  components: {
    FeeDetailFeeRule
  },
  methods: {
    switchData() {
      this.$refs.feeDetailFeeRule.loadData({
        feeId: '123',
        roomId: '456'
      })
    }
  }
}
</script>