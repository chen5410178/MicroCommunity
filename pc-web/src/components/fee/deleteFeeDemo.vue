<template>
  <div>
    <el-button @click="openModal">{{ $t('deleteFeeDemo.openModal') }}</el-button>
    <delete-fee ref="deleteFee" @success="handleSuccess"></delete-fee>
  </div>
</template>

<script>
import DeleteFee from './deleteFee'

export default {
  name: 'DeleteFeeDemo',
  components: {
    DeleteFee
  },
  methods: {
    openModal() {
      this.$refs.deleteFee.open({
        feeId: '123'
      })
    },
    handleSuccess() {
      this.$message.success(this.$t('common.operationSuccess'))
    }
  }
}
</script>