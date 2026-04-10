<template>
  <el-dialog
    :title="$t('integralRule.addTitle')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('integralRule.ruleName')" prop="ruleName">
        <el-input
          v-model="form.ruleName"
          :placeholder="$t('integralRule.ruleNamePlaceholder')"
        ></el-input>
      </el-form-item>
      <el-form-item :label="$t('integralRule.remark')" prop="remark">
        <el-input
          type="textarea"
          :rows="3"
          v-model="form.remark"
          :placeholder="$t('integralRule.remarkPlaceholder')"
        ></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveIntegralRule } from '@/api/scm/integralRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddIntegralRule',
  data() {
    return {
      visible: false,
      form: {
        ruleName: '',
        remark: ''
      },
      rules: {
        ruleName: [
          { required: true, message: this.$t('integralRule.ruleNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('integralRule.ruleNameMaxLength'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('integralRule.remarkRequired'), trigger: 'blur' },
          { max: 512, message: this.$t('integralRule.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.communityId = getCommunityId()
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        
        try {
          const params = {
            ...this.form,
            communityId: this.communityId
          }
          await saveIntegralRule(params)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message || this.$t('common.addFailed'))
        }
      })
    }
  }
}
</script>