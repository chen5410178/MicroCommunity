<template>
  <el-dialog 
    :title="$t('adminRepairDetail.replyAppraise')" 
    :visible.sync="visible" 
    width="50%"
    @close="resetForm"
  >
    <el-form :model="form" :rules="rules" ref="form" label-width="120px">
      <el-form-item :label="$t('adminRepairDetail.content')" prop="replyContext">
        <el-input 
          type="textarea" 
          :rows="5" 
          v-model="form.replyContext" 
          :placeholder="$t('adminRepairDetail.replyPlaceholder')"
        ></el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { replyRepairAppraise } from '@/api/work/adminRepairDetailApi'

export default {
  name: 'ReplyRepairAppraise',
  data() {
    return {
      visible: false,
      form: {
        repairId: '',
        ruId: '',
        replyContext: ''
      },
      rules: {
        replyContext: [
          { required: true, message: this.$t('adminRepairDetail.replyRequired'), trigger: 'blur' },
          { max: 512, message: this.$t('adminRepairDetail.replyMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form.repairId = data.repairId
      this.form.ruId = data.ruId
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        
        try {
          await replyRepairAppraise({
            ...this.form,
            communityId: this.getCommunityId()
          })
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message || this.$t('adminRepairDetail.replyError'))
        }
      })
    },
    resetForm() {
      this.form = {
        repairId: '',
        ruId: '',
        replyContext: ''
      }
      this.$refs.form && this.$refs.form.clearValidate()
    }
  }
}
</script>