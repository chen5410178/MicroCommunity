<template>
  <div class="payFeeOrder-container padding">
    <el-card class="ibox">
      <div slot="header" class="flex justify-between">
        <div>
          <span>
            {{ $t('payFeeOrder.orderCharge') }}
          </span>{{ payFeeOrderInfo.payerObjName }}
          <span v-if="payFeeOrderInfo.payerObjType == '3333'" @click="_viewRoomData()">
            <view-room-data ref="viewRoomData"></view-room-data>
          </span>
        </div>
        <div class="" >
          <el-button type="button" class="btn btn-primary btn-sm" style="margin-left:10px" @click="goBack()">
            <i class="fa fa-times"></i>
            {{ $t('payFeeOrder.back') }}
          </el-button>
        </div>
      </div>
      <div class="">
        <el-row :gutter="20">
          <el-col :span="14" style="border-right: solid #f4f4f4 2px;padding-left: 30px;">
            <el-form :model="payFeeOrderInfo" label-width="120px">
              <el-row>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.feeId') + ':'">
                    <span @click="_viewFee()" class="hand">{{ payFeeOrderInfo.feeId }}</span>
                    <view-fee-data ref="viewFeeData"></view-fee-data>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.feeName') + ':'">
                    <span @click="_viewFeeConfig()" class="hand">{{ payFeeOrderInfo.feeName }}</span>
                    <view-fee-config-data ref="viewFeeConfigData"></view-fee-config-data>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.feeType') + ':'">
                    {{ payFeeOrderInfo.feeTypeCdName }}
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.billingPeriod') + ':'">
                    {{ dateFormat(payFeeOrderInfo.startTime) }}~{{ dateFormat(payFeeOrderInfo.endTime) }}
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row v-if="payFeeOrderInfo.builtUpArea">
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.area') + ':'">
                    {{ payFeeOrderInfo.builtUpArea }}
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.unitPrice') + ':'">
                    {{ payFeeOrderInfo.squarePrice }}
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row v-if="payFeeOrderInfo.builtUpArea">
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.additionalFee') + ':'">
                    {{ payFeeOrderInfo.additionalAmount }}
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row v-else>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.fixedFee') + ':'">
                    {{ payFeeOrderInfo.additionalAmount }}
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.ownerAccount') + ':'">
                    <el-checkbox v-model="useUserAccount" @change="_useUserAccountChange"></el-checkbox>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.giftPoints') + ':'">
                    {{ payFeeOrderInfo.integralQuantity }}
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.amountReceivable')" style="line-height: 50px;padding-right:0px;">
                    <span style="font-size: 30px;color: red;padding-left:0px;">￥{{ payFeeOrderInfo.totalFeePrice }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.advancePayment')" style="line-height: 50px;padding-right:0px;">
                    <span style="font-size: 30px;color: red;padding-left:0px;">
                      ￥{{ payFeeOrderInfo.ruleAmount }}
                      <a href="javascript:void(0)" style="font-size: 18px;" @click="_doOpenPrePayFee()">({{ $t('payFeeOrder.prePay') }})</a>
                    </span>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-col>
          <el-col :span="10" style="padding-left: 30px;">
            <el-form :model="payFeeOrderInfo" label-width="100px">
              <el-row>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.chargeTime')">
                    <el-input v-model="payFeeOrderInfo.createTime" :placeholder="$t('payFeeOrder.requiredChargeTime')" readonly class="payFeeOrderCreateTime" style="border: none; outline: none;"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.paymentMethod')">
                    <el-select v-model="payFeeOrderInfo.primeRate" :placeholder="$t('payFeeOrder.requiredPaymentMethod')" style="width:100%">
                      <el-option :label="$t('payFeeOrder.requiredPaymentMethod')" value="" disabled></el-option>
                      <template v-for="(item, index) in payFeeOrderInfo.primeRates"  >
                        <el-option :key="index" v-if="item.statusCd != '5' && item.statusCd != '6' && item.statusCd != '8'" :value="item.statusCd" :label="item.name"></el-option>
                      </template>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.actualAmount')">
                    <el-input v-model="payFeeOrderInfo.receivedAmount" :placeholder="$t('payFeeOrder.actualAmountPlaceholder')" v-if="payFeeOrderInfo.receivedAmountSwitch=='1'"></el-input>
                    <span v-if="payFeeOrderInfo.receivedAmountSwitch=='2'" style="font-size: 15px;">￥{{ payFeeOrderInfo.receivedAmount }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row v-if="payFeeOrderInfo.primeRate == '7'">
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.attachment')">
                    <upload-file ref="uploadFile" :callBackListener="'payFeeOrder'" :callBackFunction="'notifyPrimaryFile'" :namespace="'payFeeOrder'"></upload-file>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item :label="$t('payFeeOrder.remark')">
                    <el-input type="textarea" :placeholder="$t('payFeeOrder.remarkPlaceholder')" v-model="payFeeOrderInfo.remark"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row style="margin-top: 30px;">
                <el-col :span="6">
                  <el-button type="success" size="large" block v-if="payFeeOrderInfo.primeRate == '3' || payFeeOrderInfo.primeRate == '4' || payFeeOrderInfo.primeRate == '10' || payFeeOrderInfo.primeRate == '12'" @click="payFeeOrderInfo.primeRate == '10' ? _openPayFee('qinghe') : (payFeeOrderInfo.primeRate == '12' ? _openPayFee('yudifu') : _openPayFee('qrCode'))">
                    {{ $t('payFeeOrder.scanCharge') }}
                  </el-button>
                </el-col>
                <el-col :span="6">
                  <el-button type="primary" size="large" block v-if="payFeeOrderInfo.offlinePayFeeSwitch !='2'" @click="_openPayFee('common')">
                    {{ $t('payFeeOrder.submitCharge') }}
                  </el-button>
                </el-col>
              </el-row>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </el-card>
    <order-pre-pay-fee ref="orderPrePayFee" @changeUserAmountPrice="onChangeUserAmountPrice"></order-pre-pay-fee>
    <pay-fee-deposit ref="payFeeDeposit"></pay-fee-deposit>
    <pre-pay-fee-dialog ref="prePayFeeDialog" @notify-pre-fee-order="onNotifyPreFeeOrder"></pre-pay-fee-dialog>
    <pay-fee-order-confirm ref="payFeeOrderConfirm" @submitSuccess="submitSuccess"></pay-fee-order-confirm>
    <pay-fee-discount ref="payFeeDiscount"></pay-fee-discount>
    <pay-fee-coupon ref="payFeeCoupon"></pay-fee-coupon>
    <prestore-account2 ref="prestoreAccount2"></prestore-account2>
    <pay-fee-order-result ref="payFeeOrderResult" @back="handleClose" />

  </div>
</template>

<script>
import { getCommunityId, getDict } from '@/api/community/communityApi'
import {listFeeObj} from '@/api/fee/payFeeOrderApi'
import ViewRoomData from '@/components/fee/viewRoomData.vue'
import ViewFeeData from '@/components/fee/viewFeeData.vue'
import ViewFeeConfigData from '@/components/fee/viewFeeConfigData.vue'
import UploadFile from '@/components/upload/uploadFile.vue'
import OrderPrePayFee from '@/components/fee/orderPrePayFee.vue'
import PayFeeDeposit from '@/components/fee/payFeeDeposit.vue'
import PrePayFeeDialog from '@/components/fee/prePayFeeDialog.vue'
import PayFeeOrderConfirm from '@/components/fee/payFeeOrderConfirm.vue'
import PayFeeDiscount from '@/components/fee/payFeeDiscount.vue'
import PayFeeCoupon from '@/components/fee/payFeeCoupon.vue'
import PrestoreAccount2 from '@/components/fee/prestoreAccount2.vue'
import { dateFormat,getDateStr } from '@/utils/dateUtil'
import PayFeeOrderResult from '@/components/fee/payFeeOrderResult.vue'


export default {
  name: 'payFeeOrderList',
  components: {
    ViewRoomData,
    ViewFeeData,
    ViewFeeConfigData,
    UploadFile,
    OrderPrePayFee,
    PayFeeDeposit,
    PrePayFeeDialog,
    PayFeeOrderConfirm,
    PayFeeDiscount,
    PayFeeCoupon,
    PrestoreAccount2,
    PayFeeOrderResult
  },
  computed: {
    filteredPrimeRates() {
      // 过滤掉 statusCd 为 5、6、8 的选项
      const list = this.payFeeOrderInfo && Array.isArray(this.payFeeOrderInfo.primeRates)
        ? this.payFeeOrderInfo.primeRates
        : []
      return list.filter(item => item && item.statusCd !== '5' && item.statusCd !== '6' && item.statusCd !== '8')
    }
  },
  data() {
    return {
      useUserAccount: true, // 对应 checkbox
      communityId: '',
      payFeeOrderInfo: {
        feeId: '',
        feeName: '',
        feeTypeCdName: '',
        primeRates: [],
        primeRate: '',
        startTime: '',
        endTime: '',
        cycles: '1',
        totalFeePrice: 0.00,
        receivedAmount: 0.0,
        orgReceivedAmount: 0.0,
        communityId: '',
        payerObjName: '',
        payerObjId: '',
        payerObjType: '',
        remark: '',
        builtUpArea: 0.0,
        squarePrice: 0.0,
        additionalAmount: 0.0,
        receiptId: '',
        showEndTime: '',
        preFees: [],
        integralAmount: '',
        cashAmount: '',
        couponAmount: '',
        selectDiscount: [],
        totalDiscountMoney: 0.0,
        scale: 1,
        decimalPlace: 2,
        receivedAmountSwitch: '',
        createTime: getDateStr(),
        totalUserAmount: 0.0,
        viewPreFeeAmount: 0.0,
        deductionAmount: 0.0,
        redepositAmount: 0.0,
        selectPreFees: [],
        authCode: '',
        orderId: '',
        offlinePayFeeSwitch: '1',
        flag: '',
        custEndTime: '',
        configId: '',
        roomName: '',
        sign: 1,
        integralQuantity: 0,
        primeFile: '',
        rules: [],
        ruleAmount: 0
      }
    }
  },
  mounted() {
    this._initMethod()
  },
  methods: {
    async _initMethod() {
      this.payFeeOrderInfo.feeId = this.$route.query.feeId || ''
      this.communityId = getCommunityId()
      await this.getDictData()
      this.listAndComputeFeeInfo()
    },
    async getDictData() {
      try {
        const data = await getDict('pay_fee_detail', 'prime_rate')
        this.payFeeOrderInfo.primeRates = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
   async listAndComputeFeeInfo() {
      if (!this.payFeeOrderInfo.feeId) {
        return
      }
      // 这里需要调用 API
      // 假设有 listFeeObj API
      const { data } = await listFeeObj({ communityId: this.communityId, feeId: this.payFeeOrderInfo.feeId })
      this.payFeeOrderInfo = { ...this.payFeeOrderInfo, ...data }
      this.payFeeOrderInfo.remark = ''
      this.payFeeOrderInfo.totalFeePrice = this.payFeeOrderInfo.feeTotalPrice
      this.payFeeOrderInfo.receivedAmount = this.payFeeOrderInfo.feeTotalPrice
      this.payFeeOrderInfo.orgReceivedAmount = this.payFeeOrderInfo.feeTotalPrice
      this.$refs.orderPrePayFee.loadPreFees({
        payerObjId: this.payFeeOrderInfo.payerObjId,
        payerObjType: this.payFeeOrderInfo.payerObjType,
        feeId: this.payFeeOrderInfo.feeId
      })
      this.$refs.payFeeDiscount.computeFeeDiscount({
        feeId: this.payFeeOrderInfo.feeId,
        cycles: 1,
        payerObjId: this.payFeeOrderInfo.payerObjId,
        payerObjType: this.payFeeOrderInfo.payerObjType,
        endTime: this.payFeeOrderInfo.endTime,
        custEndTime: this.payFeeOrderInfo.custEndTime
      })
      this.$refs.payFeeCoupon.computeFeeCoupon({
        feeId: this.payFeeOrderInfo.feeId,
        cycles: 1,
        payerObjId: this.payFeeOrderInfo.payerObjId,
        payerObjType: this.payFeeOrderInfo.payerObjType,
        endTime: this.payFeeOrderInfo.endTime
      })
      this.$refs.payFeeDeposit.open({
        payerObjId: this.payFeeOrderInfo.payerObjId,
        payerObjType: this.payFeeOrderInfo.payerObjType
      })
      this._listFeeIntegral()
    },
    _useUserAccountChange(e) {
      if (e) {
        // 刷新预付账户组件数据
        this.$refs.orderPrePayFee.loadPreFees({
          payerObjId: this.payFeeOrderInfo.payerObjId,
          feeId: this.payFeeOrderInfo.feeId
        })
      } else {
        // 清空组件内部状态（如有需要可在子组件暴露 clear 方法）
        // this.$refs.orderPrePayFee.clear()
      }
    },
    payFeeValidate() {
      // 验证逻辑
      if(this.payFeeOrderInfo.primeRate == '') {
        this.$message.error(this.$t('payFeeOrder.requiredPrimeRate'))
        return false
      }
      return true
    },
    _openPayFee(_type) {
      if (!this.payFeeValidate()) {
        return
      }
      this.payFeeOrderInfo.payType = _type
      //this._computeFeeOrderReceivedAmount()
      this.$refs.payFeeOrderConfirm.open(this.payFeeOrderInfo)
    },
    _getFixedNum(num) {
      return parseFloat(num).toFixed(2)
    },
    _computeFeeOrderReceivedAmount() {
      let _receivedAmount = 0
      let _totalFeePrice = this.payFeeOrderInfo.totalFeePrice
      let _ruleAmount = parseFloat(this.payFeeOrderInfo.ruleAmount)
      let _totalUserAmount = this.payFeeOrderInfo.totalUserAmount
      let _totalDiscountMoney = this.payFeeOrderInfo.totalDiscountMoney
      _receivedAmount = _totalFeePrice - _totalDiscountMoney
      if (_totalUserAmount >= _receivedAmount) {
        _receivedAmount = 0
      } else {
        _receivedAmount = _receivedAmount - _totalUserAmount
      }
      _receivedAmount += _ruleAmount
      this.payFeeOrderInfo.receivedAmount = this._getFixedNum(_receivedAmount)
      this.payFeeOrderInfo.orgReceivedAmount = this.payFeeOrderInfo.receivedAmount
    },
    async _listFeeIntegral() {
      // 调用 API
      // const { data } = await computePayFeeIntegral({ feeId: this.payFeeOrderInfo.feeId, ... })
      this.payFeeOrderInfo.integralQuantity = 0 // 示例
    },
    _viewFeeConfig() {
      this.$refs.viewFeeConfigData.open({ configId: this.payFeeOrderInfo.configId })
    },
    _viewFee() {
      this.$refs.viewFeeData.open({ feeId: this.payFeeOrderInfo.feeId })
    },
    _viewRoomData() {
      this.$refs.viewRoomData.open({ roomId: this.payFeeOrderInfo.payerObjId })
    },
    _doOpenPrePayFee() {
      this.$refs.prePayFeeDialog.open({
        payerObjId: this.payFeeOrderInfo.payerObjId,
        payerObjType: this.payFeeOrderInfo.payerObjType
      })
    },
    onNotifyPreFeeOrder(payload) {
      // 子组件回传选择的规则与预付款金额
      const { rules = [], ruleAmount = 0 } = payload || {}
      this.payFeeOrderInfo.rules = rules
      this.payFeeOrderInfo.ruleAmount = ruleAmount
      this._computeFeeOrderReceivedAmount()
    },
    onChangeUserAmountPrice(payload) {
      const { totalUserAmount = 0, preFees = [], selectPreFees = [] } = payload || {}
      this.payFeeOrderInfo.totalUserAmount = totalUserAmount
      this.payFeeOrderInfo.preFees = preFees
      this.payFeeOrderInfo.selectPreFees = selectPreFees
      // 清零积分和账户直接金额，保持与 md 逻辑一致
      this.payFeeOrderInfo.integralAmount = 0
      this.payFeeOrderInfo.cashAmount = 0
      this.payFeeOrderInfo.couponAmount = 0
      this._computeFeeOrderReceivedAmount()
    },
    dateFormat,
    goBack() {
      this.$router.go(-1)
    },
    handleClose() {
      this.$router.go(-1)
    },
    submitSuccess(payload) {
      this.$refs.payFeeOrderResult.open(payload)
    }
  }
}
</script>

<style lang="scss" scoped>
.payFeeOrder-container {
  .ibox {
    margin-bottom: 20px;
  }
  .hand {
    cursor: pointer;
  }
}
</style>