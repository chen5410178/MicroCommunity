<template>
  <el-dialog
    :title="$t('cancelAccountDetail.title')"
    :visible.sync="visible"
    width="600px"
    :close-on-click-modal="false">
    <el-form 
      ref="form" 
      :model="formData" 
      label-position="left"
      label-width="120px">
      <el-form-item 
        :label="$t('cancelAccountDetail.accountName')" 
        prop="acctName">
        <el-input 
          v-model="formData.acctName" 
          disabled
          :placeholder="$t('cancelAccountDetail.required')"/>
      </el-form-item>
      
      <el-form-item 
        :label="$t('cancelAccountDetail.cancelAmount')" 
        prop="amount">
        <el-input 
          v-model="formData.amount" 
          disabled
          :placeholder="$t('cancelAccountDetail.required')"/>
      </el-form-item>
      
      <el-form-item 
        :label="$t('cancelAccountDetail.cancelReason')" 
        prop="remark"
        :rules="[{ required: true, message: $t('cancelAccountDetail.required') }]">
        <el-input
          type="textarea"
          v-model="formData.remark"
          :rows="4"
          :placeholder="$t('cancelAccountDetail.required')"/>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('cancelAccountDetail.cancel') }}
      </el-button>
      <el-button 
        type="primary" 
        @click="handleSubmit"
        :loading="submitting">
        {{ $t('cancelAccountDetail.submit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { cancelAccountDetail } from '@/api/account/accountDetailManageApi'

export default {
  name: 'CancelAccountDetail',
  data() {
    return {
      visible: false,
      submitting: false,
      formData: {
        detailId: '',
        acctName: '',
        amount: '',
        remark: ''
      }
    }
  },
  methods: {
    open(row) {
      this.formData = {
        detailId: row.detailId,
        acctName: row.acctName,
        amount: row.amount,
        remark: ''
      }
      this.visible = true
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    async handleSubmit() {
      try {
        const valid = await this.$refs.form.validate()
        if (!valid) return
        
        this.submitting = true
        await cancelAccountDetail({
          detailId: this.formData.detailId,
          remark: this.formData.remark
        })
        
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message || this.$t('common.operationFailed'))
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>