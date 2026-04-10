<template>
  <div class="pre-pay-fee-order-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('prePayFeeOrder.title') }}</span>
        <div class="card-header-tools">
          <el-button type="primary" size="small" @click="handleGoBack">
            <i class="el-icon-close"></i>
            {{ $t('prePayFeeOrder.back') }}
          </el-button>
        </div>
      </div>

      <div class="card-content">
        <el-row :gutter="20">
          <el-col :span="17" class="left-panel">
            <el-row>
              <el-col :span="24">
                <el-form label-width="120px">
                  <el-row>
                    <el-col :span="12">
                      <el-form-item :label="$t('prePayFeeOrder.payerObject')">
                        <span class="form-label">{{ prePayFeeOrderInfo.payerObjName }}</span>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item :label="$t('prePayFeeOrder.ownerName')">
                        <span class="form-label">{{ prePayFeeOrderInfo.ownerName }}</span>
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-row v-if="prePayFeeOrderInfo.payerObjType == '3333'">
                    <el-col :span="12">
                      <el-form-item :label="$t('prePayFeeOrder.buildUpArea')">
                        <span class="form-label">{{ prePayFeeOrderInfo.buildUpArea }}</span>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item :label="$t('prePayFeeOrder.roomArea')">
                        <span class="form-label">{{ prePayFeeOrderInfo.roomArea }}</span>
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-row v-if="prePayFeeOrderInfo.payerObjType == '6666'">
                    <el-col :span="12">
                      <el-form-item :label="$t('prePayFeeOrder.parkingLot')">
                        <span class="form-label">{{ prePayFeeOrderInfo.areaNum }}</span>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item :label="$t('prePayFeeOrder.parkingSpace')">
                        <span class="form-label">{{ prePayFeeOrderInfo.num }}</span>
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-row>
                    <el-col :span="12">
                      <el-form-item :label="$t('prePayFeeOrder.receivableAmount')">
                        <span class="amount-label">￥{{ prePayFeeOrderInfo.receivedAmount }}</span>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-form>
              </el-col>
            </el-row>
          </el-col>

          <el-col :span="7" class="right-panel">
            <el-form label-width="80px">
              <el-form-item :label="$t('prePayFeeOrder.paymentTime')">
                <el-input v-model="prePayFeeOrderInfo.createTime"
                  :placeholder="$t('prePayFeeOrder.paymentTimePlaceholder')" readonly
                  class="prePayFeeOrderCreateTime" />
              </el-form-item>

              <el-form-item :label="$t('prePayFeeOrder.autoDeduction')">
                <el-select v-model="prePayFeeOrderInfo.way" style="width: 100%">
                  <el-option value="Y" :label="$t('prePayFeeOrder.yes')" />
                  <el-option value="N" :label="$t('prePayFeeOrder.no')" />
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('prePayFeeOrder.paymentMethod')">
                <el-select v-model="prePayFeeOrderInfo.primeRate"
                  :placeholder="$t('prePayFeeOrder.paymentMethodPlaceholder')" style="width: 100%">
                  <el-option v-for="(item, index) in filteredPrimeRates" :key="index" :value="item.statusCd"
                    :label="item.name" />
                </el-select>
              </el-form-item>

              <el-form-item :label="$t('prePayFeeOrder.remark')">
                <el-input type="textarea" :placeholder="$t('prePayFeeOrder.remarkPlaceholder')"
                  v-model="prePayFeeOrderInfo.remark" :rows="3" />
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>

        <div class="table-section">
          <el-table :data="prePayFeeOrderInfo.rules" border style="width: 100%" class="fee-table">
            <el-table-column width="60" align="center">
              <template slot-scope="scope">
                <el-checkbox-group v-model="prePayFeeOrderInfo.selectRuleIds" @change="selectOneRule">
                  <el-checkbox :label="scope.row.ruleId" />
                </el-checkbox-group>
              </template>
            </el-table-column>
            <el-table-column :label="$t('prePayFeeOrder.feeName')" align="center" prop="feeName" />
            <el-table-column :label="$t('prePayFeeOrder.monthlyPrice')" align="center" prop="monthAmount" />
            <el-table-column :label="$t('prePayFeeOrder.description')" align="center" prop="remark" />
            <el-table-column :label="$t('prePayFeeOrder.receivableAmount')" align="center" width="250">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.receivedAmount" :disabled="scope.row.selected == 'N'"
                  @change="computeReceivedAmount" :precision="2" :controls="false" style="width: 100%" />
              </template>
            </el-table-column>
            <el-table-column :label="$t('prePayFeeOrder.months')" align="center" width="200">
              <template slot-scope="scope">
                <el-select v-model="scope.row.cycles" :disabled="scope.row.selected == 'N'"
                  @change="changeMonth(scope.row.cycles, scope.row)" style="width: 100%"
                  :placeholder="$t('prePayFeeOrder.selectMonthsPlaceholder')">
                  <el-option v-for="item in 24" :key="item" :value="item"
                    :label="item + $t('prePayFeeOrder.monthsUnit')" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column :label="$t('prePayFeeOrder.startMonth')" align="center" width="250">
              <template slot-scope="scope">
                <el-date-picker v-model="scope.row.startMonth" type="month" format="yyyy-MM" value-format="yyyy-MM"
                  readonly style="width: 100%" />
              </template>
            </el-table-column>
          </el-table>

          <div v-for="rule in prePayFeeOrderInfo.rules" :key="rule.ruleId">
            <el-table v-if="rule.showFlag == 'Y' && rule.discounts && rule.discounts.length > 0" :data="rule.discounts"
              style="background-color: #f9f9f9;" class="discount-table">
              <el-table-column width="60" align="center">
                <template slot-scope="scope">
                  <el-checkbox v-model="rule.selectedDiscountIds" :value="scope.row.discountId"
                    @change="selectOneDiscount(scope.row)" />
                </template>
              </el-table-column>
              <el-table-column :label="$t('prePayFeeOrder.feeName')" align="center" prop="discountName" />
              <el-table-column :label="$t('prePayFeeOrder.monthlyPrice')" align="center">
                <span>-</span>
              </el-table-column>
              <el-table-column :label="$t('prePayFeeOrder.description')" align="center" prop="ruleName" />
              <el-table-column :label="$t('prePayFeeOrder.receivableAmount')" align="center" prop="discountPrice" />
              <el-table-column align="center" colspan="2" />
            </el-table>
          </div>
        </div>

        <div class="meter-section">
          <div class="meter-header flex justify-start">
            <el-checkbox v-model="prePayFeeOrderInfo.showMeterRule" @change="selectMeterRule">
              {{ $t('prePayFeeOrder.prepaidUtilities') }}
            </el-checkbox>
          </div>

          <div v-if="prePayFeeOrderInfo.showMeterRule == 'Y'">
            <el-table :data="prePayFeeOrderInfo.meterRules" border class="meter-table">
              <el-table-column :label="$t('prePayFeeOrder.feeName')" align="center" prop="ftName" />
              <el-table-column :label="$t('prePayFeeOrder.prepaidAmount')" align="center">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.amount" @change="computeReceivedAmount" :precision="2"
                    :controls="false" style="width: 250px;" />
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </el-card>

    <div class="action-buttons">
      <el-button
        v-if="prePayFeeOrderInfo.primeRate == '3' || prePayFeeOrderInfo.primeRate == '4' || prePayFeeOrderInfo.primeRate == '10' || prePayFeeOrderInfo.primeRate == '12'"
        type="success" size="large" @click="handleQrCodePay">
        {{ $t('prePayFeeOrder.scanPayment') }}
      </el-button>

      <el-button v-if="prePayFeeOrderInfo.offlinePayFeeSwitch != '2'" type="primary" size="large"
        @click="handleCommonPay" style="margin-left: 20px;">
        {{ $t('prePayFeeOrder.submitPayment') }}
      </el-button>
    </div>

    <pre-pay-fee-order-confirm ref="confirmDialog" @success="handlePaymentSuccess" />
  </div>
</template>

<script>
import { getFeeRuleInfo, getMeterFeeRuleInfo } from '@/api/fee/prePayFeeOrderApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import PrePayFeeOrderConfirm from '@/components/fee/prePayFeeOrderConfirm'
import { getDateStr } from '@/utils/dateUtil'

export default {
  name: 'PrePayFeeOrder',
  components: {
    PrePayFeeOrderConfirm
  },
  data() {
    return {
      prePayFeeOrderInfo: {
        payerObjId: '',
        payerObjName: '',
        payerObjType: '',
        ownerName: '',
        ownerId: '',
        buildUpArea: 0.0,
        roomArea: '',
        areaNum: '',
        num: '',
        createTime: getDateStr(),
        communityId: '',
        receivedAmount: '0.00',
        remark: '',
        authCode: '',
        orderId: '',
        primeRate: '',
        way: 'Y',
        primeRates: [],
        rules: [],
        selectRuleIds: [],
        meterRules: [],
        showMeterRule: 'N'
      }
    }
  },
  created() {
    this.initData()
  },
  computed: {
    filteredPrimeRates() {
      return (this.prePayFeeOrderInfo.primeRates || []).filter(item => item.statusCd != '5' && item.statusCd != '6' && item.statusCd != '8')
    }
  },
  methods: {
    async initData() {
      this.prePayFeeOrderInfo.communityId = getCommunityId()
      this.prePayFeeOrderInfo.payerObjId = this.$route.query.payerObjId
      this.prePayFeeOrderInfo.payerObjType = this.$route.query.payerObjType

      await this.loadFeeRuleInfo()
      await this.initPageData()
    },

    async loadFeeRuleInfo() {
      try {
        const params = {
          communityId: this.prePayFeeOrderInfo.communityId,
          payerObjId: this.prePayFeeOrderInfo.payerObjId,
          payerObjType: this.prePayFeeOrderInfo.payerObjType
        }

        const response = await getFeeRuleInfo(params)
        const data = response.data

        data.rules = data.rules.map(item => {
          item.receivedAmount = 0.00
          item.cycles = ''
          item.startDateInputClass = 'startDateInputClass' + item.ruleId
          const date = new Date(item.curYearMonth)
          item.startMonth = date.getFullYear() + '-' + (date.getMonth() + 1)
          item.selected = 'N'
          item.showFlag = 'N'
          return item
        })

        Object.assign(this.prePayFeeOrderInfo, data)
        this.computeReceivedAmount()
      } catch (error) {
        this.$message.error(this.$t('prePayFeeOrder.loadError'))
      }
    },

    async initPageData() {
      try {
        const primeRates = await getDict('pay_fee_detail', 'prime_rate')
        this.prePayFeeOrderInfo.primeRates = primeRates
      } catch (error) {
        console.error('获取支付方式字典失败:', error)
      }
    },

    selectAllRule(event) {
      if (event.target.checked) {
        this.prePayFeeOrderInfo.selectRuleIds = this.prePayFeeOrderInfo.rules.map(item => item.ruleId)
        this.prePayFeeOrderInfo.rules.forEach(item => item.selected = 'Y')
      } else {
        this.prePayFeeOrderInfo.selectRuleIds = []
        this.prePayFeeOrderInfo.rules.forEach(item => item.selected = 'N')
      }
      this.computeReceivedAmount()
    },

    selectOneRule() {
      const selectedRuleIds = this.prePayFeeOrderInfo.selectRuleIds
      this.prePayFeeOrderInfo.rules.forEach(rule => {
        rule.selected = selectedRuleIds.includes(rule.ruleId) ? 'Y' : 'N'
      })
      this.computeReceivedAmount()
    },

    selectOneDiscount() {
      this.computeReceivedAmount()
    },

    async selectMeterRule() {
      const showMeterRule = this.prePayFeeOrderInfo.showMeterRule
      if (showMeterRule == 'Y') {
        this.prePayFeeOrderInfo.showMeterRule = 'N'
      } else {
        this.prePayFeeOrderInfo.showMeterRule = 'Y'
        await this.loadMeterFeeRuleInfo()
      }
      this.$forceUpdate()
    },

    async loadMeterFeeRuleInfo() {
      try {
        const params = {
          communityId: this.prePayFeeOrderInfo.communityId
        }
        const response = await getMeterFeeRuleInfo(params)
        this.prePayFeeOrderInfo.meterRules = response.data
      } catch (error) {
        this.$message.error(this.$t('prePayFeeOrder.loadMeterError'))
      }
    },

    switchRuleFlag(rule) {
      if (rule.showFlag == 'Y') {
        rule.showFlag = 'N'
      } else {
        rule.showFlag = 'Y'
      }
      this.$forceUpdate()
    },

    changeMonth(cycles, rule) {
      this.prePayFeeOrderInfo.rules.forEach(item => {
        item.receivedAmount = 0.00
        if (item.cycles != '') {
          item.receivedAmount = (parseFloat(item.cycles) * parseFloat(item.monthAmount)).toFixed(2)
        }
      })
      this.computeReceivedAmount()
      this.computePreFeeDiscount(cycles, rule)
    },

    computeReceivedAmount() {
      let receivedAmount = 0.00
      const selectedRuleIds = this.prePayFeeOrderInfo.selectRuleIds

      if (selectedRuleIds.length > 0) {
        this.prePayFeeOrderInfo.rules.forEach(item => {
          if (selectedRuleIds.includes(item.ruleId)) {
            receivedAmount += parseFloat(item.receivedAmount)

            if (item.selectedDiscountIds && item.selectedDiscountIds.length > 0) {
              item.discounts.forEach(discount => {
                if (discount.ruleId == '102020008') {
                  return;
                }
                if (item.selectedDiscountIds.includes(discount.discountId)) {
                  receivedAmount += parseFloat(discount.discountPrice)
                }
              })
            }
          }
        })
      }

      this.prePayFeeOrderInfo.receivedAmount = receivedAmount.toFixed(2)
      this.computeMeterReceivedAmount()
    },

    computeMeterReceivedAmount() {
      if (this.prePayFeeOrderInfo.meterRules.length < 1) return

      let receivedAmount = parseFloat(this.prePayFeeOrderInfo.receivedAmount)
      this.prePayFeeOrderInfo.meterRules.forEach(item => {
        receivedAmount += parseFloat(item.amount)
      })
      this.prePayFeeOrderInfo.receivedAmount = receivedAmount.toFixed(2)
    },

    computePreFeeDiscount(cycles, rule) {
      rule.showFlag = 'Y'
      // 这里需要调用折扣计算接口，由于接口未定义，暂时注释
      // await computePreFeeDiscount({ ruleId: rule.ruleId, cycles })
    },

    handleGoBack() {
      this.$router.go(-1)
    },

    handleQrCodePay() {
      if (!this.validatePayment()) return

      const payType = this.getQrCodePayType()
      this.openConfirmDialog(payType)
    },

    handleCommonPay() {
      if (!this.validatePayment()) return
      this.openConfirmDialog('common')
    },

    validatePayment() {
      if (!this.prePayFeeOrderInfo.primeRate) {
        this.$message.warning(this.$t('prePayFeeOrder.selectPaymentMethod'))
        return false
      }

      const meterRules = this.prePayFeeOrderInfo.meterRules.filter(item => item.amount > 0)
      if (this.prePayFeeOrderInfo.selectRuleIds.length < 1 && meterRules.length < 1) {
        this.$message.warning(this.$t('prePayFeeOrder.selectPrepaidFee'))
        return false
      }

      return true
    },

    getQrCodePayType() {
      if (this.prePayFeeOrderInfo.primeRate == '10') return 'qinghe'
      if (this.prePayFeeOrderInfo.primeRate == '12') return 'yudifu'
      return 'qrCode'
    },

    openConfirmDialog(payType) {
      const rules = this.prePayFeeOrderInfo.rules.filter(item =>
        this.prePayFeeOrderInfo.selectRuleIds.includes(item.ruleId)
      )

      const meterRules = this.prePayFeeOrderInfo.meterRules.filter(item => item.amount > 0)

      const confirmData = {
        primeRate: this.prePayFeeOrderInfo.primeRate,
        rules: rules,
        meterRules: meterRules,
        remark: this.prePayFeeOrderInfo.remark,
        communityId: this.prePayFeeOrderInfo.communityId,
        payerObjId: this.prePayFeeOrderInfo.payerObjId,
        ownerId: this.prePayFeeOrderInfo.ownerId,
        payerObjName: this.prePayFeeOrderInfo.payerObjName,
        receivedAmount: this.prePayFeeOrderInfo.receivedAmount,
        payType: payType,
        way: this.prePayFeeOrderInfo.way
      }

      this.$refs.confirmDialog.open(confirmData)
    },

    handlePaymentSuccess() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.pre-pay-fee-order-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .card-content {

      .left-panel,
      .right-panel {
        padding: 10px;
      }

      .form-label {
        font-size: 14px;
        color: #606266;
      }

      .amount-label {
        font-size: 30px;
        color: red;
        font-weight: bold;
      }
    }
  }

  .table-section {
    margin-top: 20px;

    .fee-table,
    .discount-table {
      margin-bottom: 10px;
    }
  }

  .meter-section {
    margin-top: 20px;

    .meter-header {
      margin-bottom: 10px;
      margin-left: 50px;
    }
  }

  .action-buttons {
    display: flex;
    justify-content: flex-end;
    margin-top: 30px;
  }
}

::v-deep .prePayFeeOrderCreateTime .el-input__inner {
  border: none;
  outline: none;
  background: transparent;
}
</style>