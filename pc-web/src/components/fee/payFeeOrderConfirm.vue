<template>
  <el-dialog :title="$t('payFeeOrderConfirm.chargeConfirm')" :visible.sync="visible" width="60%" @close="handleClose">
    <el-form :model="payFeeOrderConfirmInfo" label-width="120px" class="text-left">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('payFeeOrderConfirm.payeeObject') + '：'">
            {{ payFeeOrderConfirmInfo.payerObjName }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payFeeOrderConfirm.amountReceivable') + '：'">
            {{ payFeeOrderConfirmInfo.totalFeePrice }}
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('payFeeOrderConfirm.actualAmount') + '：'">
            {{ payFeeOrderConfirmInfo.receivedAmount }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('payFeeOrderConfirm.transferAmount') + '：'">
            {{ payFeeOrderConfirmInfo.transforAmount }}
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('payFeeOrderConfirm.discountAmount') + '：'">
            {{ payFeeOrderConfirmInfo.totalDiscountMoney.toFixed(2) }}
          </el-form-item>
        </el-col>
        <el-col v-if="payFeeOrderConfirmInfo.transforAmount > 0" :span="12">
          <el-form-item :label="$t('payFeeOrderConfirm.transferFee')">
            <el-select v-model="payFeeOrderConfirmInfo.ruleId" style="width:100%">
              <el-option :label="$t('payFeeOrderConfirm.requiredTransferFee')" value="" disabled></el-option>
              <el-option v-for="(item, index) in payFeeOrderConfirmInfo.transforPreFees" :key="index"
                :value="item.ruleId" :label="item.feeName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" v-if="payFeeOrderConfirmInfo.integralAmount">
        <el-col :span="12">
          <el-form-item :label="$t('payFeeOrderConfirm.pointsDeduction') + '：'">
            {{ payFeeOrderConfirmInfo.integralAmount }}
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" v-if="payFeeOrderConfirmInfo.cashAmount">
        <el-col :span="12">
          <el-form-item :label="$t('payFeeOrderConfirm.accountDeduction') + '：'">
            {{ payFeeOrderConfirmInfo.cashAmount }}
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('payFeeOrderConfirm.remark') + '：'">
            {{ payFeeOrderConfirmInfo.remark }}
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div v-if="payFeeOrderConfirmInfo.payType == 'yudifu'" class="qr-code-container"
      style="width: 100%; text-align: center;">
      <div v-if="payFeeOrderConfirmInfo.qrCodeUrl">
        <img :src="payFeeOrderConfirmInfo.qrCodeUrl" style="max-width: 240px; margin-bottom: 10px;">
        <p>{{ $t('payFeeOrderConfirm.scanPay') }}</p>
      </div>
      <div v-else>
        <p>{{ $t('payFeeOrderConfirm.generatingQrCode') }}</p>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-input v-model="payFeeOrderConfirmInfo.authCode"
        v-if="payFeeOrderConfirmInfo.payType == 'qrCode' || payFeeOrderConfirmInfo.payType == 'qinghe'"
        style="width: 60%;" type="text" @keyup.enter="_qrCodePayFee"
        :placeholder="$t('payFeeOrderConfirm.scanCodePlaceholder')"></el-input>
      <el-button @click="handleClose">{{ $t('payFeeOrderConfirm.close') }}</el-button>
      <el-button v-if="payFeeOrderConfirmInfo.payType == 'common'" type="primary" @click="_payFee">{{
        $t('payFeeOrderConfirm.confirmCharge') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { qrCodePayment, checkPayFinish, payFee, listPrePayFeeRule } from '@/api/fee/payFeeOrderApi'
import { dateFormatH } from '@/utils/dateUtil'

export default {
  name: 'PayFeeOrderConfirm',
  data() {
    return {
      visible: false,
      communityId: '',
      payFeeOrderConfirmInfo: {
        totalDiscountMoney: 0.0,
        totalUserAmount: 0.0,
        qrCodeUrl: null,
        payType: '',
        transforAmount: 0,
        transforPreFees: [],
        ruleId: '',
        feeReceivedAmount: '',
        rules: []
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      // 保留响应式对象，避免替换导致新增属性不响应
      Object.assign(this.payFeeOrderConfirmInfo, data)
      // 确保这些字段存在且为初始值
      this.payFeeOrderConfirmInfo.transforPreFees = []
      this.payFeeOrderConfirmInfo.transforAmount = 0
      this.payFeeOrderConfirmInfo.ruleId = ''
      this.payFeeOrderConfirmInfo.feeReceivedAmount = data.receivedAmount
      const transforAmount = parseFloat(data.receivedAmount) - parseFloat(data.orgReceivedAmount)
      if (transforAmount > 0) {
        this.payFeeOrderConfirmInfo.transforAmount = transforAmount.toFixed(2)
        // 聚焦到输入框
        this._loadTransforPrePayFees()
      }
      this.visible = true
      this.payFeeOrderConfirmInfo.payType = data.payType
      if (this.payFeeOrderConfirmInfo.payType == 'yudifu') {
        this._requestYuDiFuQrCode()
      } else {
        if (this.payFeeOrderConfirmInfo.payType != 'common') {
          setTimeout(() => {
            // 聚焦到输入框
          }, 1000)
        }
      }
    },
    handleClose() {
      this.visible = false
    },
    async _requestYuDiFuQrCode() {
      const printFees = [{
        feeId: this.payFeeOrderConfirmInfo.feeId,
        squarePrice: this.payFeeOrderConfirmInfo.squarePrice,
        additionalAmount: this.payFeeOrderConfirmInfo.additionalAmount,
        feeName: this.payFeeOrderConfirmInfo.feeName,
        amount: this.payFeeOrderConfirmInfo.receivedAmount,
        authCode: '123456'
      }]
      const requestData = Object.assign({}, this.payFeeOrderConfirmInfo, {
        subServiceCode: 'fee.payFee',
        printFees
      })
      try {
        const { data } = await qrCodePayment(requestData)
        if (data.code == 0) {
          this.payFeeOrderConfirmInfo.qrCodeUrl = data.qrCode
        } else {
          this.$message.error(data.msg || '生成支付二维码失败')
        }
      } catch (error) {
        console.error('请求失败处理')
        this.$message.error('请求失败')
      }
    },
    async _qrCodePayFee() {
      this.payFeeOrderConfirmInfo.subServiceCode = 'fee.payFee'
      const data = this._mergeTransforPreFee()
      try {
        const { data: responseData } = await qrCodePayment(data)
        if (responseData.code == 404) {
          this.$message.error(responseData.msg)
          if (responseData.data && responseData.data.orderId) {
            this.payFeeOrderConfirmInfo.orderId = responseData.data.orderId
            this.payFeeOrderConfirmInfo.paymentPoolId = responseData.data.paymentPoolId
            setTimeout(() => {
              this._qrCodeCheckPayFinish()
            }, 5000)
          }
          return
        }
        this.handleClose()
        setTimeout(() => {
          this.$refs.payFeeOrderResult._queryPayFeeReceipt(responseData)
        }, 1500)
      } catch (error) {
        console.error('请求失败处理')
        this.$message.error('请求失败')
      }
    },
    async _qrCodeCheckPayFinish() {

      this.payFeeOrderConfirmInfo.subServiceCode = 'fee.payFee'
      const data = this._mergeTransforPreFee()
      try {
        const { data: responseData } = await checkPayFinish(data)
        if (responseData.code == 404) {
          this.$message.error(responseData.msg)
          return
        } else if (responseData.code == 41) {
          setTimeout(() => {
            this._qrCodeCheckPayFinish()
          }, 30000)
          return
        }
        this.handleClose()
        setTimeout(() => {
          this.$refs.payFeeOrderResult._queryPayFeeReceipt(responseData.data)
        }, 1500)
      } catch (error) {
        console.error('请求失败处理')
        this.$message.error('请求失败')
      }
    },
    async _payFee() {

      const data = this._mergeTransforPreFee()
      try {
        const { data: responseData, code, msg } = await payFee(data)
        if (code == 0) {
          this.handleClose()
          setTimeout(() => {
            this.$emit('submitSuccess', responseData)
          }, 1500)
          return
        } else {
          this.$message.error(msg)
        }
      } catch (error) {
        console.error('请求失败处理')
        this.$message.error('请求失败')
      }
    },
    _mergeTransforPreFee() {
      const data = Object.assign({}, this.payFeeOrderConfirmInfo)
      const ruleId = data.ruleId
      const rules = data.rules
      if (ruleId) {
        rules.push({
          ruleId,
          cycles: 24,
          receivedAmount: data.transforAmount,
          startMonth: dateFormatH(new Date())
        })
      }
      const receivedAmount = data.receivedAmount
      if (rules.length < 1) {
        data.feeReceivedAmount = receivedAmount
        return data
      }
      let ruleAmount = 0
      rules.forEach(rule => {
        ruleAmount += parseFloat(rule.receivedAmount)
      })
      const feeReceivedAmount = parseFloat(receivedAmount) - ruleAmount
      data.feeReceivedAmount = feeReceivedAmount.toFixed(2)
      return data
    },
    async _loadTransforPrePayFees() {
      try {
        const params = {
          communityId: this.communityId,
          payerObjId: this.payFeeOrderConfirmInfo.payerObjId,
          payerObjType: this.payFeeOrderConfirmInfo.payerObjType
        }
        console.log('Loading transfor pre pay fees with params:', params)
        const { data } = await listPrePayFeeRule(params)
        // 使用 $set 确保响应式更新
        this.$set(this.payFeeOrderConfirmInfo, 'transforPreFees', data.rules || [])
      } catch (error) {
        console.error('加载预付费用规则失败:', error)
        // 设置空数组避免界面错误
        this.$set(this.payFeeOrderConfirmInfo, 'transforPreFees', [])
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}

.qr-code-container {
  margin-top: 20px;
}
</style>