<template>
  <el-dialog :title="$t('resourceAuditFlow.addTitle')" :visible.sync="dialogVisible" width="600px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('resourceAuditFlow.flowName')" prop="flowName" :label-width="formLabelWidth">
        <el-input v-model="form.flowName" :placeholder="$t('resourceAuditFlow.flowNamePlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('resourceAuditFlow.flowType')" prop="auditType" :label-width="formLabelWidth">
        <el-select v-model="form.auditType" :placeholder="$t('resourceAuditFlow.flowTypePlaceholder')"
          style="width:100%">
          <el-option :label="$t('resourceAuditFlow.purchaseFlow')" value="10001" />
          <el-option :label="$t('resourceAuditFlow.receiveFlow')" value="10002" />
          <el-option :label="$t('resourceAuditFlow.transferFlow')" value="10003" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('resourceAuditFlow.remark')" prop="remark" :label-width="formLabelWidth">
        <el-input v-model="form.remark" type="textarea" :rows="3"
          :placeholder="$t('resourceAuditFlow.remarkPlaceholder')" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">
        {{ $t('resourceAuditFlow.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('resourceAuditFlow.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveResourceAuditFlow } from '@/api/resource/resourceAuditFlowApi'

export default {
  name: 'AddResourceAuditFlow',
  data() {
    return {
      dialogVisible: false,
      formLabelWidth: '120px',
      form: {
        flowName: '',
        auditType: '',
        remark: ''
      },
      rules: {
        flowName: [
          { required: true, message: this.$t('resourceAuditFlow.flowNameRequired'), trigger: 'blur' },
          { max: 30, message: this.$t('resourceAuditFlow.maxLength30'), trigger: 'blur' }
        ],
        auditType: [
          { required: true, message: this.$t('resourceAuditFlow.flowTypeRequired'), trigger: 'change' }
        ],
        remark: [
          { max: 512, message: this.$t('resourceAuditFlow.maxLength512'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.dialogVisible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        flowName: '',
        auditType: '',
        remark: ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            const res = await saveResourceAuditFlow(this.form)
            if (res.code == 0) {
              this.$message.success(this.$t('common.operationSuccess'))
              this.dialogVisible = false
              this.$emit('success')
            } else {
              this.$message.error(res.msg || this.$t('common.submitFailed'))
            }
          } catch (error) {
            console.error('添加审批流程失败:', error)
            this.$message.error(this.$t('common.submitFailed'))
          }
        }
      })
    }
  }
}
</script>