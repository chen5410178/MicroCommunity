<template>
  <el-dialog :title="$t('contractCreateFeeAdd.title')" :visible.sync="visible" width="800px" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" label-position="right" class="text-left">
      <el-form-item :label="$t('contractCreateFeeAdd.feeTypeCd')" prop="feeTypeCd">
        <el-select v-model="formData.feeTypeCd" :placeholder="$t('contractCreateFeeAdd.selectFeeType')"
          style="width: 100%" @change="handleFeeTypeChange">
          <el-option v-for="item in feeTypeOptions" :key="item.statusCd" :label="item.name" :value="item.statusCd"
            :disabled="item.statusCd === '888800010008' ||
              item.statusCd === '888800010017'
              " />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('contractCreateFeeAdd.configId')" prop="configId">
        <el-select v-model="formData.configId" :placeholder="$t('contractCreateFeeAdd.selectFeeConfig')"
          style="width: 100%" @change="handleConfigChange">
          <el-option v-for="item in feeConfigOptions" :key="item.configId" :label="item.feeName"
            :value="item.configId" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="formData.computingFormula === '4004'" :label="$t('contractCreateFeeAdd.amount')"
        prop="amount">
        <el-input v-model.trim="formData.amount" :placeholder="$t('contractCreateFeeAdd.inputAmount')" />
      </el-form-item>

      <el-form-item v-if="isMore" :label="$t('contractCreateFeeAdd.contractState')" prop="contractState">
        <el-checkbox-group v-model="formData.contractState">
          <el-checkbox label="2001">
            {{ $t('contractCreateFeeAdd.state2001') }}
          </el-checkbox>
          <el-checkbox label="2003">
            {{ $t('contractCreateFeeAdd.state2003') }}
          </el-checkbox>
          <el-checkbox label="2005">
            {{ $t('contractCreateFeeAdd.state2005') }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item :label="$t('contractCreateFeeAdd.startTime')" prop="startTime">
        <el-date-picker v-model="formData.startTime" type="datetime"
          :placeholder="$t('contractCreateFeeAdd.selectStartTime')" value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%" @change="validateStartTime" />
      </el-form-item>

      <el-form-item :label="$t('contractCreateFeeAdd.endTime')" prop="endTime" :rules="formData.feeFlag === '2006012'
        ? [
          {
            required: true,
            message: $t('contractCreateFeeAdd.endTimeRequired'),
            trigger: 'blur'
          }
        ]
        : []
        ">
        <el-date-picker v-model="formData.endTime" type="datetime"
          :placeholder="$t('contractCreateFeeAdd.selectEndTime')" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%"
          :disabled="!formData.feeFlag" @change="validateEndTime" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.submit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveContractCreateFee, listFeeConfigs } from '@/api/fee/contractCreateFeeApi'
import { getCommunityId, getDict } from '@/api/community/communityApi'

export default {
  name: 'ContractCreateFeeAdd',
  data() {
    return {
      visible: false,
      isMore: false,
      formData: {
        feeTypeCds: [],
        feeConfigs: [],
        contractId: '',
        feeTypeCd: '',
        configId: '',
        contractState: ['2001'],
        startTime: '',
        feeFlag: '',
        endTime: '',
        computingFormula: '',
        amount: ''
      },
      feeTypeOptions: [],
      feeConfigOptions: [],
      rules: {
        feeTypeCd: [
          {
            required: true,
            message: this.$t('contractCreateFeeAdd.feeTypeRequired'),
            trigger: 'change'
          }
        ],
        configId: [
          {
            required: true,
            message: this.$t('contractCreateFeeAdd.configRequired'),
            trigger: 'change'
          }
        ],
        startTime: [
          {
            required: true,
            message: this.$t('contractCreateFeeAdd.startTimeRequired'),
            trigger: 'change'
          }
        ],
        amount: [
          {
            required: true,
            message: this.$t('contractCreateFeeAdd.amountRequired'),
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    open(contract, isMore) {
      this.isMore = isMore
      if (contract) {
        this.formData.contractId = contract.contractId
      }
      this.visible = true
      this.loadFeeTypes()
    },
    async loadFeeTypes() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.feeTypeOptions = data.filter(
          item =>
            item.statusCd !== '888800010015' && item.statusCd !== '888800010016'
        )
      } catch (error) {
        console.error('Failed to load fee types:', error)
      }
    },
    async handleFeeTypeChange(value) {
      this.formData.configId = ''
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: getCommunityId(),
          feeTypeCd: value,
          isDefault: 'F',
          valid: '1'
        }
        const { feeConfigs } = await listFeeConfigs(params)
        this.feeConfigOptions = feeConfigs
      } catch (error) {
        console.error('Failed to load fee configs:', error)
      }
    },
    handleConfigChange(value) {
      this.formData.endTime = ''
      const config = this.feeConfigOptions.find(item => item.configId === value)
      if (config) {
        this.formData.feeFlag = config.feeFlag
        this.formData.computingFormula = config.computingFormula
      }
    },
    validateStartTime(value) {
      if (value && this.formData.endTime) {
        const start = new Date(value).getTime()
        const end = new Date(this.formData.endTime).getTime()
        if (start >= end) {
          this.$message.error(this.$t('contractCreateFeeAdd.timeError'))
          this.formData.startTime = ''
        }
      }
    },
    validateEndTime(value) {
      if (value && this.formData.startTime) {
        const start = new Date(this.formData.startTime).getTime()
        const end = new Date(value).getTime()
        if (start >= end) {
          this.$message.error(this.$t('contractCreateFeeAdd.timeError'))
          this.formData.endTime = ''
        }
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        const params = {
          ...this.formData,
          communityId: getCommunityId(),
          contractState: this.formData.contractState.join(',')
        }
        const data  = await saveContractCreateFee(params)
        this.$message.success(
          this.$t('contractCreateFeeAdd.success', {
            total: data.totalRoom,
            success: data.successRoom,
            error: data.errorRoom
          })
        )
        this.$emit('success')
        this.visible = false
      } catch (error) {
        console.error('Submit failed:', error)
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        feeTypeCds: [],
        feeConfigs: [],
        contractId: '',
        feeTypeCd: '',
        configId: '',
        contractState: ['2001'],
        startTime: '',
        feeFlag: '',
        endTime: '',
        computingFormula: '',
        amount: ''
      }
    }
  }
}
</script>