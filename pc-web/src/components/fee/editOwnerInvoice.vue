<template>
  <el-dialog
    :title="$t('editTitle')"
    :visible.sync="visible"
    width="50%"
    @close="resetForm"
  >
    <el-form ref="form" :model="formData" label-width="150px">
      <el-form-item :label="$t('invoiceType')" prop="invoiceType" required>
        <el-select 
          v-model="formData.invoiceType" 
          :placeholder="$t('requiredInvoiceType')"
          style="width: 100%"
        >
          <el-option value="1001" :label="$t('personal')" />
          <el-option value="2002" :label="$t('enterprise')" />
        </el-select>
      </el-form-item>
      
      <el-form-item :label="$t('invoiceHeader')" prop="invoiceName" required>
        <el-input 
          v-model="formData.invoiceName" 
          :placeholder="$t('requiredInvoiceHeader')" 
        />
      </el-form-item>
      
      <el-form-item :label="$t('taxpayerId')" prop="invoiceNum" required>
        <el-input 
          v-model="formData.invoiceNum" 
          :placeholder="$t('requiredTaxpayerId')" 
        />
      </el-form-item>
      
      <el-form-item :label="$t('address')" prop="invoiceAddress" required>
        <el-input 
          v-model="formData.invoiceAddress" 
          :placeholder="$t('requiredAddress')" 
        />
      </el-form-item>
      
      <el-form-item :label="$t('phone')" prop="invoiceLink" required>
        <el-input 
          v-model="formData.invoiceLink" 
          :placeholder="$t('requiredPhone')" 
        />
      </el-form-item>
      
      <el-form-item :label="$t('bankAccount')" prop="invoiceAccount" required>
        <el-input 
          v-model="formData.invoiceAccount" 
          :placeholder="$t('requiredBankAccount')" 
        />
      </el-form-item>
      
      <el-form-item :label="$t('remark')" prop="remark">
        <el-input 
          v-model="formData.remark" 
          type="textarea" 
          :placeholder="$t('optionalRemark')" 
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="handleSave">{{ $t('save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateOwnerInvoice } from '@/api/fee/ownerInvoiceApi'

export default {
  name: 'EditOwnerInvoice',
  data() {
    return {
      visible: false,
      formData: {
        oiId: '',
        ownerId: '',
        ownerName: '',
        invoiceType: '',
        invoiceName: '',
        invoiceNum: '',
        invoiceAddress: '',
        invoiceLink: '',
        invoiceAccount: '',
        remark: ''
      }
    }
  },
  methods: {
    open(data) {
      Object.assign(this.formData, data)
      this.visible = true
    },
    resetForm() {
      this.$refs.form.resetFields()
      Object.keys(this.formData).forEach(key => {
        this.formData[key] = ''
      })
    },
    async handleSave() {
      try {
        await updateOwnerInvoice(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.saveFailed'))
      }
    }
  }
}
</script>