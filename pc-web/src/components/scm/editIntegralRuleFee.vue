<template>
  <el-dialog
    :title="$t('integralRule.editFeeTitle')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form 
      ref="form" 
      :model="form" 
      :rules="rules" 
      label-width="150px"
    >
      <el-form-item 
        :label="$t('integralRule.payStartTime')" 
        prop="payStartTime"
      >
        <el-date-picker
          v-model="form.payStartTime"
          type="datetime"
          :placeholder="$t('integralRule.payStartTimePlaceholder')"
          style="width: 100%"
          value-format="yyyy-MM-dd HH:mm:ss"
        />
      </el-form-item>
      <el-form-item 
        :label="$t('integralRule.payEndTime')" 
        prop="payEndTime"
      >
        <el-date-picker
          v-model="form.payEndTime"
          type="datetime"
          :placeholder="$t('integralRule.payEndTimePlaceholder')"
          style="width: 100%"
          value-format="yyyy-MM-dd HH:mm:ss"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button 
        type="primary" 
        @click="handleSubmit"
      >{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateIntegralRuleFee } from '@/api/scm/integralRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditIntegralRuleFee',
  data() {
    return {
      visible: false,
      form: {
        irfId: '',
        ruleId: '',
        payStartTime: '',
        payEndTime: ''
      },
      rules: {
        payStartTime: [
          { required: true, message: this.$t('integralRule.payStartTimeRequired'), trigger: 'change' }
        ],
        payEndTime: [
          { required: true, message: this.$t('integralRule.payEndTimeRequired'), trigger: 'change' },
          { 
            validator: (rule, value, callback) => {
              if (!value || !this.form.payStartTime) {
                callback()
              } else if (new Date(value) <= new Date(this.form.payStartTime)) {
                callback(new Error(this.$t('integralRule.payEndTimeAfterStart')))
              } else {
                callback()
              }
            },
            trigger: 'change'
          }
        ],
        irfId: [
          { required: true, message: this.$t('integralRule.feeIdRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        irfId: data.irfId,
        ruleId: data.ruleId,
        payStartTime: data.payStartTime,
        payEndTime: data.payEndTime
      }
      this.communityId = getCommunityId()
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        
        try {
          const params = {
            ...this.form,
            communityId: this.communityId
          }
          await updateIntegralRuleFee(params)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message || this.$t('common.editFailed'))
        }
      })
    }
  }
}
</script>