<template>
  <div>
    <el-button @click="switchData">{{ $t('feeDetailRuleBillDemo.switchData') }}</el-button>
    <fee-detail-rule-bill ref="feeDetailRuleBill"></fee-detail-rule-bill>
  </div>
</template>

<script>
import FeeDetailRuleBill from './feeDetailRuleBill'

export default {
  name: 'FeeDetailRuleBillDemo',
  components: {
    FeeDetailRuleBill
  },
  methods: {
    switchData() {
      this.$refs.feeDetailRuleBill.loadData({
        feeId: '123',
        roomId: '456'
      })
    }
  }
}
</script>