<template>
  <el-dialog
    :title="$t('audit.title')"
    :visible.sync="visible"
    width="50%"
    @close="closeDialog"
  >
    <el-form :model="auditInfo" label-width="120px">
      <el-form-item :label="$t('audit.state')" required>
        <el-select 
          v-model="auditInfo.state" 
          :placeholder="$t('audit.selectPlaceholder')"
          style="width: 100%"
        >
          <el-option 
            :label="$t('audit.agree')" 
            value="1100"
          />
          <el-option 
            :label="$t('audit.reject')" 
            value="1200"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('audit.reason')" required>
        <el-input
          type="textarea"
          :rows="4"
          :placeholder="$t('audit.reasonPlaceholder')"
          v-model="auditInfo.remark"
        />
      </el-form-item>
    </el-form>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitAudit">{{ $t('common.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'AuditDialog',
  data() {
    return {
      visible: false,
      auditInfo: {
        state: '',
        remark: ''
      }
    }
  },
  watch: {
    'auditInfo.state'(val) {
      if (val === '1100') {
        this.auditInfo.remark = this.$t('audit.agree')
      } else if (val === '1200') {
        this.auditInfo.remark = ''
      }
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    closeDialog() {
      this.visible = false
      this.resetForm()
    },
    resetForm() {
      this.auditInfo = {
        state: '',
        remark: ''
      }
    },
    submitAudit() {
      if (!this.validateForm()) {
        return
      }
      
      const auditData = { ...this.auditInfo }
      if (auditData.state === '1200') {
        auditData.remark = `${this.$t('audit.reject')}: ${auditData.remark}`
      }
      
      this.$emit('submit', auditData)
      this.closeDialog()
    },
    validateForm() {
      if (!this.auditInfo.state) {
        this.$message.error(this.$t('audit.stateRequired'))
        return false
      }
      if (!this.auditInfo.remark) {
        this.$message.error(this.$t('audit.reasonRequired'))
        return false
      }
      if (this.auditInfo.remark.length > 200) {
        this.$message.error(this.$t('audit.reasonMaxLength'))
        return false
      }
      return true
    }
  }
}
</script>

<style scoped>
.el-select {
  width: 100%;
}
</style>