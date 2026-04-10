<template>
    <el-dialog
      :title="$t('batchPrePayFeeDialog.title')"
      :visible.sync="visible"
      width="60%"
      :before-close="handleClose"
    >
      <el-row>
        <el-col :span="8">
          <el-select
            v-model="batchPrePayFeeDialogInfo.payerObjId"
            @change="_selectBatchPrePayObj"
            style="width: 100%"
          >
            <el-option disabled value="">{{$t('batchPrePayFeeDialog.selectTransferFee')}}</el-option>
            <el-option
              v-for="(item,index) in batchPrePayFeeDialogInfo.payerObjs"
              :key="index"
              :value="item.payerObjId"
              :label="item.payerObjName"
            />
          </el-select>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <table class="native-table" style="width: 100%; margin-top: 20px;" border="1" cellspacing="0" cellpadding="0">
            <thead>
              <tr>
                <th style="width:60px; text-align:center;">
                  <input
                    type="checkbox"
                    class="i-checks checkItem"
                    :checked="batchPrePayFeeDialogInfo.selectRuleIds.length >= batchPrePayFeeDialogInfo.rules.length && batchPrePayFeeDialogInfo.rules.length > 0"
                    @change="selectAllRule($event)"
                  />
                </th>
                <th style="text-align:center;">{{$t('batchPrePayFeeDialog.feeName')}}</th>
                <th style="text-align:center;">{{$t('batchPrePayFeeDialog.monthPrice')}}</th>
                <th style="text-align:center;">{{$t('batchPrePayFeeDialog.description')}}</th>
                <th style="text-align:center; width:150px;">{{$t('batchPrePayFeeDialog.receivableAmount')}}</th>
                <th style="text-align:center; width:120px;">{{$t('batchPrePayFeeDialog.months')}}</th>
                <th style="text-align:center; width:120px;">{{$t('batchPrePayFeeDialog.startMonth')}}</th>
              </tr>
            </thead>
            <tbody>
              <template v-for="(rule, rIndex) in batchPrePayFeeDialogInfo.rules">
                <tr :key="`r-${rIndex}`">
                  <td style="text-align:center;">
                    <input
                      type="checkbox"
                      class="i-checks checkItem"
                      :value="rule.ruleId"
                      v-model="batchPrePayFeeDialogInfo.selectRuleIds"
                      @change="selectOneRule"
                    />
                  </td>
                  <td style="text-align:center;">{{rule.feeName}}</td>
                  <td style="text-align:center;">{{rule.monthAmount}}</td>
                  <td style="text-align:center;" class="multiline-text">{{rule.remark}}</td>
                  <td style="text-align:center; width:150px;">
                    <el-input
                      type="number"
                      v-model="rule.receivedAmount"
                      :disabled="!batchPrePayFeeDialogInfo.selectRuleIds.includes(rule.ruleId)"
                      @change="_computeReceivedAmount"
                    />
                  </td>
                  <td style="text-align:center; width:120px;">
                    <el-select
                      v-model="rule.cycles"
                      :disabled="!batchPrePayFeeDialogInfo.selectRuleIds.includes(rule.ruleId)"
                      @change="_changeMonth(rule.cycles, rule)"
                      style="width: 100%"
                    >
                      <el-option disabled value="">{{$t('batchPrePayFeeDialog.selectMonths')}}</el-option>
                      <el-option
                        v-for="item in 24"
                        :key="item"
                        :value="item"
                        :label="`${item}${$t('batchPrePayFeeDialog.months')}`"
                      />
                    </el-select>
                  </td>
                  <td style="text-align:center; width:120px;">
                    <el-date-picker
                      v-model="rule.startMonth"
                      type="month"
                      format="yyyy-MM"
                      value-format="yyyy-MM"
                      :readonly="true"
                      :disabled="!batchPrePayFeeDialogInfo.selectRuleIds.includes(rule.ruleId)"
                      style="width: 100%"
                    />
                  </td>
                </tr>
                <template v-if="rule.showFlag == 'Y'">
                  <tr v-for="(discount, dIndex) in rule.discounts" :key="`d-${rIndex}-${dIndex}`" style="background-color: #f9f9f9;">
                    <td style="text-align:center;">
                      <input
                        type="checkbox"
                        class="i-checks checkItem"
                        :value="discount.discountId"
                        v-model="rule.selectedDiscountIds"
                        @change="selectOneDiscount(discount)"
                      />
                    </td>
                    <td style="text-align:center;">{{discount.discountName}}</td>
                    <td style="text-align:center;">-</td>
                    <td style="text-align:center;">{{discount.ruleName}}</td>
                    <td style="text-align:center;">{{discount.discountPrice}}</td>
                    <td style="text-align:center;" colspan="2"></td>
                  </tr>
                </template>
              </template>
            </tbody>
          </table>
        </el-col>
      </el-row>
  
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">{{$t('batchPrePayFeeDialog.cancel')}}</el-button>
        <el-button type="primary" @click="_doBatchPrePayFeeDialog">
          {{$t('batchPrePayFeeDialog.confirm')}}
        </el-button>
      </div>
    </el-dialog>
  </template>
  
  <script>
  import { getCommunityId } from '@/api/community/communityApi'
  import { listPrePayFeeRule, computePreFeeDiscount } from '@/api/fee/batchPayFeeOrderApi'
  
  export default {
    name: 'BatchPrePayFeeDialog',
    data() {
      return {
        visible: false,
        batchPrePayFeeDialogInfo: {
          payerObjs: [],
          payerObjId: '',
          payerObjType: '',
          rules: [],
          selectRuleIds: [],
          receivedAmount: '0.00'
        },
        communityId: ''
      }
    },
    created() {
      this.communityId = getCommunityId()
    },
    methods: {
      open(params) {
        this.visible = true
        Object.assign(this.batchPrePayFeeDialogInfo, params)
      },
      
      handleClose() {
        this.visible = false
        this.clearBatchPrePayFeeDialogInfo()
      },
      
      async _selectBatchPrePayObj() {
        const payerObjId = this.batchPrePayFeeDialogInfo.payerObjId
        if (!payerObjId) return
        
        const payerObj = this.batchPrePayFeeDialogInfo.payerObjs.find(obj => obj.payerObjId === payerObjId)
        if (payerObj) {
          this.batchPrePayFeeDialogInfo.payerObjType = payerObj.payerObjType
        }
        
        await this._loadFeeRuleInfo()
      },
      
      async _loadFeeRuleInfo() {
        try {
          const params = {
            communityId: this.communityId,
            payerObjId: this.batchPrePayFeeDialogInfo.payerObjId,
            payerObjType: this.batchPrePayFeeDialogInfo.payerObjType
          }
          
          const response = await listPrePayFeeRule(params)
          
          const rules = response.data.rules.map(item => {
            return {
              ...item,
              receivedAmount: 0.00,
              cycles: '',
              startMonth: this._getDefaultStartMonth(item.curYearMonth),
              selected: 'N',
              showFlag: 'N',
              selectedDiscountIds: []
            }
          })
          
          this.batchPrePayFeeDialogInfo.rules = rules
          this._computeReceivedAmount()
        } catch (error) {
          console.error('加载费用规则失败:', error)
          this.$message.error('加载费用规则失败')
        }
      },
      
      _getDefaultStartMonth(curYearMonth) {
        if (!curYearMonth) {
          const date = new Date()
          return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}`
        }
        
        try {
          const date = new Date(curYearMonth)
          return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}`
        } catch (error) {
          const date = new Date()
          return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}`
        }
      },
      
      selectAllRule(event) {
        const isChecked = event && event.target ? event.target.checked : true
        if (isChecked) {
          this.batchPrePayFeeDialogInfo.selectRuleIds = this.batchPrePayFeeDialogInfo.rules.map(item => item.ruleId)
        } else {
          this.batchPrePayFeeDialogInfo.selectRuleIds = []
        }
        this._computeReceivedAmount()
      },
      
      selectOneRule() {
        this._computeReceivedAmount()
      },
      
      _changeMonth(cycles, rule) {
        this.batchPrePayFeeDialogInfo.rules.forEach(item => {
          if (item.cycles !== '') {
            item.receivedAmount = (parseFloat(item.cycles) * parseFloat(item.monthAmount)).toFixed(2)
          }
        })
        
        this._computeReceivedAmount()
        this.computePreFeeDiscount(cycles, rule)
      },
      
      _computeReceivedAmount() {
        const rules = this.batchPrePayFeeDialogInfo.rules
        
        if (rules.length < 1) {
          this.batchPrePayFeeDialogInfo.receivedAmount = '0.00'
          return
        }
        
        let receivedAmount = 0.00
        const selectedRuleIds = this.batchPrePayFeeDialogInfo.selectRuleIds
        
        if (selectedRuleIds.length < 1) {
          this.batchPrePayFeeDialogInfo.receivedAmount = '0.00'
          return
        }
        
        rules.forEach(item => {
          if (selectedRuleIds.includes(item.ruleId)) {
            receivedAmount += parseFloat(item.receivedAmount)
            
            if (item.selectedDiscountIds && item.selectedDiscountIds.length > 0) {
              item.discounts.forEach(discount => {
                if (item.selectedDiscountIds.includes(discount.discountId)) {
                  receivedAmount += parseFloat(discount.discountPrice)
                }
              })
            }
          }
        })
        
        this.batchPrePayFeeDialogInfo.receivedAmount = receivedAmount.toFixed(2)
      },
      
      async computePreFeeDiscount(cycles, rule) {
        rule.showFlag = 'Y'
        
        try {
          const params = {
            communityId: this.communityId,
            ruleId: rule.ruleId,
            cycles: cycles
          }
          
          const response = await computePreFeeDiscount(params)
          
          if (response.data && response.data.length > 0) {
            rule.discounts = response.data
            rule.selectedDiscountIds = rule.discounts.map(discount => discount.discountId)
            this._computeReceivedAmount()
          }
        } catch (error) {
          console.error('计算预付款折扣失败:', error)
        }
      },
      
      selectOneDiscount(discount) {
        console.log(discount)
        this._computeReceivedAmount()
      },
      
      getDiscounts(row) {
        return row.discounts || []
      },
      
      _doBatchPrePayFeeDialog() {
        const rules = this.batchPrePayFeeDialogInfo.rules.filter(item =>
          this.batchPrePayFeeDialogInfo.selectRuleIds.includes(item.ruleId)
        )
        
        this.$emit('notifyPreFeeOrder', {
          rules: rules,
          ruleAmount: this.batchPrePayFeeDialogInfo.receivedAmount
        })
        
        this.handleClose()
      },
      
      clearBatchPrePayFeeDialogInfo() {
        this.batchPrePayFeeDialogInfo = {
          payerObjs: [],
          payerObjId: '',
          payerObjType: '',
          rules: [],
          selectRuleIds: [],
          receivedAmount: '0.00'
        }
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .dialog-footer {
    text-align: right;
  }
  .native-table {
    width: 100%;
    border-collapse: collapse;
    background: #fff;
  }
  .native-table thead tr {
    background: #f5f7fa;
    color: #909399;
  }
  .native-table th,
  .native-table td {
    padding: 12px 10px;
    border: 1px solid #ebeef5;
    font-size: 14px;
  }
  .native-table tbody tr:hover {
    background: #f5f7fa;
  }
  .checkItem {
    width: 18px;
    height: 18px;
    margin: 0 6px 0 0;
    vertical-align: middle;
    cursor: pointer;
    appearance: none;
    -webkit-appearance: none;
    border: 1px solid #dcdfe6;
    border-radius: 2px;
    background-color: #fff;
    position: relative;
    outline: none;
  }
  .checkItem:checked {
    background-color: #409eff;
    border-color: #409eff;
  }
  .checkItem:checked::after {
    content: '';
    position: absolute;
    left: 4px;
    top: 0px;
    width: 5px;
    height: 10px;
    border: solid #fff;
    border-width: 0 2px 2px 0;
    transform: rotate(45deg);
  }
  </style>