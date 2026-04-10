<template>
  <el-dialog
    :title="$t('oaWorkflowManage.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item
        :label="$t('oaWorkflowManage.edit.flowName')"
        prop="flowName"
      >
        <el-input
          v-model="form.flowName"
          :placeholder="$t('oaWorkflowManage.edit.flowNamePlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="$t('oaWorkflowManage.edit.describle')">
        <el-input
          v-model="form.describle"
          type="textarea"
          :placeholder="$t('oaWorkflowManage.edit.describlePlaceholder')"
          :rows="3"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateOaWorkflow } from '@/api/oa/oaWorkflowManageApi'

export default {
  name: 'EditOaWorkflow',
  data() {
    return {
      visible: false,
      form: {
        flowId: '',
        flowName: '',
        describle: ''
      },
      rules: {
        flowName: [
          { required: true, message: this.$t('oaWorkflowManage.validate.flowNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('oaWorkflowManage.validate.flowNameMaxLength'), trigger: 'blur' }
        ],
        flowId: [
          { required: true, message: this.$t('oaWorkflowManage.validate.flowIdRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.form = {
        flowId: data.flowId,
        flowName: data.flowName,
        describle: data.describle || ''
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        flowId: '',
        flowName: '',
        describle: ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateOaWorkflow(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(this.$t('oaWorkflowManage.edit.error'))
          }
        }
      })
    }
  }
}
</script>