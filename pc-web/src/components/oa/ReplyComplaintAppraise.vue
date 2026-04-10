<template>
  <el-dialog
    :title="$t('replyComplaintAppraise.title')"
    :visible.sync="visible"
    width="50%"
    @close="resetForm"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('replyComplaintAppraise.content')" prop="replyContext">
        <el-input
          type="textarea"
          :rows="5"
          v-model="form.replyContext"
          :placeholder="$t('replyComplaintAppraise.placeholder')"
        />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm" :loading="submitting">
        {{ $t('common.submit') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { replyComplaintAppraise } from '@/api/oa/complaintDetailApi'

export default {
  name: 'ReplyComplaintAppraise',
  data() {
    return {
      visible: false,
      submitting: false,
      form: {
        appraiseId: '',
        replyContext: '',
        communityId: ''
      },
      rules: {
        replyContext: [
          { required: true, message: this.$t('replyComplaintAppraise.required'), trigger: 'blur' },
          { max: 512, message: this.$t('replyComplaintAppraise.maxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form.appraiseId = row.appraiseId
      this.form.communityId = getCommunityId()
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    async submitForm() {
      try {
        const valid = await this.$refs.form.validate()
        if (!valid) return

        this.submitting = true
        await replyComplaintAppraise(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('回复评价失败:', error)
      } finally {
        this.submitting = false
      }
    },
    resetForm() {
      this.form.replyContext = ''
      this.$refs.form && this.$refs.form.clearValidate()
    }
  }
}
</script>