<template>
  <el-button @click="openModal">{{ $t('deleteFeeRuleDemo.openModal') }}</el-button>
  <delete-fee-rule ref="deleteFeeRule" @delete-success="handleDeleteSuccess"></delete-fee-rule>
</template>

<script>
import DeleteFeeRule from './deleteFeeRule'

export default {
  name: 'DeleteFeeRuleDemo',
  components: {
    DeleteFeeRule
  },
  methods: {
    openModal() {
      this.$refs.deleteFeeRule.open({
        feeRuleId: '123'
      })
    },
    handleDeleteSuccess() {
      console.log('Delete success')
    }
  }
}
</script>