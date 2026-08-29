<template>
  <el-dialog
    :title="$t('batchPayConfirm.title')"
    :visible.sync="visible"
    width="60%"
    :before-close="handleClose"
  >
    <el-form label-width="120px">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('batchPayConfirm.feeAmount')">
            <el-input
              v-model="batchPayConfirmInfo.feePrices"
              type="number"
              @blur="_computeTransforAmount"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('batchPayConfirm.discountAmount')">
            <span>{{batchPayConfirmInfo.discount}}{{$t('batchPayConfirm.yuan')}}</span>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="batchPayConfirmInfo.transforAmount > 0">
        <el-col :span="12">
          <el-form-item :label="$t('batchPayConfirm.transferAmount')">
            <span>{{batchPayConfirmInfo.transforAmount}}{{$t('batchPayConfirm.yuan')}}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('batchPayConfirm.transferObject')">
            <el-select
              v-model="batchPayConfirmInfo.payerObjId"
              @change="_selectTransforPreFee"
              style="width: 100%"
            >
              <el-option disabled value="">{{$t('batchPayConfirm.selectTransferObject')}}</el-option>
              <el-option
                v-for="(item,index) in batchPayConfirmInfo.payerObjs"
                :key="index"
                :value="item.payerObjId"
                :label="item.payerObjName"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12" v-if="batchPayConfirmInfo.transforAmount > 0">
          <el-form-item :label="$t('batchPayConfirm.transferFee')">
            <el-select
              v-model="batchPayConfirmInfo.ruleId"
              style="width: 100%"
            >
              <el-option disabled value="">{{$t('batchPayConfirm.selectTransferFee')}}</el-option>
              <el-option
                v-for="(item,index) in batchPayConfirmInfo.transforPreFees"
                :key="index"
                :value="item.ruleId"
                :label="item.feeName"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('batchPayConfirm.accountDeduction')">
            <el-select
              v-model="batchPayConfirmInfo.acctId"
              style="width: 100%"
              @change="_selectAccountChange"
            >
              <el-option value="">{{$t('batchPayConfirm.selectDeductionAccount')}}</el-option>
              <el-option
                v-for="(item,index) in batchPayConfirmInfo.accounts"
                :key="index"
                :value="item.acctId"
                :label="`${item.acctName}~${item.amount}元`"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('batchPayConfirm.paymentMethod')">
            <el-select
              v-model="batchPayConfirmInfo.primeRate"
              style="width: 100%"
              @change="_watchPrimeRateChange"
            >
              <el-option disabled value="">{{$t('batchPayConfirm.selectPaymentMethod')}}</el-option>
              <template v-if="batchPayConfirmInfo.payType == 'common'">
                <el-option
                  v-for="(item,index) in commonPrimeRates"
                  :key="index"
                  :value="item.statusCd"
                  :label="item.name"
                />
              </template>
              <template v-else>
                <el-option
                  v-for="(item,index) in qrPrimeRates"
                  :key="index"
                  :value="item.statusCd"
                  :label="item.name"
                />
              </template>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('batchPayConfirm.remark')">
            <el-input
              v-model="batchPayConfirmInfo.remark"
              type="textarea"
              :placeholder="$t('batchPayConfirm.enterRemark')"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 扫码输入框 -->
      <el-row v-if="batchPayConfirmInfo.payType == 'qrCode' && (batchPayConfirmInfo.primeRate == '3' || batchPayConfirmInfo.primeRate == '4' || batchPayConfirmInfo.primeRate == '10')">
        <el-col :span="12">
          <el-form-item :label="$t('batchPayConfirm.authCode')">
            <el-input
              v-model="batchPayConfirmInfo.authCode"
              @keyup.enter.native="_qrCodePayFee"
              :placeholder="$t('batchPayConfirm.scanCode')"
              ref="authCodeInput"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 渝地付二维码 -->
      <el-row v-if="batchPayConfirmInfo.payType == 'qrCode' && batchPayConfirmInfo.primeRate == '12'">
        <el-col :span="24" style="text-align: center;">
          <div class="qr-code-container">
            <div v-if="batchPayConfirmInfo.qrCodeUrl">
              <img :src="batchPayConfirmInfo.qrCodeUrl" style="max-width: 240px; margin-bottom: 10px;">
              <p>{{$t('batchPayConfirm.scanQrCode')}}</p>
            </div>
            <div v-else>
              <p>{{$t('batchPayConfirm.generatingQrCode')}}</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{$t('batchPayConfirm.close')}}</el-button>
      <el-button
        type="primary"
        @click="_doPayFee"
        v-if="batchPayConfirmInfo.payType == 'common'"
      >
        {{$t('batchPayConfirm.confirmPayment')}}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'
import { payBatchFee, qrCodePayment, checkPayFinish, queryCommunityOwnerAccount, listPrePayFeeRule, queryFeeReceipt, listFeePrintPage } from '@/api/fee/batchPayFeeOrderApi'
import {dateFormatH} from '@/utils/dateUtil'
export default {
  name: 'BatchPayConfirm',
  data() {
    return {
      visible: false,
      batchPayConfirmInfo: {
        qrCodeUrl: null,
        payType: '',
        fees: [],
        primeRate: '',
        primeRates: [],
        payerObjName: '',
        orderId: '',
        paymentPoolId: '',
        authCode: '',
        feePrices: '',
        orgFeePrices: 0.0,
        receiptIds: '',
        printUrl: '/#/pages/property/printPayFee',
        accountAmount: 0,
        acctId: '',
        isBatch: '',
        accounts: [],
        ownerId: '',
        rules: [],
        ruleAmount: 0.0,
        transforAmount: 0,
        ruleId: '',
        transforPreFees: [],
        payerObjId: '',
        payerObjType: '',
        payerObjs: []
      },
      communityId: ''
    }
  },
  computed: {
    commonPrimeRates() {
      const list = this.batchPayConfirmInfo.primeRates || []
      return list.filter(item => item.statusCd != '5' && item.statusCd != '6' && item.statusCd != '8')
    },
    qrPrimeRates() {
      const list = this.batchPayConfirmInfo.primeRates || []
      return list.filter(item => item.statusCd == '3' || item.statusCd == '4' || item.statusCd == '10' || item.statusCd == '12')
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initDictData()
    this._listFeePrintPages()
  },
  methods: {
    open(params) {
      this.visible = true
      this._resetBatchPayConfirm()
      Object.assign(this.batchPayConfirmInfo, params)
      this.batchPayConfirmInfo.orgFeePrices = params.feePrices
      
      this.$nextTick(() => {
        if (this.batchPayConfirmInfo.payType == 'qrCode') {
          setTimeout(() => {
            if (this.$refs.authCodeInput) {
              this.$refs.authCodeInput.focus()
            }
          }, 500)
        }
      })
      
      this._loadOwnerAccts()
    },
    
    handleClose() {
      this.visible = false
    },
    
    async _initDictData() {
      try {
        const data = await getDict('pay_fee_detail', 'prime_rate')
        this.batchPayConfirmInfo.primeRates = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    
    async _listFeePrintPages() {
      try {
        const params = {
          page: 1,
          row: 1,
          state: 'T',
          communityId: this.communityId
        }
        const res = await listFeePrintPage(params)
        const feePrintPages = res.data
        if (feePrintPages && feePrintPages.length > 0) {
          this.batchPayConfirmInfo.printUrl = feePrintPages[0].url
        }
      } catch (error) {
        console.error('获取打印页面失败:', error)
      }
    },

    async _loadOwnerAccts() {
      try {
        const params = {
          page: 1,
          row: 100,
          ownerId: this.batchPayConfirmInfo.ownerId,
          communityId: this.communityId
        }
        
        const response = await queryCommunityOwnerAccount(params)
        
        if (response.data && response.data.length > 0) {
          this.batchPayConfirmInfo.accounts = response.data
        }
      } catch (error) {
        console.error('加载账户失败:', error)
      }
    },
    _selectAccountChange() {
      const acctId = this.batchPayConfirmInfo.acctId
      const orgFeePrices = parseFloat(this.batchPayConfirmInfo.orgFeePrices) || 0

      if (!acctId) {
        // 取消账户扣款：恢复原始缴费金额
        this.batchPayConfirmInfo.accountAmount = 0
        this.batchPayConfirmInfo.feePrices = orgFeePrices.toFixed(2)
        this._computeTransforAmount()
        return
      }

      const account = this.batchPayConfirmInfo.accounts.find(item => item.acctId === acctId)
      if (!account) {
        return
      }

      this.batchPayConfirmInfo.accountAmount = parseFloat(account.amount) || 0
      // 账户实际抵扣不超过原始应缴
      const deductAmount = Math.min(this.batchPayConfirmInfo.accountAmount, orgFeePrices)
      // 自动修改缴费金额（最低为 0）
      this.batchPayConfirmInfo.feePrices = Math.max(orgFeePrices - deductAmount, 0).toFixed(2)

      this._computeTransforAmount()
    },
    _computeTransforAmount() {
      const feePrices = parseFloat(this.batchPayConfirmInfo.feePrices)
      const orgFeePrices = parseFloat(this.batchPayConfirmInfo.orgFeePrices)
      const accountAmount = parseFloat(this.batchPayConfirmInfo.accountAmount) || 0
      // 账户实际抵扣不超过原始应缴
      const deductAmount = Math.min(accountAmount, orgFeePrices)
      // 转存金额 = 实付金额 - (原始应缴 - 账户抵扣)
      const transforAmount = feePrices - (orgFeePrices - deductAmount)

      if (transforAmount < 0) {
        const minPrices = (orgFeePrices - deductAmount).toFixed(2)
        this.batchPayConfirmInfo.feePrices = minPrices
        this.$message.warning(`输入金额不能小于${minPrices}`)
        return
      }

      this.batchPayConfirmInfo.transforAmount = transforAmount.toFixed(2)
    },
    
    async _doPayFee() {
      if (!this.batchPayConfirmInfo.primeRate) {
        this.$message.warning('请选择支付方式')
        return
      }
      
      const payFees = this._preparePayFees()
      const paramData = this._mergeTransforPreFee()
      
      const data = {
        communityId: this.communityId,
        feeConfigs: payFees,
        primeRate: this.batchPayConfirmInfo.primeRate,
        acctId: this.batchPayConfirmInfo.acctId,
        remark: this.batchPayConfirmInfo.remark,
        amount: paramData.feePrices,
        receivedAmount: this.batchPayConfirmInfo.feePrices,
        discountAmount: this.batchPayConfirmInfo.discount,
        rules: paramData.rules
      }
      
      try {
        const response = await payBatchFee(data)
        
        if (response.code === 0) {
          this._doDealPayResult(response)
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        console.error('支付失败:', error)
        this.handleClose()
        this.$message.error('支付失败')
      }
    },
    
    async _qrCodePayFee() {
      if (!this.batchPayConfirmInfo.primeRate) {
        this.$message.warning('请选择支付方式')
        return
      }
      
      const payFees = this._preparePayFees()
      const payType = (this.batchPayConfirmInfo.primeRate === '12') ? 'yudifu' : 'qrCode'
      const paramData = this._mergeTransforPreFee()
      
      const data = {
        communityId: this.communityId,
        feeConfigs: payFees,
        primeRate: this.batchPayConfirmInfo.primeRate,
        acctId: this.batchPayConfirmInfo.acctId,
        remark: this.batchPayConfirmInfo.remark,
        amount: paramData.feePrices,
        receivedAmount: this.batchPayConfirmInfo.feePrices,
        discountAmount: this.batchPayConfirmInfo.discount,
        rules: paramData.rules,
        subServiceCode: 'fee.payBatchFee',
        payType: payType,
        isBatch: 'yes'
      }
      
      try {
        const response = await qrCodePayment(data)
        
        if (payType === 'yudifu') {
          if (response.code === 0) {
            this.batchPayConfirmInfo.qrCodeUrl = response.data.qrCode
            this.$forceUpdate()
          } else {
            this.$message.error(response.msg || '生成二维码失败')
          }
          return
        }
        
        if (response.code === 404) {
          this.$message.error(response.msg)
          if (response.data && response.data.orderId) {
            this.batchPayConfirmInfo.orderId = response.data.orderId
            this.batchPayConfirmInfo.paymentPoolId = response.data.paymentPoolId
            setTimeout(() => this._qrCodeCheckPayFinish(), 5000)
          }
        } else if (response.code === 0) {
          this._doDealPayResult(response)
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        console.error('扫码支付失败:', error)
        this.$message.error('扫码支付失败')
      }
    },
    
    _watchPrimeRateChange(newVal) {
      if (newVal === '12') {
        this._qrCodePayFee()
      }
    },
    
    async _qrCodeCheckPayFinish() {
      const payFees = this._preparePayFees()
      const paramData = this._mergeTransforPreFee()
      
      const data = {
        communityId: this.communityId,
        feeConfigs: payFees,
        primeRate: this.batchPayConfirmInfo.primeRate,
        acctId: this.batchPayConfirmInfo.acctId,
        remark: this.batchPayConfirmInfo.remark,
        amount: paramData.feePrices,
        receivedAmount: this.batchPayConfirmInfo.feePrices,
        discountAmount: this.batchPayConfirmInfo.discount,
        rules: paramData.rules,
        subServiceCode: 'fee.payBatchFee',
        orderId: this.batchPayConfirmInfo.orderId,
        paymentPoolId: this.batchPayConfirmInfo.paymentPoolId
      }
      
      try {
        const response = await checkPayFinish(data)
        
        if (response.code === 404) {
          this.$message.error(response.msg)
        } else if (response.code === 41) {
          setTimeout(() => this._qrCodeCheckPayFinish(), 5000)
        } else {
          this._doDealPayResult(response)
        }
      } catch (error) {
        console.error('检查支付状态失败:', error)
        this.$message.error('检查支付状态失败')
      }
    },
    
    async _doDealPayResult(json) {
      this.handleClose()
      
      setTimeout(async () => {
        try {
          const params = {
            receiptCode: json.data.receiptCode,
            communityId: this.communityId,
            page: 1,
            row: 100
          }
          
          const response = await queryFeeReceipt(params)
          
          if (response.code === 0 && response.data && response.data.length > 0) {
            const receiptIds = response.data.map(item => item.receiptId).join(',')
            this.batchPayConfirmInfo.receiptIds = receiptIds
          }
          
          this.$emit('paymentSuccess', {
            receiptIds: this.batchPayConfirmInfo.receiptIds,
            printUrl: this.batchPayConfirmInfo.printUrl
          })
        } catch (error) {
          console.error('处理支付结果失败:', error)
        }
      }, 2000)
    },
    
    _preparePayFees() {
      const payFees = []
      
      this.batchPayConfirmInfo.fees.forEach(fee => {
        fee.childs.forEach(feeConfig => {
          const payFeeItems = feeConfig.childs.filter(child =>
            feeConfig.selectedFeeIds.includes(child.feeId)
          )
          
          payFees.push({
            configId: feeConfig.configId,
            selectedDiscountIds: feeConfig.selectedDiscountIds,
            payFees: payFeeItems
          })
        })
      })
      
      return payFees
    },
    
    _mergeTransforPreFee() {
      const data = JSON.parse(JSON.stringify(this.batchPayConfirmInfo))
      const ruleId = data.ruleId
      const rules = data.rules
      
      if (ruleId) {
        rules.push({
          ruleId: ruleId,
          cycles: 24,
          receivedAmount: data.transforAmount,
          startMonth: dateFormatH(new Date())
        })
      }
      
      let feePrices = data.feePrices
      
      if (rules.length < 1) {
        data.feePrices = feePrices
        return data
      }
      
      let ruleAmount = 0
      rules.forEach(rule => {
        ruleAmount += parseFloat(rule.receivedAmount)
      })
      
      feePrices = parseFloat(feePrices) - ruleAmount
      data.feePrices = feePrices.toFixed(2)
      return data
    },
    
    async _selectTransforPreFee() {
      const payerObjId = this.batchPayConfirmInfo.payerObjId
      if (!payerObjId) return
      
      const payerObj = this.batchPayConfirmInfo.payerObjs.find(obj => obj.payerObjId === payerObjId)
      if (payerObj) {
        this.batchPayConfirmInfo.payerObjType = payerObj.payerObjType
      }
      
      await this._loadTransforPrePayFees()
    },
    
    async _loadTransforPrePayFees() {
      try {
        const params = {
          communityId: this.communityId,
          payerObjId: this.batchPayConfirmInfo.payerObjId,
          payerObjType: this.batchPayConfirmInfo.payerObjType
        }
        
        const response = await listPrePayFeeRule(params)
        this.batchPayConfirmInfo.transforPreFees = response.data.rules
        this.$forceUpdate()
      } catch (error) {
        console.error('加载转存费用失败:', error)
      }
    },
    
    _resetBatchPayConfirm() {
      const primeRates = this.batchPayConfirmInfo.primeRates
      const printUrl = this.batchPayConfirmInfo.printUrl
      
      this.batchPayConfirmInfo = {
        qrCodeUrl: null,
        payType: '',
        fees: [],
        primeRate: '',
        primeRates: primeRates,
        payerObjName: '',
        orderId: '',
        paymentPoolId: '',
        authCode: '',
        feePrices: '',
        orgFeePrices: 0.0,
        receiptIds: '',
        printUrl: printUrl,
        accountAmount: 0,
        acctId: '',
        accounts: [],
        ownerId: '',
        rules: [],
        ruleAmount: 0.0,
        transforAmount: 0,
        ruleId: '',
        transforPreFees: [],
        payerObjId: '',
        payerObjType: '',
        payerObjs: []
      }
    },
    dateFormatH
  }
}
</script>

<style lang="scss" scoped>
.qr-code-container {
  padding: 20px;
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  background: #f9f9f9;
  
  p {
    margin: 0;
    color: #666;
  }
}

.dialog-footer {
  text-align: right;
}
</style>