<template>
  <el-dialog
    :title="$t('prePayFeeOrderConfirm.title')"
    :visible.sync="visible"
    width="600px"
    :before-close="handleClose"
    class="pre-pay-fee-order-confirm-dialog"
  >
    <div class="confirm-content">
      <el-form label-width="100px">
        <el-form-item :label="$t('prePayFeeOrderConfirm.payerObject')">
          <span>{{ confirmInfo.payerObjName }}</span>
        </el-form-item>
        
        <el-form-item :label="$t('prePayFeeOrderConfirm.receivableAmount')">
          <span style="color: red; font-weight: bold;">{{ confirmInfo.receivedAmount }}</span>
        </el-form-item>
        
        <el-form-item :label="$t('prePayFeeOrderConfirm.remark')">
          <span>{{ confirmInfo.remark }}</span>
        </el-form-item>
      </el-form>

      <!-- 渝地付支付显示二维码 -->
      <div v-if="confirmInfo.payType === 'yudifu'" class="qr-code-container">
        <div v-if="confirmInfo.qrCodeUrl" class="qr-code-content">
          <img :src="confirmInfo.qrCodeUrl" alt="支付二维码" class="qr-code-image">
          <p class="qr-code-tip">{{ $t('prePayFeeOrderConfirm.qrCodeTip') }}</p>
        </div>
        <div v-else class="qr-code-loading">
          <el-alert
            :title="$t('prePayFeeOrderConfirm.generatingQrCode')"
            type="info"
            :closable="false"
          />
        </div>
      </div>

      <!-- 扫码支付输入框 -->
      <div v-if="confirmInfo.payType === 'qrCode' || confirmInfo.payType === 'qinghe'" class="auth-code-input">
        <el-input
          v-model="confirmInfo.authCode"
          :placeholder="$t('prePayFeeOrderConfirm.authCodePlaceholder')"
          @keyup.enter.native="handleQrCodePay"
          clearable
        >
          <template slot="prepend">{{ $t('prePayFeeOrderConfirm.authCode') }}</template>
        </el-input>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('prePayFeeOrderConfirm.close') }}</el-button>
      <el-button 
        v-if="confirmInfo.payType === 'common'" 
        type="primary" 
        @click="handleConfirmPayment"
        :loading="loading"
      >
        {{ $t('prePayFeeOrderConfirm.confirmPayment') }}
      </el-button>
      <el-button 
        v-else-if="confirmInfo.payType !== 'yudifu'" 
        type="primary" 
        @click="handleQrCodePay"
        :loading="loading"
      >
        {{ $t('prePayFeeOrderConfirm.confirmPayment') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { qrCodePayment, checkPayFinish, prePayFee } from '@/api/fee/prePayFeeOrderApi'

export default {
  name: 'PrePayFeeOrderConfirm',
  data() {
    return {
      visible: false,
      loading: false,
      confirmInfo: {
        qrCodeUrl: null,
        primeRate: '',
        rules: '',
        remark: '',
        communityId: '',
        payerObjId: '',
        payerObjName: '',
        receivedAmount: '',
        payType: 'common',
        authCode: '',
        orderId: '',
        paymentPoolId: '',
        feeId: '',
        squarePrice: '',
        additionalAmount: '',
        feeName: '',
        subServiceCode: 'fee.payFee'
      },
      checkTimer: null,
      retryCount: 0,
      MAX_RETRY: 10
    }
  },
  methods: {
    open(confirmData) {
      this.visible = true
      Object.assign(this.confirmInfo, confirmData)
      
      if (this.confirmInfo.payType === 'yudifu') {
        this.requestYuDiFuQrCode()
      } else if (this.confirmInfo.payType !== 'common') {
        this.$nextTick(() => {
          const input = this.$el.querySelector('input')
          if (input) {
            input.focus()
          }
        })
      }
    },
    
    handleClose() {
      this.clearCheckTimer()
      this.visible = false
      this.loading = false
      this.retryCount = 0
      this.resetConfirmInfo()
    },
    
    resetConfirmInfo() {
      this.confirmInfo = {
        qrCodeUrl: null,
        primeRate: '',
        rules: '',
        remark: '',
        communityId: '',
        payerObjId: '',
        payerObjName: '',
        receivedAmount: '',
        payType: 'common',
        authCode: '',
        orderId: '',
        paymentPoolId: '',
        feeId: '',
        squarePrice: '',
        additionalAmount: '',
        feeName: '',
        subServiceCode: 'fee.payFee'
      }
    },
    
    clearCheckTimer() {
      if (this.checkTimer) {
        clearTimeout(this.checkTimer)
        this.checkTimer = null
      }
    },
    
    async requestYuDiFuQrCode() {
      try {
        this.loading = true
        const printFees = [{
          feeId: this.confirmInfo.feeId,
          squarePrice: this.confirmInfo.squarePrice,
          additionalAmount: this.confirmInfo.additionalAmount,
          feeName: this.confirmInfo.feeName,
          amount: this.confirmInfo.receivedAmount,
          authCode: '123456'
        }]

        const requestData = Object.assign({}, this.confirmInfo, {
          printFees: printFees
        })

        const response = await qrCodePayment(requestData)
        if (response.code === 0) {
          this.confirmInfo.qrCodeUrl = response.data.qrCode
          this.startPaymentStatusCheck()
        } else {
          this.$message.error(response.msg || this.$t('prePayFeeOrderConfirm.qrCodeGenerateFailed'))
        }
      } catch (error) {
        this.$message.error(this.$t('prePayFeeOrderConfirm.requestFailed'))
      } finally {
        this.loading = false
      }
    },
    
    startPaymentStatusCheck() {
      this.clearCheckTimer()
      this.checkTimer = setTimeout(() => {
        this.checkPaymentStatus()
      }, 3000)
    },
    
    async checkPaymentStatus() {
      try {
        const printFees = [{
          feeId: this.confirmInfo.feeId,
          squarePrice: this.confirmInfo.squarePrice,
          additionalAmount: this.confirmInfo.additionalAmount,
          feeName: this.confirmInfo.feeName,
          amount: this.confirmInfo.receivedAmount,
          authCode: this.confirmInfo.authCode,
          orderId: this.confirmInfo.orderId
        }]

        const requestData = Object.assign({}, this.confirmInfo, {
          printFees: printFees
        })

        const response = await checkPayFinish(requestData)
        
        if (response.code === 41) {
          this.retryCount++
          if (this.retryCount < this.MAX_RETRY) {
            this.startPaymentStatusCheck()
          } else {
            this.$message.warning(this.$t('prePayFeeOrderConfirm.paymentTimeout'))
            this.handleClose()
          }
          return
        } else if (response.code === 404) {
          this.$message.error(response.msg)
          return
        }
        
        this.handlePaymentSuccess(response.data)
      } catch (error) {
        console.error('支付状态检查失败:', error)
        this.retryCount++
        if (this.retryCount < this.MAX_RETRY) {
          this.startPaymentStatusCheck()
        } else {
          this.$message.error(this.$t('prePayFeeOrderConfirm.paymentCheckFailed'))
          this.handleClose()
        }
      }
    },
    
    async handleQrCodePay() {
      if (!this.confirmInfo.authCode && this.confirmInfo.payType !== 'yudifu') {
        this.$message.warning(this.$t('prePayFeeOrderConfirm.enterAuthCode'))
        return
      }

      try {
        this.loading = true
        const printFees = [{
          feeId: this.confirmInfo.feeId,
          squarePrice: this.confirmInfo.squarePrice,
          additionalAmount: this.confirmInfo.additionalAmount,
          feeName: this.confirmInfo.feeName,
          amount: this.confirmInfo.receivedAmount,
          authCode: this.confirmInfo.authCode
        }]

        const requestData = Object.assign({}, this.confirmInfo, {
          printFees: printFees
        })

        const response = await qrCodePayment(requestData)
        
        if (response.code === 404) {
          this.$message.warning(response.msg)
          if (response.data && response.data.orderId) {
            this.confirmInfo.orderId = response.data.orderId
            this.confirmInfo.paymentPoolId = response.data.paymentPoolId
            this.startPaymentStatusCheck()
          }
          return
        }
        
        this.handlePaymentSuccess(response.data)
      } catch (error) {
        this.$message.error(this.$t('prePayFeeOrderConfirm.paymentFailed'))
      } finally {
        this.loading = false
      }
    },
    
    async handleConfirmPayment() {
      try {
        this.loading = true
        const printFees = [{
          primeRate: this.confirmInfo.primeRate,
          rules: this.confirmInfo.rules,
          remark: this.confirmInfo.remark,
          communityId: this.confirmInfo.communityId,
          payerObjId: this.confirmInfo.payerObjId,
          payerObjName: this.confirmInfo.payerObjName,
          ownerId: this.confirmInfo.ownerId,
          receivedAmount: this.confirmInfo.receivedAmount,
        }]

        const requestData = Object.assign({}, this.confirmInfo, {
          printFees: printFees
        })

        const response = await prePayFee(requestData)
        
        if (response.code === 0) {
          this.handlePaymentSuccess(response.data)
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('prePayFeeOrderConfirm.paymentFailed'))
      } finally {
        this.loading = false
      }
    },
    
    handlePaymentSuccess(data) {
      this.clearCheckTimer()
      this.$emit('success', data)
      this.handleClose()
    }
  },
  
  beforeDestroy() {
    this.clearCheckTimer()
  }
}
</script>

<style lang="scss" scoped>
.pre-pay-fee-order-confirm-dialog {
  .confirm-content {
    padding: 20px 0;
    
    .qr-code-container {
      text-align: center;
      margin: 20px 0;
      
      .qr-code-content {
        .qr-code-image {
          max-width: 240px;
          height: auto;
          margin-bottom: 10px;
          border: 1px solid #ddd;
          padding: 10px;
          background: white;
        }
        
        .qr-code-tip {
          color: #666;
          font-size: 14px;
        }
      }
      
      .qr-code-loading {
        margin: 20px 0;
      }
    }
    
    .auth-code-input {
      margin: 20px 0;
    }
  }
  
  .dialog-footer {
    text-align: right;
  }
}

::v-deep .el-form-item {
  margin-bottom: 15px;
  
  .el-form-item__label {
    font-weight: bold;
  }
}
</style>