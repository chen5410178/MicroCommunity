<template>
  <el-dialog
    :title="$t('couponRule.addRule')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('couponRule.ruleName')" prop="ruleName">
        <el-input v-model="form.ruleName" :placeholder="$t('couponRule.ruleNamePlaceholder')" />
      </el-form-item>
      
      <el-form-item :label="$t('couponRule.remark')" prop="remark">
        <el-input
          type="textarea"
          :rows="3"
          v-model="form.remark"
          :placeholder="$t('couponRule.remarkPlaceholder')" />
      </el-form-item>
    </el-form>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveCouponRule } from '@/api/scm/couponRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddCouponRule',
  data() {
    return {
      visible: false,
      form: {
        ruleName: '',
        remark: '',
        communityId: ''
      },
      rules: {
        ruleName: [
          { required: true, message: this.$t('couponRule.ruleNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('couponRule.ruleNameMaxLength'), trigger: 'blur' }
        ],
        remark: [
          { max: 512, message: this.$t('couponRule.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.form.communityId = getCommunityId()
      this.$nextTick(() => {
        this.$refs.form.resetFields()
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveCouponRule(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('Failed to add coupon rule:', error)
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.el-dialog {
  .el-form {
    padding: 0 20px;
    
    .el-input, .el-textarea {
      width: 100%;
    }
  }
}
</style>