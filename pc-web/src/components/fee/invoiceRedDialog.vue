<template>
  <el-dialog
    :title="$t('invoiceApply.redDialogTitle')"
    :visible.sync="visible"
    width="500px"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('invoiceApply.redReason')" prop="reason">
        <el-select v-model="form.reason" :placeholder="$t('invoiceApply.redReasonPlaceholder')" style="width: 100%">
          <el-option
            v-for="item in reasonOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('invoiceApply.redRemark')" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          maxlength="200"
          show-word-limit
          :placeholder="$t('invoiceApply.redRemarkPlaceholder')"
        />
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">
        {{ $t('invoiceApply.confirmRed') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveInvoiceRed } from '@/api/fee/invoiceApplyApi'

export default {
  name: 'InvoiceRedDialog',
  data() {
    return {
      visible: false,
      loading: false,
      applyId: '',
      form: {
        reason: '',
        remark: ''
      }
    }
  },
  computed: {
    rules() {
      return {
        reason: [{ required: true, message: this.$t('invoiceApply.redReasonRequired'), trigger: 'change' }]
      }
    },
    // 红冲原因需与商城 invoice_red.reason 保持一致, 选项值固定中文
    reasonOptions() {
      return [
        { value: '开票有误', label: this.$t('invoiceApply.redReasonOptions.invoiceError') },
        { value: '销售退回', label: this.$t('invoiceApply.redReasonOptions.salesReturn') },
        { value: '服务终止', label: this.$t('invoiceApply.redReasonOptions.serviceStop') },
        { value: '其他', label: this.$t('invoiceApply.redReasonOptions.other') }
      ]
    }
  },
  methods: {
    open(row) {
      this.applyId = row.applyId
      this.form = { reason: '', remark: '' }
      this.visible = true
    },
    handleClose() {
      this.visible = false
      this.applyId = ''
      this.form = { reason: '', remark: '' }
      if (this.$refs.formRef) {
        this.$refs.formRef.resetFields()
      }
    },
    handleSubmit() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        this.loading = true
        saveInvoiceRed({
          applyId: this.applyId,
          reason: this.form.reason,
          remark: this.form.remark
        }).then(res => {
          const data = res.data || {}
          if (data.state === 'DONE') {
            this.$message.success(this.$t('invoiceApply.redSuccess'))
          } else {
            // 落单成功但红字开票未完成, 冲红单保留待冲红状态(可在商城后台重试)
            this.$message.warning(data.failReason || this.$t('invoiceApply.redFailed'))
          }
          this.$emit('success')
          this.loading = false
          this.handleClose()
        }).catch(error => {
          this.$message.error(error.message)
          this.loading = false
        })
      })
    }
  }
}
</script>
