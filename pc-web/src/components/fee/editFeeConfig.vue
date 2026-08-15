<template>
  <el-dialog :title="$t('editFeeConfig.title')" :visible.sync="dialogVisible" width="900px" :before-close="handleClose">
    <el-form ref="editFeeConfigForm" :model="editFeeConfigInfo" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editFeeConfig.feeType')" prop="ftId">
            <el-select v-model="editFeeConfigInfo.ftId" :placeholder="$t('editFeeConfig.feeTypePlaceholder')"
              style="width: 100%" @change="_changeEditFeeTypeCd" :disabled="editFeeConfigInfo.isDefault === 'T'"
              clearable>
              <el-option v-for="(item, index) in editFeeConfigInfo.ftIds" :key="index" :label="item.ftName"
                :value="item.ftId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editFeeConfig.feeName')" prop="feeName">
            <el-input v-model="editFeeConfigInfo.feeName" :placeholder="$t('editFeeConfig.feeNamePlaceholder')"
              :disabled="editFeeConfigInfo.isDefault === 'T'" clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editFeeConfig.feeFlag')" prop="feeFlag">
            <el-select v-model="editFeeConfigInfo.feeFlag" :placeholder="$t('editFeeConfig.feeFlagPlaceholder')"
              style="width: 100%" :disabled="editFeeConfigInfo.isDefault === 'T'" clearable>
              <el-option v-for="(item, index) in editFeeConfigInfo.feeFlags" :key="index" :label="item.name"
                :value="item.statusCd" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editFeeConfig.paymentType')" prop="paymentCd">
            <el-select v-model="editFeeConfigInfo.paymentCd" :placeholder="$t('editFeeConfig.paymentTypePlaceholder')"
              style="width: 100%" clearable>
              <el-option v-for="(item, index) in editFeeConfigInfo.paymentCds" :key="index" :label="item.name"
                :value="item.statusCd" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="editFeeConfigInfo.feeFlag !== '2006012'">
        <el-col :span="12">
          <el-form-item :label="$t('editFeeConfig.paymentCycle')" prop="paymentCycle">
            <el-input v-model="editFeeConfigInfo.paymentCycle"
              :placeholder="$t('editFeeConfig.paymentCyclePlaceholder')" clearable>
              <template slot="append">月</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="editFeeConfigInfo.feeFlag === '1003006'">
          <el-form-item :label="$t('editFeeConfig.prepaymentPeriod')" prop="prepaymentPeriod">
            <el-select v-model="editFeeConfigInfo.prepaymentPeriod"
              :placeholder="$t('editFeeConfig.prepaymentPeriodPlaceholder')" style="width: 100%" clearable>
              <el-option v-for="n in 12" :key="n" :label="String(n)" :value="String(n)" />
              <el-option :label="$t('editFeeConfig.toYearEnd')" value="1231" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editFeeConfig.unit')" prop="units">
            <el-input v-model="editFeeConfigInfo.units" :placeholder="$t('editFeeConfig.unitPlaceholder')" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editFeeConfig.billing')" prop="deductFrom">
            <el-select v-model="editFeeConfigInfo.deductFrom" :placeholder="$t('editFeeConfig.billingPlaceholder')"
              style="width: 100%" clearable>
              <el-option :label="$t('editFeeConfig.currentMonth')" value="C" />
              <el-option :label="$t('editFeeConfig.lastMonth')" value="H" />
              <el-option :label="$t('editFeeConfig.nextMonth')" value="N" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editFeeConfig.mobilePayment')" prop="payOnline">
            <el-select v-model="editFeeConfigInfo.payOnline" style="width: 100%" clearable>
              <el-option :label="$t('common.yes')" value="Y" />
              <el-option :label="$t('common.no')" value="N" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editFeeConfig.roundingMethod')" prop="scale">
            <el-select v-model="editFeeConfigInfo.scale" style="width: 100%" clearable>
              <el-option :label="$t('editFeeConfig.roundHalfUp')" value="1" />
              <el-option :label="$t('editFeeConfig.roundUp')" value="3" />
              <el-option :label="$t('editFeeConfig.roundDown')" value="4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editFeeConfig.decimalPlaces')" prop="decimalPlace">
            <el-select v-model="editFeeConfigInfo.decimalPlace" style="width: 100%" clearable>
              <el-option :label="$t('editFeeConfig.integer')" value="0" />
              <el-option :label="$t('editFeeConfig.oneDecimal')" value="1" />
              <el-option :label="$t('editFeeConfig.twoDecimals')" value="2" />
              <el-option :label="$t('editFeeConfig.threeDecimals')" value="3" />
              <el-option :label="$t('editFeeConfig.fourDecimals')" value="4" />
              <el-option :label="$t('editFeeConfig.fiveDecimals')" value="5" />
              <el-option :label="$t('editFeeConfig.sixDecimals')" value="6" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editFeeConfig.status')" prop="state">
            <el-select v-model="editFeeConfigInfo.state" style="width: 100%" clearable>
              <el-option :label="$t('editFeeConfig.enable')" value="Y" />
              <el-option :label="$t('editFeeConfig.disable')" value="N" />
              <el-option :label="$t('editFeeConfig.disableAndEnd')" value="NA" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('editFeeConfig.computingFormula')" prop="computingFormula">
            <el-select v-model="editFeeConfigInfo.computingFormula"
              :placeholder="$t('editFeeConfig.computingFormulaPlaceholder')" style="width: 100%"
              :disabled="editFeeConfigInfo.isDefault === 'T'" clearable>
              <el-option v-for="(item, index) in editFeeConfigInfo.computingFormulas" :key="index" :label="item.name"
                :value="item.statusCd" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-show="showUnitPriceField">
        <el-col :span="24">
          <el-form-item :label="$t('editFeeConfig.unitPrice')" prop="squarePrice">
            <el-input v-model="editFeeConfigInfo.squarePrice" :placeholder="$t('editFeeConfig.unitPricePlaceholder')"
              clearable>
              <template slot="append">元</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-show="showAdditionalAmountField">
        <el-col :span="24">
          <el-form-item :label="additionalAmountLabel" prop="additionalAmount">
            <el-input v-model="editFeeConfigInfo.additionalAmount"
              :placeholder="$t('editFeeConfig.additionalAmountPlaceholder')" clearable>
              <template slot="append">元</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-show="editFeeConfigInfo.computingFormula === '7007'">
        <el-col :span="24">
          <el-form-item :label="$t('editFeeConfig.formula')" prop="computingFormulaText">
            <el-input type="textarea" v-model="editFeeConfigInfo.computingFormulaText"
              :placeholder="$t('editFeeConfig.formulaPlaceholder')" :rows="4" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-show="editFeeConfigInfo.computingFormula === '7007'">
        <el-col :span="24">
          <div class="formula-explanation">
            <div><strong>{{ $t('editFeeConfig.explanation') }}</strong></div>
            <div>C - {{ $t('editFeeConfig.explanationC') }}</div>
            <div>F - {{ $t('editFeeConfig.explanationF') }}</div>
            <div>U - {{ $t('editFeeConfig.explanationU') }}</div>
            <div>R - {{ $t('editFeeConfig.explanationR') }}</div>
            <div>X - {{ $t('editFeeConfig.explanationX') }}</div>
            <div>L - {{ $t('editFeeConfig.explanationL') }}</div>
            <div>RLPC - {{ $t('editFeeConfig.explanationRLPC') }}</div>
            <div class="example">{{ $t('editFeeConfig.example') }}</div>
            <div class="example">(L-5)*5 + 10</div>
          </div>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('editFeeConfig.cancel') }}
      </el-button>
      <el-button type="primary" @click="editFeeConfig" :loading="loading">
        {{ $t('editFeeConfig.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'
import { updateFeeConfig, listFeeType } from '@/api/fee/feeConfigManageApi'

export default {
  name: 'EditFeeConfig',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      editFeeConfigInfo: {
        configId: '',
        feeTypeCd: '',
        feeName: '',
        feeFlag: '',
        startTime: '',
        endTime: '',
        computingFormula: '',
        squarePrice: '',
        additionalAmount: '0.00',
        isDefault: '',
        feeTypeCds: [],
        computingFormulas: [],
        feeFlags: [],
        billTypes: [],
        paymentCds: [],
        billType: '',
        paymentCycle: '',
        paymentCd: '',
        computingFormulaText: '',
        deductFrom: 'C',
        payOnline: 'Y',
        scale: '1',
        decimalPlace: '2',
        units: '元',
        prepaymentPeriod: '1',
        state: '',
        ftId: '',
        ftIds: []
      },
      rules: {
        ftId: [
          { required: true, message: this.$t('editFeeConfig.validate.feeTypeRequired'), trigger: 'change' }
        ],
        feeName: [
          { required: true, message: this.$t('editFeeConfig.validate.feeNameRequired'), trigger: 'blur' },
          { min: 1, max: 100, message: this.$t('editFeeConfig.validate.feeNameLength'), trigger: 'blur' }
        ],
        feeFlag: [
          { required: true, message: this.$t('editFeeConfig.validate.feeFlagRequired'), trigger: 'change' }
        ],
        paymentCd: [
          { required: true, message: this.$t('editFeeConfig.validate.paymentTypeRequired'), trigger: 'change' }
        ],
        paymentCycle: [
          { pattern: /^\d+$/, message: this.$t('editFeeConfig.validate.paymentCycleNumber'), trigger: 'blur' }
        ],
        prepaymentPeriod: [
          { pattern: /^\d+$/, message: this.$t('editFeeConfig.validate.prepaymentPeriodNumber'), trigger: 'blur' }
        ],
        computingFormula: [
          { required: true, message: this.$t('editFeeConfig.validate.computingFormulaRequired'), trigger: 'change' }
        ],
        squarePrice: [
          { pattern: /^\d+(\.\d{1,6})?$/, message: this.$t('editFeeConfig.validate.unitPriceFormat'), trigger: 'blur' }
        ],
        additionalAmount: [
          { pattern: /^\d+(\.\d{1,6})?$/, message: this.$t('editFeeConfig.validate.additionalAmountFormat'), trigger: 'blur' }
        ],
        deductFrom: [
          { required: true, message: this.$t('editFeeConfig.validate.billingRequired'), trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    showUnitPriceField() {
      const excludedFormulas = ['2002', '7007', '8008', '1101', '1102', '4004', '9009']
      return !excludedFormulas.includes(this.editFeeConfigInfo.computingFormula)
    },
    showAdditionalAmountField() {
      const excludedFormulas = ['7007', '8008', '1101', '1102', '4004', '9009']
      return !excludedFormulas.includes(this.editFeeConfigInfo.computingFormula)
    },
    additionalAmountLabel() {
      if (this.editFeeConfigInfo.computingFormula === '1001') {
        return this.$t('editFeeConfig.additionalFee')
      } else if (['1002', '1003', '1004'].includes(this.editFeeConfigInfo.computingFormula)) {
        return this.$t('editFeeConfig.scaleFactor')
      } else {
        return this.$t('editFeeConfig.fixedFee')
      }
    }
  },
  methods: {
    open(params) {
      console.log('打开编辑费用配置对话框，参数:', params)  
      this.dialogVisible = true
      this.$nextTick(async () => {
        this.resetForm()
        if (params) {
          Object.assign(this.editFeeConfigInfo, params)
          this.editFeeConfigInfo.startTime = this.formatDate(params.startTime)
          this.editFeeConfigInfo.endTime = this.formatDate(params.endTime)
        }
        await this.loadDictData()
        await this._loadEditFeeType()
      })
    },

    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },

    resetForm() {
      this.editFeeConfigInfo = {
        configId: '',
        feeTypeCd: '',
        feeName: '',
        feeFlag: '',
        startTime: '',
        endTime: '',
        computingFormula: '',
        squarePrice: '',
        additionalAmount: '0.00',
        isDefault: '',
        ftIds: [],
        computingFormulas: [],
        feeFlags: [],
        paymentCds: [],
        billType: '',
        paymentCycle: '',
        paymentCd: '',
        computingFormulaText: '',
        deductFrom: 'C',
        payOnline: 'Y',
        scale: '1',
        decimalPlace: '2',
        units: '元',
        prepaymentPeriod: '1',
        state: '',
        ftId: ''
      }
      if (this.$refs.editFeeConfigForm) {
        this.$refs.editFeeConfigForm.clearValidate()
      }
    },

    formatDate(timestamp) {
      if (!timestamp) return ''
      const date = new Date(timestamp)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },

    async loadDictData() {
      try {
        // 计算公式字典
        this.editFeeConfigInfo.computingFormulas = await getDict('pay_fee_config', 'computing_formula')

        // 费用标识字典
        this.editFeeConfigInfo.feeFlags = await getDict('pay_fee_config', 'fee_flag')

        // 付费类型字典
        this.editFeeConfigInfo.paymentCds = await getDict('pay_fee_config', 'payment_cd')

      } catch (error) {
        console.error('加载字典数据失败:', error)
        this.$message.error(this.$t('editFeeConfig.loadDictError'))
      }
    },

    async _loadEditFeeType() {
      try {
        const params = {
          communityId: getCommunityId(),
          ftId: '',
          page: 1,
          row: 100
        }

        const { data } = await listFeeType(params)
        this.editFeeConfigInfo.ftIds = data

      } catch (error) {
        console.error('加载费用类型失败:', error)
      }
    },

    _changeEditFeeTypeCd() {
      const ftIds = this.editFeeConfigInfo.ftIds
      ftIds.forEach(ft => {
        if (ft.ftId === this.editFeeConfigInfo.ftId) {
          this.editFeeConfigInfo.feeTypeCd = ft.feeTypeCd
        }
      })
    },

    async editFeeConfig() {
      try {
        const valid = await this.$refs.editFeeConfigForm.validate()
        if (!valid) return

        this.loading = true

        // 处理特殊公式的数据
        if (this.editFeeConfigInfo.computingFormula === '2002') {
          this.editFeeConfigInfo.squarePrice = "0.00"
        }
        if (['7007', '4004', '1101', '1102', '9009'].includes(this.editFeeConfigInfo.computingFormula)) {
          this.editFeeConfigInfo.squarePrice = "0.00"
          this.editFeeConfigInfo.additionalAmount = "0.00"
        }
        if (this.editFeeConfigInfo.feeFlag === '2006012') {
          this.editFeeConfigInfo.paymentCycle = '1'
        }
        if (this.editFeeConfigInfo.feeFlag === '1003006') {
          if (!this.editFeeConfigInfo.prepaymentPeriod) {
            this.$message.error(this.$t('editFeeConfig.validate.prepaymentPeriodRequired'))
            return
          }
        } else {
          this.editFeeConfigInfo.prepaymentPeriod = '1'
        }

        const params = {
          ...this.editFeeConfigInfo,
          communityId: getCommunityId(),
          feeName: this.editFeeConfigInfo.feeName.trim(),
          paymentCycle: this.editFeeConfigInfo.paymentCycle.trim()
        }

        await updateFeeConfig(params)

        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()

        // 通知父组件刷新列表
        this.$parent.$emit('success')

      } catch (error) {
        console.error('修改费用配置失败:', error)
        this.$message.error(error.message || this.$t('editFeeConfig.saveError'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.formula-explanation {
  font-size: 12px;
  line-height: 1.5;
  color: #666;

  .example {
    margin-top: 10px;
    font-style: italic;
    color: #999;
  }
}

::v-deep .el-dialog {
  .el-dialog__body {
    max-height: 70vh;
    overflow-y: auto;
  }

  .el-form-item {
    margin-bottom: 18px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>