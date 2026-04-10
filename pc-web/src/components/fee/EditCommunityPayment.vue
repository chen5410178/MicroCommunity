<template>
  <el-dialog :title="$t('editCommunityPayment.title')" :visible.sync="visible" width="800px" @closed="resetForm">
    <el-form ref="form" :model="form" label-width="120px" :rules="rules">
      <el-form-item :label="$t('communityPayment.paymentName')" prop="paymentName">
        <el-input v-model="form.paymentName" :placeholder="$t('addCommunityPayment.namePlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('communityPayment.paymentVendor')">
        <el-select v-model="form.paymentType" disabled style="width: 100%">
          <el-option v-for="item in paymentTypes" :key="item.paymentType" :label="item.name" :value="item.paymentType" />
        </el-select>
      </el-form-item>

      <el-form-item v-for="(key, index) in paymentKeys" :key="index" :label="key.name">
        <el-input v-model="key.columnValue" type="textarea" :rows="2" :placeholder="key.remark" />
      </el-form-item>

      <el-form-item v-if="form.paymentType === 'WECHAT'" :label="$t('communityPayment.merchantCert')">
        <file-upload ref="uploader" namespace="editCommunityPayment" @upload-success="handleUploadSuccess" />
      </el-form-item>

      <el-form-item :label="$t('communityPayment.paymentScope')">
        <el-select v-model="form.payType" disabled style="width: 100%">
          <el-option :label="$t('communityPayment.communityFee')" value="1001" />
          <el-option :label="$t('communityPayment.tempParkingFee')" value="2002" />
          <el-option :label="$t('communityPayment.specificFee')" value="3003" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="form.payType === '3003'" :label="$t('communityPayment.selectFeeItem')">
        <el-checkbox-group v-model="form.configIds">
          <el-checkbox v-for="fee in feeConfigs" :key="fee.configId" :label="fee.configId">
            {{ fee.feeName }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item :label="$t('communityPayment.status')" prop="state">
        <el-select v-model="form.state" :placeholder="$t('editCommunityPayment.statusPlaceholder')" style="width: 100%">
          <el-option :label="$t('communityPayment.enabled')" value="Y" />
          <el-option :label="$t('communityPayment.disabled')" value="N" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('communityPayment.instruction')">
        <el-input v-model="form.remark" type="textarea" :rows="3"
          :placeholder="$t('addCommunityPayment.remarkPlaceholder')" />
      </el-form-item>
    </el-form>

    <div slot="footer">
      <el-button @click="visible = false">{{ $t('communityPayment.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ $t('communityPayment.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateAdminPaymentPool, listPaymentKey, listPaymentAdapt, queryAdminFeeConfigs } from '@/api/fee/communityPaymentApi'
import FileUpload from '@/components/upload/FileUpload'

export default {
  name: 'EditCommunityPayment',
  components: { FileUpload },
  data() {
    return {
      visible: false,
      form: {
        ppId: '',
        communityId: '',
        paymentName: '',
        paymentType: '',
        certPath: '',
        state: '',
        remark: '',
        payType: '',
        configIds: []
      },
      paymentTypes: [],
      paymentKeys: [],
      feeConfigs: [],
      rules: {
        paymentName: [
          { required: true, message: this.$t('communityPayment.requiredField'), trigger: 'blur' },
          { max: 64, message: this.$t('communityPayment.maxLength64'), trigger: 'blur' }
        ],
        state: [
          { required: true, message: this.$t('communityPayment.requiredField'), trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.loadPaymentTypes()
  },
  methods: {
    open(row) {
      this.form = { ...row }
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
        this.loadPaymentKeys()
        this.loadFeeConfigs()
        if (row.certPath && this.$refs.uploader) {
          this.$refs.uploader.setFileName(row.certPath)
        }
      })
    },
    resetForm() {
      this.form = {
        ppId: '',
        communityId: '',
        paymentName: '',
        paymentType: '',
        certPath: '',
        state: '',
        remark: '',
        payType: '',
        configIds: []
      }
      this.paymentKeys = []
      if (this.$refs.uploader) {
        this.$refs.uploader.clearFile()
      }
    },
    async loadPaymentTypes() {
      try {
        const res = await listPaymentAdapt({ page: 1, row: 100 })
        this.paymentTypes = res.data || []
      } catch (error) {
        console.error('Failed to load payment types:', error)
      }
    },
    async loadPaymentKeys() {
      if (!this.form.paymentType) return

      try {
        const res = await listPaymentKey({
          paymentType: this.form.paymentType,
          page: 1,
          row: 100
        })
        this.paymentKeys = res.data || []

        // Load existing values
        if (this.form.values) {
          this.paymentKeys.forEach(key => {
            const value = this.form.values.find(v => v.columnKey === key.columnKey)
            if (value) {
              key.columnValue = value.columnValue
            }
          })
        }
      } catch (error) {
        console.error('Failed to load payment keys:', error)
        this.paymentKeys = []
      }
    },
    async loadFeeConfigs() {
      if (!this.form.communityId) return

      try {
        const res = await queryAdminFeeConfigs({
          communityId: this.form.communityId,
          isDefault: 'F',
          page: 1,
          row: 100
        })
        this.feeConfigs = res.feeConfigs || []
      } catch (error) {
        console.error('Failed to load fee configs:', error)
        this.feeConfigs = []
      }
    },
    handleUploadSuccess(file) {
      this.form.certPath = file.realFileName
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          const payload = {
            ...this.form,
            paymentKeys: this.paymentKeys
          }
          await updateAdminPaymentPool(payload)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error || this.$t('common.updateFailed'))
        }
      })
    }
  }
}
</script>