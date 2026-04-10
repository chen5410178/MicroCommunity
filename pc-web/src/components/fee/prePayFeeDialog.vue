<template>
    <el-dialog :title="$t('prePayFeeDialog.prePayment')" :visible.sync="visible" width="80%" @close="handleClose">
      <el-table :data="prePayFeeDialogInfo.rules" border style="width:100%">
        <el-table-column :label="$t('prePayFeeDialog.select')" width="60" align="center">
          <template slot-scope="scope">
            <el-checkbox-group v-model="prePayFeeDialogInfo.selectRuleIds" @change="selectOneRule">
              <el-checkbox :label="scope.row.ruleId" />
            </el-checkbox-group>
          </template>
        </el-table-column>
        <el-table-column :label="$t('prePayFeeDialog.feeName')" prop="feeName">
          <template slot-scope="scope">
            <span @click="switchRuleFlag(scope.row)" style="cursor: pointer;">{{ scope.row.feeName }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('prePayFeeDialog.monthlyUnitPrice')" prop="monthAmount">
          <template slot-scope="scope">
            <span @click="switchRuleFlag(scope.row)">{{ scope.row.monthAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('prePayFeeDialog.explanation')" prop="remark">
          <template slot-scope="scope">
            <span @click="switchRuleFlag(scope.row)">{{ scope.row.remark }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('prePayFeeDialog.receivableAmount')" width="200" align="center">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.receivedAmount" :disabled="scope.row.selected == 'N'" @change="_computeReceivedAmount" :precision="2" :controls="false" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column :label="$t('prePayFeeDialog.months')" width="200" align="center">
          <template slot-scope="scope">
            <el-select v-model="scope.row.cycles" :disabled="scope.row.selected == 'N'" @change="_changeMonth(scope.row.cycles, scope.row)" style="width:100%" :placeholder="$t('prePayFeeDialog.selectMonths')">
              <el-option v-for="item in 24" :key="item" :value="item" :label="item + $t('prePayFeeDialog.months')" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column :label="$t('prePayFeeDialog.startingMonth')" width="200" align="center">
          <template slot-scope="scope">
            <el-date-picker v-model="scope.row.startMonth" type="month" format="yyyy-MM" value-format="yyyy-MM" style="width: 100%" />
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">{{ $t('prePayFeeDialog.cancel') }}</el-button>
        <el-button type="primary" @click="_doPrePayFeeDialog">{{ $t('prePayFeeDialog.confirm') }}</el-button>
      </div>
    </el-dialog>
  </template>
  
  <script>
  import { getCommunityId } from '@/api/community/communityApi'
  import { listPrePayFeeRule, computePreFeeDiscount } from '@/api/fee/payFeeOrderApi'
  
  export default {
    name: 'PrePayFeeDialog',
    data() {
      return {
        visible: false,
        communityId: '',
        prePayFeeDialogInfo: {
          payerObjId: '',
          payerObjType: '',
          rules: [],
          selectRuleIds: []
        }
      }
    },
    created() {
      this.communityId = getCommunityId()
    },
    methods: {
      open(params) {
        this.clearPrePayFeeDialogInfo()
        Object.assign(this.prePayFeeDialogInfo, params)
        this.visible = true
        this._loadFeeRuleInfo()
      },
      handleClose() {
        this.visible = false
      },
      async _loadFeeRuleInfo() {
        try {
          const params = {
            communityId: this.communityId,
            payerObjId: this.prePayFeeDialogInfo.payerObjId,
            payerObjType: this.prePayFeeDialogInfo.payerObjType
          }
          const { data } = await listPrePayFeeRule(params)
          data.rules = data.rules.map(item => {
            const date = new Date(item.curYearMonth)
            const month = (date.getMonth() + 1).toString().padStart(2, '0')
            return {
              ...item,
              receivedAmount: 0.00,
              cycles: '',
              startMonth: `${date.getFullYear()}-${month}`,
              selected: 'N',
              selectedDiscountIds: [],
              discounts: item.discounts || []
            }
          })
          this.prePayFeeDialogInfo.rules = data.rules
          this._computeReceivedAmount()
          this._initPageData()
        } catch (error) {
          console.error('请求失败处理')
        }
      },
      _initPageData() {
        setTimeout(() => {
          this.prePayFeeDialogInfo.rules.forEach(item => {
            // 初始化日期选择器，如果需要
            console.log(item)
          })
        }, 1000)
      },
      selectAllRule(event) {
        if (event.target.checked) {
          this.prePayFeeDialogInfo.selectRuleIds = this.prePayFeeDialogInfo.rules.map(item => item.ruleId)
        } else {
          this.prePayFeeDialogInfo.selectRuleIds = []
        }
        this.prePayFeeDialogInfo.rules.forEach(item => {
          item.selected = this.prePayFeeDialogInfo.selectRuleIds.includes(item.ruleId) ? 'Y' : 'N'
        })
        this._computeReceivedAmount()
      },
      selectOneRule() {
        const selectedRuleIds = this.prePayFeeDialogInfo.selectRuleIds
        this.prePayFeeDialogInfo.rules.forEach(rule => {
          rule.selected = selectedRuleIds.includes(rule.ruleId) ? 'Y' : 'N'
        })
        this._computeReceivedAmount()
      },
      _changeMonth(cycles, rule) {
        this.prePayFeeDialogInfo.rules.forEach(item => {
          item.receivedAmount = 0.00
          if (item.cycles != '') {
            item.receivedAmount = (parseFloat(item.cycles) * parseFloat(item.monthAmount)).toFixed(2)
          }
        })
        this._computeReceivedAmount()
        this.computePreFeeDiscount(cycles, rule)
      },
      _computeReceivedAmount() {
        const rules = this.prePayFeeDialogInfo.rules
        if (rules.length < 1) {
          this.prePayFeeDialogInfo.receivedAmount = '0.00'
          return
        }
        let receivedAmount = 0.00
        const selectedRuleIds = this.prePayFeeDialogInfo.selectRuleIds
        if (selectedRuleIds.length < 1) {
          this.prePayFeeDialogInfo.receivedAmount = '0.00'
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
        this.prePayFeeDialogInfo.receivedAmount = receivedAmount.toFixed(2)
      },
      async computePreFeeDiscount(cycles, rule) {
        rule.showFlag = 'Y'
        try {
          const params = {
            communityId: this.communityId,
            ruleId: rule.ruleId,
            cycles
          }
          const { data } = await computePreFeeDiscount(params)
          rule.discounts = data
          rule.selectedDiscountIds = rule.discounts.map(discount => discount.discountId)
          this._computeReceivedAmount()
        } catch (error) {
          console.error('请求失败处理')
        }
      },
      _doPrePayFeeDialog() {
        const rules = this.prePayFeeDialogInfo.rules.filter(item => this.prePayFeeDialogInfo.selectRuleIds.includes(item.ruleId))
        this.$emit('notify-pre-fee-order', {
          rules,
          ruleAmount: this.prePayFeeDialogInfo.receivedAmount
        })
        this.handleClose()
      },
      clearPrePayFeeDialogInfo() {
        this.prePayFeeDialogInfo = {
          payerObjId: '',
          payerObjType: '',
          rules: [],
          selectRuleIds: []
        }
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .dialog-footer {
    text-align: right;
  }
  </style>