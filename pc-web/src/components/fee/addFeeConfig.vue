<template>
  <el-dialog
    :title="$t('addFeeConfig.title')"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose"
  >
    <el-form
      ref="addFeeConfigForm"
      :model="addFeeConfigInfo"
      :rules="rules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item 
            :label="$t('addFeeConfig.feeType')" 
            prop="ftId"
          >
            <el-select
              v-model="addFeeConfigInfo.ftId"
              :placeholder="$t('addFeeConfig.feeTypePlaceholder')"
              style="width: 100%"
              @change="_changeAddFeeTypeCd"
              clearable
            >
              <el-option
                v-for="(item, index) in addFeeConfigInfo.ftIds"
                :key="index"
                :label="item.ftName"
                :value="item.ftId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item 
            :label="$t('addFeeConfig.feeName')" 
            prop="feeName"
          >
            <el-input
              v-model="addFeeConfigInfo.feeName"
              :placeholder="$t('addFeeConfig.feeNamePlaceholder')"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item 
            :label="$t('addFeeConfig.feeFlag')" 
            prop="feeFlag"
          >
            <el-select
              v-model="addFeeConfigInfo.feeFlag"
              :placeholder="$t('addFeeConfig.feeFlagPlaceholder')"
              style="width: 100%"
              clearable
            >
              <el-option
                v-for="(item, index) in addFeeConfigInfo.feeFlags"
                :key="index"
                :label="item.name"
                :value="item.statusCd"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item 
            :label="$t('addFeeConfig.paymentType')" 
            prop="paymentCd"
          >
            <el-select
              v-model="addFeeConfigInfo.paymentCd"
              :placeholder="$t('addFeeConfig.paymentTypePlaceholder')"
              style="width: 100%"
              clearable
            >
              <el-option
                v-for="(item, index) in addFeeConfigInfo.paymentCds"
                :key="index"
                :label="item.name"
                :value="item.statusCd"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="addFeeConfigInfo.feeFlag !== '2006012'">
        <el-col :span="12">
          <el-form-item 
            :label="$t('addFeeConfig.paymentCycle')" 
            prop="paymentCycle"
          >
            <el-input
              v-model="addFeeConfigInfo.paymentCycle"
              :placeholder="$t('addFeeConfig.paymentCyclePlaceholder')"
              clearable
            >
              <template slot="append">月</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="addFeeConfigInfo.feeFlag === '1003006'">
          <el-form-item 
            :label="$t('addFeeConfig.prepaymentPeriod')" 
            prop="prepaymentPeriod"
          >
            <el-select
              v-model="addFeeConfigInfo.prepaymentPeriod"
              :placeholder="$t('addFeeConfig.prepaymentPeriodPlaceholder')"
              style="width: 100%"
              clearable
            >
              <el-option
                v-for="n in 12"
                :key="n"
                :label="String(n)"
                :value="String(n)"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item 
            :label="$t('addFeeConfig.unit')" 
            prop="units"
          >
            <el-input
              v-model="addFeeConfigInfo.units"
              :placeholder="$t('addFeeConfig.unitPlaceholder')"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item 
            :label="$t('addFeeConfig.billing')" 
            prop="deductFrom"
          >
            <el-select
              v-model="addFeeConfigInfo.deductFrom"
              style="width: 100%"
              clearable
            >
              <el-option :label="$t('addFeeConfig.currentMonth')" value="C" />
              <el-option :label="$t('addFeeConfig.lastMonth')" value="H" />
              <el-option :label="$t('addFeeConfig.nextMonth')" value="N" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item 
            :label="$t('addFeeConfig.mobilePayment')" 
            prop="payOnline"
          >
            <el-select
              v-model="addFeeConfigInfo.payOnline"
              style="width: 100%"
              clearable
            >
              <el-option :label="$t('common.yes')" value="Y" />
              <el-option :label="$t('common.no')" value="N" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item 
            :label="$t('addFeeConfig.roundingMethod')" 
            prop="scale"
          >
            <el-select
              v-model="addFeeConfigInfo.scale"
              style="width: 100%"
              clearable
            >
              <el-option :label="$t('addFeeConfig.roundHalfUp')" value="1" />
              <el-option :label="$t('addFeeConfig.roundUp')" value="3" />
              <el-option :label="$t('addFeeConfig.roundDown')" value="4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item 
            :label="$t('addFeeConfig.decimalPlaces')" 
            prop="decimalPlace"
          >
            <el-select
              v-model="addFeeConfigInfo.decimalPlace"
              style="width: 100%"
              clearable
            >
              <el-option :label="$t('addFeeConfig.integer')" value="0" />
              <el-option :label="$t('addFeeConfig.oneDecimal')" value="1" />
              <el-option :label="$t('addFeeConfig.twoDecimals')" value="2" />
              <el-option :label="$t('addFeeConfig.threeDecimals')" value="3" />
              <el-option :label="$t('addFeeConfig.fourDecimals')" value="4" />
              <el-option :label="$t('addFeeConfig.fiveDecimals')" value="5" />
              <el-option :label="$t('addFeeConfig.sixDecimals')" value="6" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item 
            :label="$t('addFeeConfig.status')" 
            prop="state"
          >
            <el-select
              v-model="addFeeConfigInfo.state"
              style="width: 100%"
              clearable
            >
              <el-option :label="$t('addFeeConfig.enable')" value="Y" />
              <el-option :label="$t('addFeeConfig.disable')" value="N" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item 
            :label="$t('addFeeConfig.computingFormula')" 
            prop="computingFormula"
          >
            <el-select
              v-model="addFeeConfigInfo.computingFormula"
              :placeholder="$t('addFeeConfig.computingFormulaPlaceholder')"
              style="width: 100%"
              clearable
            >
              <el-option
                v-for="(item, index) in addFeeConfigInfo.computingFormulas"
                :key="index"
                :label="item.name"
                :value="item.statusCd"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-show="showUnitPriceField">
        <el-col :span="24">
          <el-form-item 
            :label="$t('addFeeConfig.unitPrice')" 
            prop="squarePrice"
          >
            <el-input
              v-model="addFeeConfigInfo.squarePrice"
              :placeholder="$t('addFeeConfig.unitPricePlaceholder')"
              clearable
            >
              <template slot="append">元</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-show="showAdditionalAmountField">
        <el-col :span="24">
          <el-form-item 
            :label="additionalAmountLabel" 
            prop="additionalAmount"
          >
            <el-input
              v-model="addFeeConfigInfo.additionalAmount"
              :placeholder="$t('addFeeConfig.additionalAmountPlaceholder')"
              clearable
            >
              <template slot="append">元</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-show="addFeeConfigInfo.computingFormula === '7007'">
        <el-col :span="12">
          <el-form-item 
            :label="$t('addFeeConfig.formula')" 
            prop="computingFormulaText"
          >
            <el-input
              type="textarea"
              v-model="addFeeConfigInfo.computingFormulaText"
              :placeholder="$t('addFeeConfig.formulaPlaceholder')"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <div class="formula-explanation">
            <div><strong>{{ $t('addFeeConfig.explanation') }}</strong></div>
            <div>C - {{ $t('addFeeConfig.explanationC') }}</div>
            <div>F - {{ $t('addFeeConfig.explanationF') }}</div>
            <div>U - {{ $t('addFeeConfig.explanationU') }}</div>
            <div>R - {{ $t('addFeeConfig.explanationR') }}</div>
            <div>X - {{ $t('addFeeConfig.explanationX') }}</div>
            <div>L - {{ $t('addFeeConfig.explanationL') }}</div>
            <div>RLPC - {{ $t('addFeeConfig.explanationRLPC') }}</div>
            <div class="example">{{ $t('addFeeConfig.example') }}</div>
            <div class="example">(L-5)*5 + 10</div>
          </div>
        </el-col>
      </el-row>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('addFeeConfig.cancel') }}
      </el-button>
      <el-button type="primary" @click="saveFeeConfigInfo" :loading="loading">
        {{ $t('addFeeConfig.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'
import { saveFeeConfig, listFeeType } from '@/api/fee/feeConfigManageApi'

export default {
  name: 'AddFeeConfig',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      addFeeConfigInfo: {
        configId: '',
        feeTypeCd: '',
        feeName: '',
        feeFlag: '',
        startTime: this.formatDate(new Date()),
        endTime: '2050-01-01',
        computingFormula: '',
        squarePrice: '',
        additionalAmount: '',
        feeTypeCds: [],
        ftIds: [],
        computingFormulas: [],
        feeFlags: [],
        paymentCds: [],
        billTypes: [],
        billType: '002',
        paymentCycle: '',
        paymentCd: '1200',
        computingFormulaText: '',
        deductFrom: 'C',
        payOnline: 'Y',
        scale: '1',
        decimalPlace: '2',
        units: '元',
        prepaymentPeriod: '1',
        state: 'Y',
        ftId: ''
      },
      rules: {
        ftId: [
          { required: true, message: this.$t('addFeeConfig.validate.feeTypeRequired'), trigger: 'change' }
        ],
        feeName: [
          { required: true, message: this.$t('addFeeConfig.validate.feeNameRequired'), trigger: 'blur' },
          { min: 1, max: 100, message: this.$t('addFeeConfig.validate.feeNameLength'), trigger: 'blur' }
        ],
        feeFlag: [
          { required: true, message: this.$t('addFeeConfig.validate.feeFlagRequired'), trigger: 'change' }
        ],
        paymentCd: [
          { required: true, message: this.$t('addFeeConfig.validate.paymentTypeRequired'), trigger: 'change' }
        ],
        paymentCycle: [
          { required: true, message: this.$t('addFeeConfig.validate.paymentCycleRequired'), trigger: 'blur' },
          { pattern: /^\d+$/, message: this.$t('addFeeConfig.validate.paymentCycleNumber'), trigger: 'blur' }
        ],
        prepaymentPeriod: [
          { pattern: /^\d+$/, message: this.$t('addFeeConfig.validate.prepaymentPeriodNumber'), trigger: 'blur' }
        ],
        computingFormula: [
          { required: true, message: this.$t('addFeeConfig.validate.computingFormulaRequired'), trigger: 'change' }
        ],
        squarePrice: [
          { pattern: /^\d+(\.\d{1,6})?$/, message: this.$t('addFeeConfig.validate.unitPriceFormat'), trigger: 'blur' }
        ],
        additionalAmount: [
          { pattern: /^\d+(\.\d{1,6})?$/, message: this.$t('addFeeConfig.validate.additionalAmountFormat'), trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    showUnitPriceField() {
      const excludedFormulas = ['2002', '7007', '8008', '1101', '1102', '4004', '9009']
      return !excludedFormulas.includes(this.addFeeConfigInfo.computingFormula)
    },
    showAdditionalAmountField() {
      const excludedFormulas = ['7007', '8008', '1101', '1102', '4004', '9009']
      return !excludedFormulas.includes(this.addFeeConfigInfo.computingFormula)
    },
    additionalAmountLabel() {
      if (this.addFeeConfigInfo.computingFormula === '1001') {
        return this.$t('addFeeConfig.additionalFee')
      } else if (['1002', '1003', '1004'].includes(this.addFeeConfigInfo.computingFormula)) {
        return this.$t('addFeeConfig.scaleFactor')
      } else {
        return this.$t('addFeeConfig.fixedFee')
      }
    }
  },
  methods: {
    open(params) {
      this.dialogVisible = true
      this.$nextTick(async () => {
        this.resetForm()
        if (params) {
          Object.assign(this.addFeeConfigInfo, params)
        }
        await this.loadDictData()
        await this._loadAddFeeType()
      })
    },
    
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    
    resetForm() {
      this.addFeeConfigInfo = {
        configId: '',
        feeTypeCd: '',
        feeName: '',
        feeFlag: '',
        startTime: this.formatDate(new Date()),
        endTime: '2050-01-01',
        computingFormula: '',
        squarePrice: '',
        additionalAmount: '',
        ftIds: [],
        computingFormulas: [],
        feeFlags: [],
        paymentCds: [],
        billType: '002',
        paymentCycle: '',
        paymentCd: '1200',
        computingFormulaText: '',
        deductFrom: 'C',
        payOnline: 'Y',
        scale: '1',
        decimalPlace: '2',
        units: '元',
        prepaymentPeriod: '1',
        state: 'Y',
        ftId: ''
      }
      if (this.$refs.addFeeConfigForm) {
        this.$refs.addFeeConfigForm.clearValidate()
      }
    },
    
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    
    async loadDictData() {
      try {
        // 计算公式字典
        this.addFeeConfigInfo.computingFormulas = await getDict('pay_fee_config', 'computing_formula')
        
        // 费用标识字典
        this.addFeeConfigInfo.feeFlags = await getDict('pay_fee_config', 'fee_flag')
        
        // 付费类型字典
        this.addFeeConfigInfo.paymentCds = await getDict('pay_fee_config', 'payment_cd')
        
      } catch (error) {
        console.error('加载字典数据失败:', error)
        this.$message.error(this.$t('addFeeConfig.loadDictError'))
      }
    },
    
    async _loadAddFeeType() {
      try {
        const params = {
          communityId: getCommunityId(),
          ftId: '',
          page: 1,
          row: 100
        }
        
        const { data } = await listFeeType(params)
        this.addFeeConfigInfo.ftIds = data
        this._changeAddFeeTypeCd()
        
      } catch (error) {
        console.error('加载费用类型失败:', error)
      }
    },
    
    _changeAddFeeTypeCd() {
      const ftIds = this.addFeeConfigInfo.ftIds
      ftIds.forEach(ft => {
        if (ft.ftId === this.addFeeConfigInfo.ftId) {
          this.addFeeConfigInfo.feeTypeCd = ft.feeTypeCd
        }
      })
      
      const feeTypeCd = this.addFeeConfigInfo.feeTypeCd
      this.addFeeConfigInfo.feeFlag = ''
      this.addFeeConfigInfo.computingFormula = ''
      this.addFeeConfigInfo.paymentCd = '1200'

      // 根据费用类型设置默认值
      if (['888800010015', '888800010016', '888800010009'].includes(feeTypeCd)) {
        this.addFeeConfigInfo.feeFlag = '2006012'
        this.addFeeConfigInfo.computingFormula = '5005'
        this.addFeeConfigInfo.paymentCd = '2100'
        this.addFeeConfigInfo.deductFrom = 'N'
      } else if (feeTypeCd === '888800010006') {
        this.addFeeConfigInfo.feeFlag = '2006012'
      } else if (feeTypeCd === '888800010011') {
        this.addFeeConfigInfo.feeFlag = '4012024'
        this.addFeeConfigInfo.computingFormula = '3003'
      } else if (feeTypeCd === '888800010001') {
        this.addFeeConfigInfo.feeFlag = '1003006'
        this.addFeeConfigInfo.computingFormula = '1001'
      } else if (feeTypeCd === '888800010018') {
        this.addFeeConfigInfo.feeFlag = '1003006'
        this.addFeeConfigInfo.computingFormula = '1101'
      } else if (feeTypeCd === '888800010008') {
        this.addFeeConfigInfo.feeFlag = '1003006'
        this.addFeeConfigInfo.computingFormula = '2002'
        this.addFeeConfigInfo.paymentCycle = '1'
      } else if (feeTypeCd === '888800010017') {
        this.addFeeConfigInfo.feeFlag = '2006012'
        this.addFeeConfigInfo.computingFormula = '4004'
        this.addFeeConfigInfo.paymentCd = '2100'
      }
    },
    
    async saveFeeConfigInfo() {
      try {
        const valid = await this.$refs.addFeeConfigForm.validate()
        if (!valid) return
        
        this.loading = true
        
        // 处理特殊公式的数据
        if (this.addFeeConfigInfo.computingFormula === '2002') {
          this.addFeeConfigInfo.squarePrice = "0.00"
        }
        if (['7007', '4004', '1101', '1102', '9009'].includes(this.addFeeConfigInfo.computingFormula)) {
          this.addFeeConfigInfo.squarePrice = "0.00"
          this.addFeeConfigInfo.additionalAmount = "0.00"
        }
        if (this.addFeeConfigInfo.feeFlag === '2006012') {
          this.addFeeConfigInfo.paymentCycle = '1'
        }
        if (this.addFeeConfigInfo.feeFlag !== '1003006') {
          this.addFeeConfigInfo.prepaymentPeriod = '1'
        }
        
        const params = {
          ...this.addFeeConfigInfo,
          communityId: getCommunityId(),
          feeName: this.addFeeConfigInfo.feeName.trim(),
          paymentCycle: this.addFeeConfigInfo.paymentCycle.trim(),
          squarePrice: this.addFeeConfigInfo.squarePrice.trim(),
          additionalAmount: this.addFeeConfigInfo.additionalAmount.trim()
        }
        
        await saveFeeConfig(params)
        
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        
        // 通知父组件刷新列表
        this.$parent.$emit('listFeeConfig')
        
      } catch (error) {
        console.error('保存费用配置失败:', error)
        this.$message.error(error.message || this.$t('addFeeConfig.saveError'))
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