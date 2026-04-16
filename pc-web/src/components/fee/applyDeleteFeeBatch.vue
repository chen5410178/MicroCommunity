<template>
  <el-dialog
    :title="$t('applyDeleteFeeBatch.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="formData"
      label-width="120px"
      label-position="right"
    >
      <el-form-item :label="$t('applyDeleteFeeBatch.form.batchId')">
        <el-input
          v-model="formData.batchId"
          disabled
        />
      </el-form-item>
      <el-form-item :label="$t('applyDeleteFeeBatch.form.createUserName')">
        <el-input
          v-model="formData.createUserName"
          disabled
        />
      </el-form-item>
      <el-form-item :label="$t('applyDeleteFeeBatch.form.createTime')">
        <el-input
          v-model="formData.createTime"
          disabled
        />
      </el-form-item>
      <el-form-item
        :label="$t('applyDeleteFeeBatch.form.remark')"
        prop="remark"
        :rules="[
          { required: true, message: $t('applyDeleteFeeBatch.rules.remarkRequired'), trigger: 'blur' }
        ]"
      >
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          :placeholder="$t('applyDeleteFeeBatch.placeholder.remark')"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { applyDeletePayFeeBatch } from '@/api/fee/payFeeBatchApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ApplyDeleteFeeBatch',
  data() {
    return {
      visible: false,
      formData: {
        batchId: '',
        createUserName: '',
        createTime: '',
        remark: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.resetForm()
      Object.assign(this.formData, data)
      this.formData.communityId = getCommunityId()
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    resetForm() {
      this.formData = {
        batchId: '',
        createUserName: '',
        createTime: '',
        remark: '',
        communityId: ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await applyDeletePayFeeBatch(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('申请取消失败:', error)
          }
        }
      })
    }
  }
}
</script>