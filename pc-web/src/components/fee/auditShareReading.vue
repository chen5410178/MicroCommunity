<template>
  <el-dialog
    :title="$t('shareReading.audit.title')"
    :visible.sync="dialogVisible"
    width="40%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('shareReading.audit.status')" prop="state">
        <el-select 
          v-model="form.state" 
          :placeholder="$t('shareReading.audit.selectStatus')"
          style="width:100%"
        >
          <el-option 
            :label="$t('shareReading.status.passed')" 
            value="C"
          />
          <el-option 
            :label="$t('shareReading.status.rejected')" 
            value="F"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('shareReading.audit.remark')">
        <el-input
          v-model="form.auditRemark"
          type="textarea"
          :placeholder="$t('shareReading.audit.inputRemark')"
          :rows="3"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { auditFloorShareReading } from '@/api/fee/shareReadingApi'

export default {
  name: 'AuditShareReading',
  data() {
    return {
      dialogVisible: false,
      form: {
        readingId: '',
        state: '',
        auditRemark: '',
        communityId: ''
      },
      rules: {
        state: [
          { required: true, message: this.$t('shareReading.validate.selectStatus'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.dialogVisible = true
      this.form.readingId = data.readingId
      this.form.communityId = getCommunityId()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await auditFloorShareReading(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } catch (error) {
            console.error('审核公摊抄表失败:', error)
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        readingId: '',
        state: '',
        auditRemark: '',
        communityId: ''
      }
    }
  }
}
</script>