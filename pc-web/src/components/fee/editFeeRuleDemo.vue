<template>
  <div>
    <el-button @click="openModal">{{ $t('editFeeRuleDemo.openModal') }}</el-button>
    <edit-fee-rule ref="editFeeRule" @success="handleSuccess"></edit-fee-rule>
  </div>
</template>

<script>
import EditFeeRule from './editFeeRule'

export default {
  name: 'EditFeeRuleDemo',
  components: {
    EditFeeRule
  },
  methods: {
    openModal() {
      this.$refs.editFeeRule.open({
        ruleId: '123',
        maxTime: '2023-12-31 23:59:59',
        curYearMonth: '2023-01-01'
      })
    },
    handleSuccess() {
      this.$message.success(this.$t('common.operationSuccess'))
    }
  }
}
</script>