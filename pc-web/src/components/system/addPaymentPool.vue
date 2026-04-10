<template>
  <el-dialog :title="$t('paymentPool.add.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" class="text-left">
      <el-form-item :label="$t('paymentPool.add.paymentName')" prop="paymentName">
        <el-input v-model="formData.paymentName" :placeholder="$t('paymentPool.add.paymentNamePlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('paymentPool.add.paymentType')" prop="paymentType">
        <el-select v-model="formData.paymentType" :placeholder="$t('paymentPool.add.paymentTypePlaceholder')"
          style="width:100%" @change="handlePaymentTypeChange">
          <el-option v-for="item in paymentTypes" :key="item.paymentType" :label="item.name"
            :value="item.paymentType" />
        </el-select>
      </el-form-item>

      <el-form-item v-for="(item, index) in paymentKeys" :key="index" :label="item.name">
        <el-input v-model="item.columnValue" type="textarea" :placeholder="item.remark" :rows="3" />
      </el-form-item>

      <el-form-item v-if="formData.paymentType === 'WECHAT'" :label="$t('paymentPool.add.certFile')">
        <upload-file ref="uploadFile" @notify="handleNotifyCert" />
      </el-form-item>

      <el-form-item :label="$t('paymentPool.add.payRange')" prop="payType">
        <el-select v-model="formData.payType" :placeholder="$t('paymentPool.add.payRangePlaceholder')"
          style="width:100%">
          <el-option :label="$t('paymentPool.add.communityFee')" value="1001" />
          <el-option :label="$t('paymentPool.add.tempCarFee')" value="2002" />
          <el-option :label="$t('paymentPool.add.specifiedFee')" value="3003" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="formData.payType === '3003'" :label="$t('paymentPool.add.feeItems')">
        <el-checkbox-group v-model="formData.configIds">
          <el-checkbox v-for="item in feeConfigs" :key="item.configId" :label="item.configId">
            {{ item.feeName }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item :label="$t('paymentPool.add.remark')">
        <el-input v-model="formData.remark" type="textarea" :placeholder="$t('paymentPool.add.remarkPlaceholder')"
          :rows="3" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { savePaymentPool, listPaymentKey, listFeeConfigs, listPaymentAdapt } from '@/api/system/paymentPoolApi'
import UploadFile from '@/components/upload/uploadFile'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddPaymentPool',
  components: {
    UploadFile
  },
  props: {
    callBackListener: {
      type: String,
      default: ''
    },
    callBackFunction: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      visible: false,
      formData: {
        paymentName: '',
        paymentType: '',
        certPath: '',
        state: 'Y',
        remark: '',
        payType: '1001',
        configIds: [],
        communityId: ''
      },
      paymentTypes: [],
      paymentKeys: [],
      feeConfigs: [],
      rules: {
        paymentName: [
          { required: true, message: this.$t('paymentPool.validate.paymentNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('paymentPool.validate.paymentNameMaxLength'), trigger: 'blur' }
        ],
        paymentType: [
          { required: true, message: this.$t('paymentPool.validate.paymentTypeRequired'), trigger: 'change' }
        ],
        payType: [
          { required: true, message: this.$t('paymentPool.validate.payTypeRequired'), trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.formData.communityId = getCommunityId()
    this.getPaymentTypes()
  },
  methods: {
    open() {
      this.visible = true
      this.getFeeConfigs()
      if (this.$refs.uploadFile) {
        this.$refs.uploadFile.clear()
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        paymentName: '',
        paymentType: '',
        certPath: '',
        state: 'Y',
        remark: '',
        payType: '1001',
        configIds: [],
        communityId: getCommunityId()
      }
      this.paymentKeys = []
    },
    async getPaymentTypes() {
      try {
        const { data } = await listPaymentAdapt({
          page: 1,
          row: 100,
        })
        this.paymentTypes = data
      } catch (error) {
        console.error('获取支付类型失败:', error)
      }
    },
    async getFeeConfigs() {
      try {
        const params = {
          page: 1,
          row: 100,
          isDefault: 'F',
          communityId: this.formData.communityId
        }
        const { feeConfigs } = await listFeeConfigs(params)
        this.feeConfigs = feeConfigs
      } catch (error) {
        console.error('获取费用配置失败:', error)
      }
    },
    async handlePaymentTypeChange() {
      if (!this.formData.paymentType) return

      try {
        const params = {
          paymentType: this.formData.paymentType,
          page: 1,
          row: 100
        }
        const { data } = await listPaymentKey(params)
        this.paymentKeys = data.map(item => ({
          ...item,
          columnValue: ''
        }))
      } catch (error) {
        console.error('获取支付密钥失败:', error)
      }
    },
    handleNotifyCert(param) {
      this.formData.certPath = param.realFileName
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          const params = {
            ...this.formData,
            paymentKeys: this.paymentKeys.map(item => ({
              columnKey: item.columnKey,
              columnValue: item.columnValue
            }))
          }
          await savePaymentPool(params)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message || this.$t('paymentPool.add.error'))
        }
      })
    }
  }
}
</script>