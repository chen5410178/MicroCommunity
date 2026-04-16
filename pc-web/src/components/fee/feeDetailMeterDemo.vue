<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailMeterDemo.openComponent') }}</el-button>
    <fee-detail-meter ref="feeDetailMeter"></fee-detail-meter>
  </div>
</template>

<script>
import FeeDetailMeter from './feeDetailMeter'

export default {
  name: 'FeeDetailMeterDemo',
  components: {
    FeeDetailMeter
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailMeter.open({
        feeId: '123'
      })
    }
  }
}
</script>