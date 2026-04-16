<template>
  <div>
    <el-button @click="openModal">{{ $t('editFeeDemo.openModal') }}</el-button>
    <edit-fee ref="editFee" @success="handleSuccess"></edit-fee>
  </div>
</template>

<script>
import EditFee from './editFee'

export default {
  name: 'EditFeeDemo',
  components: {
    EditFee
  },
  methods: {
    openModal() {
      this.$refs.editFee.open({
        feeId: '123',
        startTime: '2023-01-01 00:00:00',
        endTime: '2023-01-01',
        maxEndTime: '2023-12-31',
        computingFormula: '1102',
        rateCycle: '12',
        rate: '0.05',
        rateStartTime: '2023-01-01'
      })
    },
    handleSuccess() {
      this.$message.success(this.$t('common.operationSuccess'))
    }
  }
}
</script>