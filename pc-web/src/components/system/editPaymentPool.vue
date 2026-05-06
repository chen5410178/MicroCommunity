<template>
  <el-dialog :title="$t('paymentPool.edit.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" class="text-left">
      <el-form-item :label="$t('paymentPool.edit.paymentName')" prop="paymentName">
        <el-input v-model="formData.paymentName" :placeholder="$t('paymentPool.edit.paymentNamePlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('paymentPool.edit.paymentType')" prop="paymentType">
        <el-select v-model="formData.paymentType" :placeholder="$t('paymentPool.edit.paymentTypePlaceholder')"
          style="width:100%" disabled>
          <el-option v-for="item in paymentTypes" :key="item.paymentType" :label="item.name"
            :value="item.paymentType" />
        </el-select>
      </el-form-item>

      <el-form-item v-for="(item, index) in paymentKeys" :key="index" :label="item.name">
        <el-input v-model="item.columnValue" type="textarea" :placeholder="item.remark" :rows="3" />
      </el-form-item>

      <el-form-item v-if="formData.paymentType === 'WECHAT'" :label="$t('paymentPool.edit.certFile')">
        <upload-file ref="uploadFile" @notify="handleNotifyCert" />
        <div v-if="formData.certPath" class="cert-tip">
          {{ $t('paymentPool.edit.currentCert') }}: {{ formData.certPath }}
        </div>
      </el-form-item>

      <el-form-item :label="$t('paymentPool.edit.payRange')" prop="payType">
        <el-select v-model="formData.payType" :placeholder="$t('paymentPool.edit.payRangePlaceholder')"
          style="width:100%" disabled>
          <el-option :label="$t('paymentPool.edit.communityFee')" value="1001" />
          <el-option :label="$t('paymentPool.edit.specifiedFee')" value="3003" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="formData.payType === '3003'" :label="$t('paymentPool.edit.feeItems')">
        <el-checkbox-group v-model="formData.configIds">
          <el-checkbox v-for="item in feeConfigs" :key="item.configId" :label="item.configId">
            {{ item.feeName }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item :label="$t('paymentPool.edit.state')" prop="state">
        <el-select v-model="formData.state" :placeholder="$t('paymentPool.edit.statePlaceholder')" style="width:100%">
          <el-option :label="$t('paymentPool.edit.enable')" value="Y" />
          <el-option :label="$t('paymentPool.edit.disable')" value="N" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('paymentPool.edit.remark')">
        <el-input v-model="formData.remark" type="textarea" :placeholder="$t('paymentPool.edit.remarkPlaceholder')"
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
import { updatePaymentPool, listPaymentKey, listFeeConfigs, getPaymentPoolDetail, listPaymentAdapt } from '@/api/system/paymentPoolApi'
import UploadFile from '@/components/upload/uploadFile'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditPaymentPool',
  components: {
    UploadFile
  },
  data() {
    return {
      visible: false,
      formData: {
        ppId: '',
        paymentName: '',
        paymentType: '',
        certPath: '',
        state: '',
        remark: '',
        payType: '',
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
        state: [
          { required: true, message: this.$t('paymentPool.validate.stateRequired'), trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.formData.communityId = getCommunityId()
    this.getPaymentTypes()
  },
  methods: {
    open(row) {
      this.visible = true
      this.formData.ppId = row.ppId
      this.getPaymentPoolDetail()
      this.getFeeConfigs()
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        ppId: '',
        paymentName: '',
        paymentType: '',
        certPath: '',
        state: '',
        remark: '',
        payType: '',
        configIds: [],
        communityId: getCommunityId()
      }
      this.paymentKeys = []
      if (this.$refs.uploadFile) {
        this.$refs.uploadFile.clear()
      }
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
    async getPaymentPoolDetail() {
      try {
        const params = {
          ppId: this.formData.ppId,
          communityId: this.formData.communityId
        }
        const { data } = await getPaymentPoolDetail(params)
        this.formData = {
          ...this.formData,
          paymentName: data[0].paymentName,
          paymentType: data[0].paymentType,
          certPath: data[0].certPath,
          state: data[0].state,
          remark: data[0].remark,
          payType: data[0].payType
        }

        // 加载支付密钥
        await this.loadPaymentKeys()

        // 设置支付密钥值
        if (data[0].values) {
          data[0].values.forEach(value => {
            const key = this.paymentKeys.find(k => k.columnKey === value.columnKey)
            if (key) {
              key.columnValue = value.columnValue
            }
          })
        }

        // 设置费用项
        if (data[0].configs) {
          this.formData.configIds = data[0].configs.map(c => c.configId)
        }

        // 如果有证书路径，通知上传组件
        if (this.formData.certPath && this.$refs.uploadFile) {
          this.$refs.uploadFile.fileName = this.formData.certPath
          this.$refs.uploadFile.realFileName = this.formData.certPath
          this.$refs.uploadFile.progress = 100
        }
      } catch (error) {
        console.error('获取支付池详情失败:', error)
      }
    },
    async loadPaymentKeys() {
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
          await updatePaymentPool(params)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message || this.$t('paymentPool.edit.error'))
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.cert-tip {
  margin-top: 5px;
  font-size: 12px;
  color: #909399;
}
</style>