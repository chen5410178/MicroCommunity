<template>
  <el-dialog :title="$t('withholdGold.applyWithdraw')" :visible.sync="visible" width="50%" :before-close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('withholdGold.goldAmount')" prop="amount">
            <el-input v-model="form.amount" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('withholdGold.withdrawAmount')" prop="applyAmount">
            <el-input v-model="form.applyAmount" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('withholdGold.applicant')" prop="applyUserName">
            <el-input v-model="form.applyUserName" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('withholdGold.applicantPhone')" prop="applyUserTel">
            <el-input v-model="form.applyUserTel" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('withholdGold.bankAccount')" prop="bankNum">
            <el-input v-model="form.bankNum" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('withholdGold.accountName')" prop="bankNumName">
            <el-input v-model="form.bankNumName" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('withholdGold.bankName')" prop="bankName">
            <el-input v-model="form.bankName" type="textarea" :rows="2" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('withholdGold.applicationNote')" prop="remark">
            <el-input v-model="form.remark" type="textarea" :rows="2" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm" :loading="loading">
        {{ $t('common.submit') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { applyGoldWithHold } from '@/api/scm/goldApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'WithholdGold',
  data() {
    return {
      visible: false,
      loading: false,
      form: {
        goldId: '',
        goldName: '',
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
          { required: true, message: this.$t('withholdGold.required'), trigger: 'blur' }
        ],
        applyUserName: [
          { required: true, message: this.$t('withholdGold.required'), trigger: 'blur' }
        ],
        applyUserTel: [
          { required: true, message: this.$t('withholdGold.required'), trigger: 'blur' }
        ],
        bankNum: [
          { required: true, message: this.$t('withholdGold.required'), trigger: 'blur' }
        ],
        bankNumName: [
          { required: true, message: this.$t('withholdGold.required'), trigger: 'blur' }
        ],
        bankName: [
          { required: true, message: this.$t('withholdGold.required'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        ...this.form,
        goldId: data.goldId,
        amount: data.amount,
        communityId: data.communityId || getCommunityId()
      }
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    handleClose() {
      this.visible = false
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          this.loading = true
          const { code, msg } = await applyGoldWithHold(this.form)
          if(code != 0){
            this.$message.error(msg)
            return
          }
          this.$emit('success')
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
        } catch (error) {
          console.error('Submit failed:', error)
          this.$message.error(this.$t('withholdGold.submitFailed'))
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.el-textarea {
  width: 100%;
}
</style>