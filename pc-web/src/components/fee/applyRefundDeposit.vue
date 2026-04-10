<template>
    <el-dialog :title="$t('applyRefundDeposit.refundDeposit')" :visible.sync="visible" width="80%" @close="handleClose">
      <el-form :model="applyRefundDepositInfo" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('applyRefundDeposit.feeItem')">
              {{ applyRefundDepositInfo.feeName }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('applyRefundDeposit.amount')">
              {{ applyRefundDepositInfo.receivedAmount }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('applyRefundDeposit.chargingObject')">
              {{ applyRefundDepositInfo.payerObjName }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('applyRefundDeposit.way')">
              <el-select v-model="applyRefundDepositInfo.way" style="width:100%">
                <el-option :value="'C'" :label="$t('applyRefundDeposit.cash')"></el-option>
                <el-option :value="'P'" :label="$t('applyRefundDeposit.toPrePay')"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="applyRefundDepositInfo.way == 'P'">
          <el-col :span="24">
            <el-table :data="applyRefundDepositInfo.rules" border style="width:100%">
              <el-table-column :label="$t('applyRefundDeposit.select')" width="50">
                <template slot-scope="scope">
                  <el-checkbox 
                    :value="scope.row.ruleId" 
                    v-model="scope.row.selected" 
                    true-label="Y" 
                    false-label="N"
                    @change="selectOneRule(scope.row)">
                  </el-checkbox>
                </template>
              </el-table-column>
              <el-table-column :label="$t('applyRefundDeposit.feeItem')" prop="feeName"></el-table-column>
              <el-table-column :label="$t('applyRefundDeposit.amount')" width="250">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.amount" :disabled="scope.row.selected !== 'Y'" type="number"></el-input>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('applyRefundDeposit.refundReason')" :rules="[{ required: true, message: $t('applyRefundDeposit.refundReasonRequired') }]">
              <el-input type="textarea" v-model="applyRefundDepositInfo.reason"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">{{ $t('applyRefundDeposit.cancel') }}</el-button>
        <el-button type="primary" @click="submitApplyRefundDeposit">{{ $t('applyRefundDeposit.submit') }}</el-button>
      </div>
    </el-dialog>
  </template>
  
  <script>
  import { getCommunityId } from '@/api/community/communityApi'
  import { listPrePayFeeRule, applyRefundDeposit } from '@/api/fee/payFeeOrderApi'
  
  export default {
    name: 'ApplyRefundDeposit',
    data() {
      return {
        visible: false,
        communityId: '',
        applyRefundDepositInfo: {
          feeId: '',
          detailId: '',
          feeName: '',
          way: 'P',
          payerObjName: '',
          payerObjId: '',
          ownerName: '',
          receivedAmount: '',
          communityId: '',
          selectRuleIds: [],
          rules: [],
          reason: ''
        }
      }
    },
    created() {
      this.communityId = getCommunityId()
    },
    methods: {
      open(params) {
        this.refreshFeeDetailInfo()
        Object.assign(this.applyRefundDepositInfo, params)
        this.visible = true
        this._loadFeeRuleInfo()
      },
      handleClose() {
        this.visible = false
      },
      refreshFeeDetailInfo() {
        this.applyRefundDepositInfo = {
          feeId: '',
          detailId: '',
          feeName: '',
          way: 'P',
          payerObjName: '',
          payerObjId: '',
          ownerName: '',
          receivedAmount: '',
          communityId: '',
          selectRuleIds: [],
          rules: [],
          reason: ''
        }
      },
      async _loadFeeRuleInfo() {
        try {
          const params = {
            communityId: this.communityId,
            payerObjId: this.applyRefundDepositInfo.payerObjId,
            payerObjType: '3333'
          }
          const { data } = await listPrePayFeeRule(params)
          data.rules.forEach(item => {
            item.amount = '0'
            item.selected = 'N'
          })
          this.applyRefundDepositInfo.rules = data.rules
        } catch (error) {
          console.error('获取费用规则失败:', error)
          this.$message.error(this.$t('applyRefundDeposit.loadFeeRuleFailed'))
        }
      },
      selectOneRule(rule) {
        // 更新选中规则ID数组
        if (rule.selected === 'Y') {
          if (!this.applyRefundDepositInfo.selectRuleIds.includes(rule.ruleId)) {
            this.applyRefundDepositInfo.selectRuleIds.push(rule.ruleId)
          }
        } else {
          // 取消选中时，从数组中移除并重置金额
          const index = this.applyRefundDepositInfo.selectRuleIds.indexOf(rule.ruleId)
          if (index > -1) {
            this.applyRefundDepositInfo.selectRuleIds.splice(index, 1)
          }
          rule.amount = '0'
        }
      },
      selectAllRule(event) {
        if (event.target.checked) {
          this.applyRefundDepositInfo.selectRuleIds = this.applyRefundDepositInfo.rules.map(item => item.ruleId)
          this.applyRefundDepositInfo.rules.forEach(item => {
            item.selected = 'Y'
          })
        } else {
          this.applyRefundDepositInfo.selectRuleIds = []
          this.applyRefundDepositInfo.rules.forEach(item => {
            item.selected = 'N'
            item.amount = '0'
          })
        }
      },
      async submitApplyRefundDeposit() {
        // 验证
        if (!this.applyRefundDepositInfo.reason) {
          this.$message.error(this.$t('applyRefundDeposit.refundReasonRequired'))
          return
        }
        let rules = []
        if (this.applyRefundDepositInfo.way == 'P') {
          rules = this.applyRefundDepositInfo.rules.filter(item => this.applyRefundDepositInfo.selectRuleIds.includes(item.ruleId))
          if (rules.length < 1) {
            this.$message.error(this.$t('applyRefundDeposit.selectPrePayFee'))
            return
          }
        }
        const data = {
          feeId: this.applyRefundDepositInfo.feeId,
          detailId: this.applyRefundDepositInfo.detailId,
          feeName: this.applyRefundDepositInfo.feeName,
          way: this.applyRefundDepositInfo.way,
          communityId: this.communityId,
          rules,
          reason: this.applyRefundDepositInfo.reason
        }
        try {
          await applyRefundDeposit(data)
          this.handleClose()
          this.refreshFeeDetailInfo()
          this.$message.success(this.$t('common.operationSuccess'))
        } catch (error) {
          this.$message.error(error.message || this.$t('applyRefundDeposit.operationFailed'))
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