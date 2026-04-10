<template>
  <el-dialog
    :title="$t('prestoreAccount2.title')"
    :visible.sync="dialogVisible"
    width="50%"
    @close="handleClose"
  >
    <el-form :model="formData" :rules="rules" ref="form" label-width="120px">
      <el-form-item :label="$t('prestoreAccount2.receivable')">
        <el-input v-model="formData.totalAmount" disabled></el-input>
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount2.actualReceipt')">
        <el-input 
          v-model="formData.receivedAmount" 
          @change="computedAmount"
        ></el-input>
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount2.prestoreAmount')" prop="amount">
        <el-input 
          v-model="formData.amount" 
          :placeholder="$t('prestoreAccount2.amountPlaceholder')"
        ></el-input>
      </el-form-item>

      <el-form-item :label="$t('prestoreAccount2.remark')" prop="remark">
        <el-input
          type="textarea"
          v-model="formData.remark"
          :placeholder="$t('prestoreAccount2.remarkPlaceholder')"
        ></el-input>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('prestoreAccount2.cancel') }}</el-button>
      <el-button type="primary" @click="savePrestoreAccount2Info">{{ $t('prestoreAccount2.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'PrestoreAccount2',
  data() {
    return {
      dialogVisible: false,
      formData: {
        tel: '',
        ownerId: '',
        owners: [],
        amount: '',
        remark: '',
        receivedAmount: '',
        redepositAmount: '',
        totalAmount: '',
        acctType: ''
      },
      rules: {
        amount: [
          { required: true, message: this.$t('prestoreAccount2.amountRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('prestoreAccount2.amountFormat'), trigger: 'blur' }
        ],
        remark: [
          { max: 200, message: this.$t('prestoreAccount2.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(params) {
      this.formData = {
        ...this.formData,
        acctType: params.acctType,
        amount: params.redepositAmount,
        redepositAmount: params.redepositAmount,
        receivedAmount: params.receivedAmount,
        totalAmount: (parseFloat(params.receivedAmount) - parseFloat(params.redepositAmount)).toFixed(2),
        ownerId: params.objId
      }
      this.dialogVisible = true
    },
    close() {
      this.dialogVisible = false
    },
    computedAmount() {
      let amount = parseFloat(this.formData.receivedAmount) - parseFloat(this.formData.totalAmount)
      amount = amount.toFixed(2)
      this.formData.amount = amount < 0 ? 0 : amount
    },
    async savePrestoreAccount2Info() {
      try {
        const valid = await this.$refs.form.validate()
        if (!valid) return

        this.formData.communityId = await getCommunityId()
        
        const response = await this.$http.post('/account.ownerPrestoreAccount', this.formData)
        if (response.data.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.close()
          this.$emit('success')
          this.$emit('refresh')
        } else {
          this.$message.error(response.data.msg)
        }
      } catch (error) {
        console.error('预存账户失败:', error)
        this.$message.error(this.$t('prestoreAccount2.saveError'))
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        tel: '',
        ownerId: '',
        owners: [],
        amount: '',
        remark: '',
        receivedAmount: '',
        redepositAmount: '',
        totalAmount: '',
        acctType: ''
      }
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>