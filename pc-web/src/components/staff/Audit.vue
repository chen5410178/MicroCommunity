<template>
  <el-dialog 
    :title="$t('audit.title')" 
    :visible.sync="visible" 
    width="50%"
    @close="handleClose">
    <el-form :model="auditInfo" label-width="120px">
      <el-form-item :label="$t('audit.state')" prop="state">
        <el-select v-model="auditInfo.state" class="w-full" @change="handleStateChange">
          <el-option :label="$t('audit.selectState')" value="" disabled></el-option>
          <el-option :label="$t('audit.agree')" value="1100"></el-option>
          <el-option :label="$t('audit.reject')" value="1200"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('audit.remark')" prop="remark">
        <el-input
          type="textarea"
          :rows="4"
          :placeholder="$t('audit.remarkPlaceholder')"
          v-model="auditInfo.remark">
        </el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.submit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'Audit',
  data() {
    return {
      visible: false,
      auditInfo: {
        state: '',
        remark: ''
      }
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    handleClose() {
      this.auditInfo = {
        state: '',
        remark: ''
      }
    },
    handleStateChange(val) {
      if (val === '1100') {
        this.auditInfo.remark = this.$t('audit.agree')
      } else {
        this.auditInfo.remark = ''
      }
    },
    handleSubmit() {
      if (!this.auditInfo.state) {
        this.$message.warning(this.$t('audit.stateRequired'))
        return
      }
      if (!this.auditInfo.remark) {
        this.$message.warning(this.$t('audit.remarkRequired'))
        return
      }
      if (this.auditInfo.state === '1200') {
        this.auditInfo.remark = `${this.$t('audit.reject')}: ${this.auditInfo.remark}`
      }
      this.$emit('auditMessage', this.auditInfo)
      this.visible = false
    }
  }
}
</script>