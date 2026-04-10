<template>
  <el-dialog
    :title="$t('selectFeeConfig.title')"
    :visible.sync="visible"
    width="60%"
    @close="handleClose"
  >
    <div class="fee-config-container">
      <el-form :model="form" label-width="120px">
        <el-form-item :label="$t('selectFeeConfig.feeType')" required>
          <el-select
            v-model="form.feeTypeCd"
            style="width: 100%"
            @change="changeFeeType"
          >
            <el-option
              v-for="item in feeTypes"
              :key="item.statusCd"
              :label="item.name"
              :value="item.statusCd"
              :disabled="item.statusCd === '888800010008' || item.statusCd === '888800010017'"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('selectFeeConfig.feeItem')" required>
          <el-select
            v-model="form.configId"
            style="width: 100%"
            @change="selectFeeConfig"
          >
            <el-option
              v-for="item in feeConfigs"
              :key="item.configId"
              :label="item.feeName"
              :value="item.configId"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          v-if="form.computingFormula === '4004'"
          :label="$t('selectFeeConfig.amount')"
          required
        >
          <el-input
            v-model="form.amount"
            :placeholder="$t('selectFeeConfig.amountPlaceholder')"
          ></el-input>
        </el-form-item>

        <el-form-item :label="$t('selectFeeConfig.startTime')" required>
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            style="width: 100%"
            :placeholder="$t('selectFeeConfig.startTimePlaceholder')"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>

        <el-form-item
          v-if="form.feeFlag === '2006012'"
          :label="$t('selectFeeConfig.endTime')"
          required
        >
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            style="width: 100%"
            :placeholder="$t('selectFeeConfig.endTimePlaceholder')"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
      </el-form>

      <div class="dialog-footer">
        <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="saveFeeConfig">{{ $t('common.submit') }}</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'
import { listFeeConfigs } from '@/api/room/ownerExitRoomApi'

export default {
  name: 'SelectFeeConfig',
  data() {
    return {
      visible: false,
      form: {
        feeTypeCd: '',
        configId: '',
        configName: '',
        locationTypeCdName: '',
        startTime: '',
        endTime: '',
        computingFormula: '',
        amount: '',
        feeFlag: '',
        call: null
      },
      feeTypes: [],
      feeConfigs: []
    }
  },
  created() {
    this.getDictData()
  },
  methods: {
    open(call) {
      this.form.call = call
      this.visible = true
    },
    handleClose() {
      this.clearForm()
    },
    async getDictData() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.feeTypes = data.filter(item => 
          item.statusCd !== '888800010015' && item.statusCd !== '888800010016'
        )
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async changeFeeType(feeTypeCd) {
      this.form.configId = ''
      try {
        const params = {
          page: 1,
          row: 500,
          communityId: getCommunityId(),
          feeTypeCd: feeTypeCd,
          isDefault: 'F',
          valid: '1'
        }
        const res = await listFeeConfigs(params)
        this.feeConfigs = res.feeConfigs
      } catch (error) {
        console.error('获取费用配置失败:', error)
      }
    },
    selectFeeConfig(configId) {
      this.form.endTime = ''
      const selectedConfig = this.feeConfigs.find(item => item.configId === configId)
      if (selectedConfig) {
        this.form.feeName = selectedConfig.feeName
        this.form.feeFlag = selectedConfig.feeFlag
        this.form.computingFormula = selectedConfig.computingFormula
      }
    },
    saveFeeConfig() {
      if (!this.form.feeTypeCd) {
        this.$message.error(this.$t('selectFeeConfig.validate.feeTypeRequired'))
        return
      }
      if (!this.form.configId) {
        this.$message.error(this.$t('selectFeeConfig.validate.feeItemRequired'))
        return
      }
      if (!this.form.startTime) {
        this.$message.error(this.$t('selectFeeConfig.validate.startTimeRequired'))
        return
      }
      if (this.form.computingFormula === '4004' && !this.form.amount) {
        this.$message.error(this.$t('selectFeeConfig.validate.amountRequired'))
        return
      }

      this.$emit('notifyFeeConfig', this.form)
      this.visible = false
    },
    clearForm() {
      this.form = {
        feeTypeCd: '',
        configId: '',
        configName: '',
        locationTypeCdName: '',
        startTime: '',
        endTime: '',
        computingFormula: '',
        amount: '',
        feeFlag: '',
        call: null
      }
      this.feeConfigs = []
    }
  }
}
</script>

<style lang="scss" scoped>
.fee-config-container {
  padding: 20px;

  .dialog-footer {
    text-align: right;
    margin-top: 20px;
  }
}
</style>