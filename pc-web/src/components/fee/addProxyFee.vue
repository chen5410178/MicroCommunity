<template>
  <el-dialog :title="$t('addProxyFee.title')" :visible.sync="visible" width="40%" :before-close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('addProxyFee.feeType')" prop="feeTypeCd">
        <el-select v-model="formData.feeTypeCd" @change="handleFeeTypeChange"
          :placeholder="$t('addProxyFee.feeTypePlaceholder')" style="width: 100%">
          <el-option v-for="item in feeTypeOptions" :key="item.value" :label="item.label"
            :value="item.value"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('addProxyFee.feeItem')" prop="configId">
        <el-select v-model="formData.configId" @change="handleConfigChange"
          :placeholder="$t('addProxyFee.feeItemPlaceholder')" style="width: 100%">
          <el-option v-for="item in feeConfigOptions" :key="item.configId" :label="item.feeName"
            :value="item.configId"></el-option>
        </el-select>
        <span class="tip">{{ $t('addProxyFee.feeItemTip') }}</span>
      </el-form-item>

      <el-form-item :label="$t('addProxyFee.chargeObject')">
        <el-input v-model="formData.ownerName" disabled
          :placeholder="$t('addProxyFee.chargeObjectPlaceholder')"></el-input>
      </el-form-item>

      <template v-if="formData.feeTypeCd !== '888800010014'">
        <el-form-item :label="$t('addProxyFee.consumption')" prop="consumption">
          <el-input v-model="formData.consumption" @blur="handleConsumptionBlur"
            :placeholder="$t('addProxyFee.consumptionPlaceholder')"></el-input>
        </el-form-item>

        <el-form-item :label="$t('addProxyFee.amount')" prop="amount">
          <el-input v-model="formData.amount" @blur="handleAmountBlur"
            :placeholder="$t('addProxyFee.amountPlaceholder')"></el-input>
        </el-form-item>
      </template>

      <template v-else>
        <el-form-item :label="$t('addProxyFee.quantity')" prop="consumption">
          <el-input v-model="formData.consumption" @blur="handleConsumptionBlur"
            :placeholder="$t('addProxyFee.consumptionPlaceholder')"></el-input>
        </el-form-item>

        <el-form-item :label="$t('addProxyFee.amount')">
          <el-input v-model="formData.amount" disabled :placeholder="$t('addProxyFee.amountPlaceholder')"></el-input>
        </el-form-item>
      </template>

      <el-form-item :label="$t('addProxyFee.startTime')" prop="startTime">
        <el-date-picker v-model="formData.startTime" type="date" :placeholder="$t('addProxyFee.startTimePlaceholder')"
          value-format="yyyy-MM-dd" @change="validateStartTime"></el-date-picker>
      </el-form-item>

      <el-form-item :label="$t('addProxyFee.endTime')" prop="endTime">
        <el-date-picker v-model="formData.endTime" type="date" :placeholder="$t('addProxyFee.endTimePlaceholder')"
          value-format="yyyy-MM-dd" @change="validateEndTime"></el-date-picker>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveProxyFee, listFeeConfigs } from '@/api/fee/addProxyFeeApi'
import { getCommunityId } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'AddProxyFee',
  props: {
    callBackListener: String,
    callBackFunction: String
  },
  data() {
    return {
      visible: false,
      formData: {
        remark: '',
        roomId: '',
        objId: '',
        objName: '',
        feeTypeCd: '',
        feeConfigs: [],
        amount: '',
        consumption: '',
        configId: '',
        ownerName: '',
        objType: '3333',
        startTime: dateFormat(new Date()),
        endTime: dateFormat(new Date())
      },
      feeTypeOptions: [
        { value: '888800010015', label: this.$t('addProxyFee.waterFee') },
        { value: '888800010016', label: this.$t('addProxyFee.electricityFee') },
        { value: '888800010014', label: this.$t('addProxyFee.otherFee') }
      ],
      feeConfigOptions: [],
      rules: {
        amount: [
          { required: true, message: this.$t('addProxyFee.validate.amountRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('addProxyFee.validate.amountFormat'), trigger: 'blur' }
        ],
        consumption: [
          { required: true, message: this.$t('addProxyFee.validate.consumptionRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('addProxyFee.validate.consumptionFormat'), trigger: 'blur' }
        ],
        objId: [
          { required: true, message: this.$t('addProxyFee.validate.roomRequired'), trigger: 'blur' }
        ],
        configId: [
          { required: true, message: this.$t('addProxyFee.validate.feeRequired'), trigger: 'change' }
        ],
        startTime: [
          { required: true, message: this.$t('addProxyFee.validate.startTimeRequired'), trigger: 'blur' }
        ],
        endTime: [
          { required: true, message: this.$t('addProxyFee.validate.endTimeRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(params) {
      this.resetForm()
      if (params.objType) {
        this.formData.objType = params.objType
      }
      this.formData.roomId = params.roomId
      this.formData.objId = params.roomId
      this.formData.objName = params.roomName
      this.formData.ownerName = `${params.roomName}(${params.ownerName})`
      this.visible = true
    },

    handleFeeTypeChange(value) {
      this.formData.amount = ''
      this.formData.consumption = ''
      this.formData.configId = ''
      this.loadFeeConfigs(value)
    },

    loadFeeConfigs(feeTypeCd) {
      listFeeConfigs({
        page: 1,
        row: 20,
        communityId: getCommunityId(),
        feeTypeCd,
        isDefault: 'F',
        computingFormula: '6006',
        valid: '1'
      }).then(response => {
        this.feeConfigOptions = response.feeConfigs
      })
    },

    handleConfigChange() {
      this.formData.amount = ''
      this.formData.consumption = ''
    },

    getCurrentConfig() {
      return this.feeConfigOptions.find(
        item => this.formData.configId === item.configId && item.computingFormula === '6006'
      )
    },

    handleAmountBlur() {
      const config = this.getCurrentConfig()
      const amount = parseFloat(this.formData.amount)

      if (!config) {
        this.$message.error(this.$t('addProxyFee.validate.invalidConfig'))
        this.formData.amount = ''
        this.formData.consumption = ''
        return
      }

      if (amount < config.additionalAmount) {
        this.$message.error(this.$t('addProxyFee.validate.amountTooSmall'))
        this.formData.amount = ''
        this.formData.consumption = ''
        return
      }

      const consumption = (amount - config.additionalAmount) / config.squarePrice
      this.formData.consumption = consumption.toFixed(2)
    },

    handleConsumptionBlur() {
      const config = this.getCurrentConfig()
      const consumption = parseFloat(this.formData.consumption)

      if (!config) {
        this.$message.error(this.$t('addProxyFee.validate.invalidConfig'))
        this.formData.amount = ''
        this.formData.consumption = ''
        return
      }

      const amount = config.squarePrice * consumption + parseFloat(config.additionalAmount)
      this.formData.amount = amount.toFixed(2)
    },

    validateStartTime(value) {
      const start = new Date(value)
      const end = new Date(this.formData.endTime)
      if (start >= end) {
        this.$message.error(this.$t('addProxyFee.validate.startBeforeEnd'))
        this.formData.startTime = ''
      }
    },

    validateEndTime(value) {
      const start = new Date(this.formData.startTime)
      const end = new Date(value)
      if (start >= end) {
        this.$message.error(this.$t('addProxyFee.validate.endAfterStart'))
        this.formData.endTime = ''
      }
    },

    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return

        const params = {
          ...this.formData,
          communityId: getCommunityId()
        }



        saveProxyFee(params).then(() => {
           this.$message.success(this.$t('common.operationSuccess'))
           this.$emit('success')
          this.handleClose()
        }).catch(error => {
          this.$message.error(error.message)
        })
      })
    },

    resetForm() {
      if (this.$refs.form) {
        //this.$refs.form.resetFields()
      }
      this.formData = {
        remark: '',
        roomId: '',
        objId: '',
        objName: '',
        feeTypeCd: '',
        feeConfigs: [],
        amount: '',
        consumption: '',
        configId: '',
        ownerName: '',
        objType: '3333',
        startTime: dateFormat(new Date()),
        endTime: dateFormat(new Date())
      }
    },

    handleClose() {
      this.visible = false
      this.resetForm()
    }
  }
}
</script>

<style scoped>
.tip {
  font-size: 12px;
  color: #909399;
}
.el-form-item{
  width: 90%;
  text-align: left;
}
.el-date-editor{  
  width: 100%;
}
</style>