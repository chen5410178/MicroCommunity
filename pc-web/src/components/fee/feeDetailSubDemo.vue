<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailSubDemo.openComponent') }}</el-button>
    <fee-detail-sub ref="feeDetailSub"></fee-detail-sub>
  </div>
</template>

<script>
import FeeDetailSub from './feeDetailSub'

export default {
  name: 'FeeDetailSubDemo',
  components: {
    FeeDetailSub
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailSub.open({
        feeId: '123'
      })
    }
  }
}
</script>