<template>
  <div>
    <el-button @click="openComponent">{{ $t('feeDetailHisDemo.openComponent') }}</el-button>
    <fee-detail-his ref="feeDetailHis"></fee-detail-his>
  </div>
</template>

<script>
import FeeDetailHis from './feeDetailHis'

export default {
  name: 'FeeDetailHisDemo',
  components: {
    FeeDetailHis
  },
  methods: {
    openComponent() {
      this.$refs.feeDetailHis.open({
        feeId: '123',
        staffNameLike: '',
        feeNameLike: '',
        payerObjName: '',
        logStartTime: '',
        logEndTime: ''
      })
    }
  }
}
</script>