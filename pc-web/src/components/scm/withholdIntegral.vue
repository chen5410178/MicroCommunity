<template>
  <el-dialog :title="$t('withholdIntegral.applyWithdraw')" :visible.sync="visible" width="50%" center>
    <el-form ref="form" :model="formData" label-width="120px" :rules="rules">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('withholdIntegral.totalPoints')" prop="amount">
            <el-input v-model="formData.amount" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('withholdIntegral.withdrawPoints')" prop="applyAmount">
            <el-input v-model="formData.applyAmount" :placeholder="$t('withholdIntegral.withdrawPointsPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('withholdIntegral.applicant')" prop="applyUserName">
            <el-input v-model="formData.applyUserName" :placeholder="$t('withholdIntegral.applicantPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('withholdIntegral.applicantPhone')" prop="applyUserTel">
            <el-input v-model="formData.applyUserTel" :placeholder="$t('withholdIntegral.applicantPhonePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('withholdIntegral.bankAccount')" prop="bankNum">
            <el-input v-model="formData.bankNum" :placeholder="$t('withholdIntegral.bankAccountPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('withholdIntegral.accountName')" prop="bankNumName">
            <el-input v-model="formData.bankNumName" :placeholder="$t('withholdIntegral.accountNamePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('withholdIntegral.bank')" prop="bankName">
            <el-input v-model="formData.bankName" type="textarea"
              :placeholder="$t('withholdIntegral.bankPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('withholdIntegral.applicationNote')" prop="remark">
            <el-input v-model="formData.remark" type="textarea"
              :placeholder="$t('withholdIntegral.applicationNotePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="close">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submit" :loading="loading">
        {{ $t('common.submit') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { applyIntegralWithdrawal } from '@/api/scm/communityIntegralApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'WithholdIntegral',
  data() {
    return {
      visible: false,
      loading: false,
      formData: {
        integralId: '',
        integralName: '',
        communityId: '',
        amount: '',
        applyAmount: '',
        applyUserName: '',
        applyUserTel: '',
        remark: '',
        bankNum: '',
        bankNumName: '',
        bankName: ''
      },
      rules: {
        applyAmount: [
          { required: true, message: this.$t('withholdIntegral.withdrawPointsRequired'), trigger: 'blur' }
        ],
        applyUserName: [
          { required: true, message: this.$t('withholdIntegral.applicantRequired'), trigger: 'blur' }
        ],
        applyUserTel: [
          { required: true, message: this.$t('withholdIntegral.applicantPhoneRequired'), trigger: 'blur' }
        ],
        bankNum: [
          { required: true, message: this.$t('withholdIntegral.bankAccountRequired'), trigger: 'blur' }
        ],
        bankNumName: [
          { required: true, message: this.$t('withholdIntegral.accountNameRequired'), trigger: 'blur' }
        ],
        bankName: [
          { required: true, message: this.$t('withholdIntegral.bankRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.resetForm()
      this.formData = {
        ...this.formData,
        ...data,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    close() {
      this.visible = false
      this.loading = false
      this.$refs.form.resetFields()
    },
    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    async submit() {
      try {
        this.$refs.form.validate(async valid => {
          if (!valid) return

          this.loading = true
          const { code, msg } = await applyIntegralWithdrawal(this.formData)
          if (code != 0) {
            this.$message.error(msg)
            return
          }
          this.$emit('success')
          this.$message.success(this.$t('common.operationSuccess'))
          this.close()
        })
      } catch (error) {
        console.error('Failed to apply integral withdrawal:', error)
        this.$message.error(this.$t('withholdIntegral.applyFailed'))
      } finally {
        this.loading = false
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