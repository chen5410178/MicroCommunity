<template>
  <el-dialog
    :title="$t('printerRule.editTitle')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('printerRule.ruleName')" prop="ruleName">
        <el-input
          v-model="form.ruleName"
          :placeholder="$t('printerRule.ruleNamePlaceholder')"
        ></el-input>
      </el-form-item>
      <el-form-item :label="$t('printerRule.remark')" prop="remark">
        <el-input
          type="textarea"
          v-model="form.remark"
          :placeholder="$t('printerRule.remarkPlaceholder')"
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
import { updatePrinterRule } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditPrinterRule',
  data() {
    return {
      visible: false,
      form: {
        ruleId: '',
        ruleName: '',
        remark: '',
        communityId: ''
      },
      rules: {
        ruleName: [
          { required: true, message: this.$t('printerRule.ruleNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('printerRule.ruleNameMaxLength'), trigger: 'blur' }
        ],
        remark: [
          { max: 512, message: this.$t('printerRule.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.form.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.visible = true
      this.form = {
        ruleId: data.ruleId,
        ruleName: data.ruleName,
        remark: data.remark,
        communityId: getCommunityId()
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updatePrinterRule(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('修改打印机规则失败:', error)
          }
        }
      })
    }
  }
}
</script>