<template>
  <el-dialog
    :title="$t('editFeeRule.title')"
    :visible.sync="visible"
    width="40%"
    :before-close="handleClose"
  >
    <el-form :model="editFeeRuleInfo" label-width="120px" >
      <el-form-item :label="$t('editFeeRule.maxTime')" prop="maxTime">
        <el-date-picker
          v-model="editFeeRuleInfo.maxTime"
          type="datetime"
          :placeholder="$t('editFeeRule.maxTimePlaceholder')"
          value-format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('editFeeRule.curYearMonth')" prop="curYearMonth">
        <el-date-picker
          v-model="editFeeRuleInfo.curYearMonth"
          type="date"
          :placeholder="$t('editFeeRule.curYearMonthPlaceholder')"
          value-format="yyyy-MM-dd"
          @change="validateCurYearMonth"
        ></el-date-picker>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="_doEidtFee">{{ $t('common.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updatePayFeeRule } from '@/api/fee/editFeeRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditFeeRule',
  data() {
    return {
      visible: false,
      editFeeRuleInfo: {
        ruleId: '',
        maxTime: '',
        curYearMonth: '',
        oldCurYearMonth: ''
      }
    }
  },
  methods: {
    open(fee) {
      this.editFeeRuleInfo = {
        ruleId: fee.ruleId,
        maxTime: fee.maxTime,
        curYearMonth: fee.curYearMonth,
        oldCurYearMonth: fee.curYearMonth
      }
      this.visible = true
    },
    handleClose() {
      this.visible = false
      this.clearEditFeeRuleInfoData()
    },
    validateCurYearMonth(value) {
      const start = new Date(this.editFeeRuleInfo.oldCurYearMonth)
      const end = new Date(value)
      if (start >= end) {
        this.$message.error(this.$t('editFeeRule.dateError', { date: this.editFeeRuleInfo.oldCurYearMonth }))
        this.editFeeRuleInfo.curYearMonth = ''
      }
    },
    editFeeRuleValidate() {
      if (!this.editFeeRuleInfo.maxTime) {
        this.$message.error(this.$t('editFeeRule.maxTimeRequired'))
        return false
      }
      if (!this.editFeeRuleInfo.curYearMonth) {
        this.$message.error(this.$t('editFeeRule.curYearMonthRequired'))
        return false
      }
      return true
    },
    async _doEidtFee() {
      if (!this.editFeeRuleValidate()) return
      
      this.editFeeRuleInfo.communityId = getCommunityId()
      
      try {
        const res = await updatePayFeeRule(this.editFeeRuleInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('请求失败:', error)
        this.$message.error(this.$t('common.operateFail'))
      }
    },
    clearEditFeeRuleInfoData() {
      this.editFeeRuleInfo = {
        ruleId: '',
        maxTime: '',
        curYearMonth: '',
        oldCurYearMonth: ''
      }
    }
  }
}
</script>