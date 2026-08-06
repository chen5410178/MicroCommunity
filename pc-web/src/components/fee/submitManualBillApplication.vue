<template>
  <el-dialog
    :title="$t('manualBillGeneration.submitApplication')"
    :visible.sync="visible"
    width="500px"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" label-width="100px">
      <el-form-item :label="$t('manualBillGeneration.remark')">
        <el-input v-model="form.remark" type="textarea" :rows="3" :placeholder="$t('manualBillGeneration.remarkPlaceholder')"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">
        {{ $t('manualBillGeneration.submitApplication') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveManualBillApplication } from '@/api/fee/manualBillGenerationApi'

export default {
  name: 'SubmitManualBillApplication',
  data() {
    return {
      visible: false,
      loading: false,
      form: { remark: '' },
      submitData: {}
    }
  },
  methods: {
    open(data) {
      this.submitData = data || {}
      this.form.remark = ''
      this.visible = true
    },
    handleClose() {
      this.visible = false
      this.form.remark = ''
      if (this.$refs.formRef) {
        this.$refs.formRef.resetFields()
      }
    },
    handleSubmit() {
      this.loading = true
      saveManualBillApplication({
        communityId: this.submitData.communityId,
        configId: this.submitData.configId,
        billMonth: this.submitData.billMonth,
        remark: this.form.remark,
        details: this.submitData.details || []
      }).then(res => {
        this.loading = false
        if (res.code === 0) {
          this.$message.success(this.$t('manualBillGeneration.submitSuccess'))
          this.$emit('success')
          this.handleClose()
        } else {
          this.$message.error(res.msg || this.$t('manualBillGeneration.submitFail'))
        }
      }).catch(() => {
        this.loading = false
        this.$message.error(this.$t('manualBillGeneration.submitFail'))
      })
    }
  }
}
</script>
