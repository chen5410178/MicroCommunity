<template>
  <el-dialog
    :title="$t('doingComplaint.title')"
    :visible.sync="visible"
    width="60%"
    @close="handleClose"
  >
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item
        :label="$t('doingComplaint.description')"
        prop="context"
        :rules="[
          { required: true, message: $t('doingComplaint.required'), trigger: 'blur' },
          { max: 512, message: $t('doingComplaint.maxLength'), trigger: 'blur' }
        ]"
      >
        <el-input
          v-model="formData.context"
          type="textarea"
          :rows="4"
          :placeholder="$t('doingComplaint.placeholder')"
        />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('doingComplaint.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('doingComplaint.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { auditComplaint } from '@/api/oa/uodoComplaintsApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DoingComplaint',
  data() {
    return {
      visible: false,
      formData: {
        complaintId: '',
        context: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.formData.complaintId = data.complaintId
      this.formData.communityId = getCommunityId()
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.resetFields()
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    async handleSubmit() {
      try {
        const valid = await this.$refs.form.validate()
        if (!valid) return

        await auditComplaint(this.formData)
        this.$emit('success')
        this.visible = false
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        console.error(error)
        this.$message.error(this.$t('doingComplaint.error'))
      }
    }
  }
}
</script>

<style scoped>
.el-textarea {
  width: 100%;
}
</style>