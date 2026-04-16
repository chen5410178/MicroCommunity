<template>
  <el-dialog :title="$t('audit.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px" label-position="right">
      <el-form-item :label="$t('audit.form.state')" prop="state" :rules="[
        { required: true, message: $t('audit.rules.stateRequired'), trigger: 'change' }
      ]">
        <el-select v-model="formData.state" :placeholder="$t('audit.placeholder.state')" style="width:100%"
          @change="handleStateChange">
          <el-option :label="$t('audit.state.agree')" value="1100" />
          <el-option :label="$t('audit.state.reject')" value="1200" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('audit.form.remark')" prop="remark" :rules="[
        { required: true, message: $t('audit.rules.remarkRequired'), trigger: 'blur' },
        { max: 200, message: $t('audit.rules.remarkMaxLength'), trigger: 'blur' }
      ]">
        <el-input v-model="formData.remark" type="textarea" :rows="3" :placeholder="$t('audit.placeholder.remark')" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.submit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'Audit',
  data() {
    return {
      visible: false,
      formData: {
        state: '',
        remark: ''
      }
    }
  },
  methods: {
    open() {
      this.resetForm()
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    resetForm() {
      this.formData = {
        state: '',
        remark: ''
      }
    },
    handleStateChange(val) {
      if (val === '1100') {
        this.formData.remark = this.$t('audit.state.agree')
      } else {
        this.formData.remark = ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.formData.state === '1200') {
            this.formData.remark = `${this.$t('audit.state.reject')}:${this.formData.remark}`
          }
          this.$emit('notifyAuditInfo', this.formData)
          this.$emit('success', this.formData)
          this.visible = false
        }
      })
    }
  }
}
</script>