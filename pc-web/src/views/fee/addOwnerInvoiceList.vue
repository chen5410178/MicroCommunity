<template>
  <div class="app-container">
    <el-card shadow="never">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('addOwnerInvoice.title') }}</span>
        <el-button size="small" type="text" @click="$router.go(-1)">
          {{ $t('addOwnerInvoice.back') }}
        </el-button>
      </div>

      <el-form ref="form" :model="formData" :rules="rules" class="text-left" label-width="150px" label-position="right">
        <el-form-item :label="$t('addOwnerInvoice.owner')" prop="ownerName">
          <el-input v-model="formData.ownerName" :placeholder="$t('addOwnerInvoice.placeholder.owner')" disabled
            style="width: 70%" />
          <el-button type="primary" style="margin-left: 10px" @click="openOwnerDialog">
            {{ $t('addOwnerInvoice.selectOwner') }}
          </el-button>
        </el-form-item>

        <el-form-item :label="$t('addOwnerInvoice.invoiceType')" prop="invoiceType">
          <el-select v-model="formData.invoiceType" :placeholder="$t('addOwnerInvoice.placeholder.invoiceType')"
            style="width: 100%">
            <el-option v-for="item in invoiceTypes" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('addOwnerInvoice.invoiceTitle')" prop="invoiceName">
          <el-input v-model="formData.invoiceName" :placeholder="$t('addOwnerInvoice.placeholder.invoiceTitle')"
            style="width: 100%" />
        </el-form-item>

        <el-form-item :label="$t('addOwnerInvoice.taxNumber')" prop="invoiceNum">
          <el-input v-model="formData.invoiceNum" :placeholder="$t('addOwnerInvoice.placeholder.taxNumber')"
            style="width: 100%" />
        </el-form-item>

        <el-form-item :label="$t('addOwnerInvoice.address')" prop="invoiceAddress">
          <el-input v-model="formData.invoiceAddress" :placeholder="$t('addOwnerInvoice.placeholder.address')"
            style="width: 100%" />
        </el-form-item>

        <el-form-item :label="$t('addOwnerInvoice.phone')" prop="invoiceLink">
          <el-input v-model="formData.invoiceLink" :placeholder="$t('addOwnerInvoice.placeholder.phone')"
            style="width: 100%" />
        </el-form-item>

        <el-form-item :label="$t('addOwnerInvoice.bankAccount')" prop="invoiceAccount">
          <el-input v-model="formData.invoiceAccount" :placeholder="$t('addOwnerInvoice.placeholder.bankAccount')"
            style="width: 100%" />
        </el-form-item>

        <el-form-item :label="$t('addOwnerInvoice.remarks')">
          <el-input v-model="formData.remark" type="textarea" :rows="3"
            :placeholder="$t('addOwnerInvoice.placeholder.remarks')" style="width: 100%" />
        </el-form-item>

        <el-form-item class="text-right">
          <el-button type="primary" @click="submitForm">
            {{ $t('addOwnerInvoice.save') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <search-owner ref="searchOwner" :visible.sync="showOwnerDialog" @chooseOwner="handleOwnerSelect" />
  </div>
</template>

<script>
import { saveOwnerInvoice } from '@/api/fee/addOwnerInvoiceApi'
import SearchOwner from '@/components/owner/SearchOwner'

export default {
  name: 'AddOwnerInvoice',
  components: { SearchOwner },
  data() {
    return {
      showOwnerDialog: false,
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
      },
      invoiceTypes: [
        { value: '1001', label: this.$t('addOwnerInvoice.personal') },
        { value: '2002', label: this.$t('addOwnerInvoice.enterprise') }
      ],
      rules: {
        ownerName: [
          { required: true, message: this.$t('addOwnerInvoice.validation.ownerRequired'), trigger: 'blur' }
        ],
        invoiceType: [
          { required: true, message: this.$t('addOwnerInvoice.validation.typeRequired'), trigger: 'change' }
        ],
        invoiceName: [
          { required: true, message: this.$t('addOwnerInvoice.validation.titleRequired'), trigger: 'blur' }
        ],
        invoiceNum: [
          { required: true, message: this.$t('addOwnerInvoice.validation.taxNumberRequired'), trigger: 'blur' }
        ],
        invoiceAddress: [
          { required: true, message: this.$t('addOwnerInvoice.validation.addressRequired'), trigger: 'blur' }
        ],
        invoiceLink: [
          { required: true, message: this.$t('addOwnerInvoice.validation.phoneRequired'), trigger: 'blur' }
        ],
        invoiceAccount: [
          { required: true, message: this.$t('addOwnerInvoice.validation.bankAccountRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    openOwnerDialog() {
      this.$refs.searchOwner.open()
    },
    handleOwnerSelect(owner) {
      this.formData.ownerId = owner.ownerId
      this.formData.ownerName = owner.name
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const payload = {
              ...this.formData,
              communityId: this.getCommunityId()
            }

            await saveOwnerInvoice(payload)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$router.go(-1)
          } catch (error) {
            console.error('保存失败:', error)
            this.$message.error(error.message || this.$t('common.saveFailed'))
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.float-right {
  float: right;
}

.el-form {
  max-width: 800px;
  margin: 0 auto;
}

.el-select,
.el-input {
  width: 100%;
}
</style>