<template>
  <el-dialog :title="$t('marketRule.add')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('marketRule.name')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('marketRule.required')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('marketRule.remark')" prop="remark">
        <el-input type="textarea" v-model="form.remark" :placeholder="$t('marketRule.required')" rows="4"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('marketRule.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('marketRule.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveMarketRule } from '@/api/market/marketRuleApi'

export default {
  name: 'AddMarketRule',
  data() {
    return {
      visible: false,
      form: {
        name: '',
        remark: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('marketRule.required'), trigger: 'blur' },
          { max: 64, message: this.$t('marketRule.maxLength64'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('marketRule.required'), trigger: 'blur' },
          { max: 512, message: this.$t('marketRule.maxLength512'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveMarketRule(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    }
  }
}
</script>