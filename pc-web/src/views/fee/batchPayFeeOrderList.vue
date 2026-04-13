<template>
  <div class="batch-pay-fee-order-container">
    <el-row :gutter="20">
      <el-col :span="5">
        <el-card class="payer-list-card">
          <div class="payer-list">
            <div
              v-for="(item,index) in batchPayFeeOrderInfo.batchFees"
              :key="index"
              class="payer-item"
              :class="{'payer-item-selected':item.payerObjId == batchPayFeeOrderInfo.payerObjId}"
              @click="_swatchPayerObj(item)"
            >
              <div class="payer-name">{{item.payerObjName}}</div>
              <div class="payer-detail">业主：{{item.ownerName}}|欠费：{{item.oweAmount}}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="19">
        <el-card>
          <div class="account-info">
            <el-row>
              <el-col :span="16">
                <div class="account-items">
                  <span class="account-item">账户金额：<span class="amount">￥{{batchPayFeeOrderInfo.acctAmount}}</span></span>
                  <span class="account-item">可用积分：<span class="amount">{{batchPayFeeOrderInfo.integralQuantity}}</span></span>
                  <span class="account-item">
                    押金金额：<span class="amount">￥{{batchPayFeeOrderInfo.depositAmount}}</span>
                    <span v-if="batchPayFeeOrderInfo.depositAmount > 0">(<a href="javascript:void(0);" @click="depositPay">退押金</a>)</span>
                  </span>
                  <span class="account-item">
                    预付款：<span class="amount">￥{{batchPayFeeOrderInfo.ruleAmount}}</span>
                    <span>(<a href="javascript:void(0);" @click="_openBatchPrePayDialog()">预付</a>)</span>
                  </span>
                </div>
              </el-col>
              <el-col :span="8" class="text-right">
                <el-button-group>
                  <el-button size="small" @click="_allSelectOrNot('Y')">{{ $t('batchPayFeeOrder.allSelect') }}</el-button>
                  <el-button size="small" @click="_allSelectOrNot('N')">{{ $t('batchPayFeeOrder.allCancel') }}</el-button>
                  <el-button size="small" @click="_currentSelectOrNot('Y')">{{ $t('batchPayFeeOrder.currentSelect') }}</el-button>
                  <el-button size="small" @click="_currentSelectOrNot('N')">{{ $t('batchPayFeeOrder.currentCancel') }}</el-button>
                  <el-button size="small" type="success" @click="$router.go(-1)">{{ $t('batchPayFeeOrder.back') }}</el-button>
                </el-button-group>
              </el-col>
            </el-row>
          </div>

          <div class="fee-list-container">
            <div class="fee-list">
              <div v-for="(feeConfig,index) in batchPayFeeOrderInfo.fees" :key="index" class="fee-category">
                <div class="fee-header" @click="switchSimplifyFeeFlag(feeConfig)">
                  <div class="fee-header-left">
                    <input
                      type="checkbox"
                      class="i-checks checkItem"
                      :checked="feeConfig.selectedFeeIds.length >= feeConfig.childs.length"
                      @change="selectAllFee(feeConfig)"
                      @click.stop
                    />
                    <div class="fee-info text-left">
                      <div class="fee-name">{{feeConfig.feeName}}</div>
                      <div class="fee-period">{{feeConfig.startTime}}~{{feeConfig.endTime}}</div>
                    </div>
                  </div>
                  <div class="fee-header-right">
                    <span class="owe-amount">欠费总额：￥{{feeConfig.oweAmount}}</span>
                    <i :class="feeConfig.showFlag == 'Y' ? 'el-icon-arrow-down' : 'el-icon-arrow-right'"></i>
                  </div>
                </div>
                
                <div v-if="feeConfig.showFlag == 'Y'" class="fee-details">
                  <div v-for="(cFee,cIndex) in feeConfig.childs" :key="cIndex" class="fee-detail-item">
                    <div class="fee-detail-content">
                      <div class="fee-detail-left">
                        <input
                          type="checkbox"
                          class="i-checks checkItem"
                          :value="cFee.feeId"
                          v-model="feeConfig.selectedFeeIds"
                          @change="selectOneFee(feeConfig)"
                        />
                        <div class="fee-period-detail">
                          {{dateFormat(cFee.startTime)}}~{{dateFormat(cFee.endTime)}}
                        </div>
                      </div>
                      <div class="fee-detail-middle">
                        <span class="fee-remark">{{cFee.remark}}</span>
                      </div>
                      <div class="fee-detail-right">
                        <span class="receivable-amount">应收金额：￥{{cFee.oweAmount}}</span>
                      </div>
                    </div>
                  </div>

                  <div v-for="(discount,dIndex) in feeConfig.discounts" :key="dIndex" class="discount-item">
                    <div class="fee-detail-content">
                      <div class="fee-detail-left">
                        <input
                          type="checkbox"
                          class="i-checks checkItem"
                          :value="discount.discountId"
                          v-model="feeConfig.selectedDiscountIds"
                          @change="selectOneDiscount(feeConfig)"
                        />
                        <div class="discount-name">{{discount.discountName}}</div>
                      </div>
                      <div class="fee-detail-middle">
                        <span class="discount-rule">{{discount.ruleName}}</span>
                      </div>
                      <div class="fee-detail-right">
                        <span class="discount-amount">金额：￥{{discount.discountPrice}}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="payment-summary">
            <div class="summary-items">
              <div class="summary-item">
                优惠金额：<span class="summary-amount">{{batchPayFeeOrderInfo.discount}}元</span>
              </div>
              <div class="summary-item">
                缴费金额：<span class="summary-amount fee-amount">{{batchPayFeeOrderInfo.feePrices}}元</span>
              </div>
            </div>
            <div class="payment-buttons">
              <el-button type="success" size="large" @click="_openPayFee('qrCode')">
                {{ $t('batchPayFeeOrder.scanPay') }}
              </el-button>
              <el-button type="primary" size="large" @click="_openPayFee('common')">
                {{ $t('batchPayFeeOrder.submitPay') }}
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 子组件 -->
    <batch-pay-confirm ref="batchPayConfirm" @paymentSuccess="_onPaymentSuccess" />
    <batch-pre-pay-fee-dialog ref="batchPrePayFeeDialog" @notifyPreFeeOrder="_notifyPreFeeOrder" />
    <batch-pay-fee-result ref="batchPayFeeResult" @closed="handleClose"/>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listOwnerFee, queryFeeDeposit, computeFeeDiscount } from '@/api/fee/batchPayFeeOrderApi'
import BatchPayConfirm from '@/components/fee/batchPayConfirm'
import BatchPrePayFeeDialog from '@/components/fee/batchPrePayFeeDialog'
import BatchPayFeeResult from '@/components/fee/batchPayFeeResult'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'BatchPayFeeOrderList',
  components: {
    BatchPayConfirm,
    BatchPrePayFeeDialog,
    BatchPayFeeResult
  },
  data() {
    return {
      batchPayFeeOrderInfo: {
        batchFees: [],
        payerObjId: '',
        fees: [],
        feePrices: 0.00,
        discount: 0.0,
        communityId: '',
        ownerId: '',
        payerObjType: '',
        remark: '',
        acctAmount: 0.0,
        acctId: '',
        depositAmount: 0.0,
        integralQuantity: 0,
        rules: [],
        ruleAmount: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.batchPayFeeOrderInfo.communityId = this.communityId
    
    const ownerId = this.$route.query.ownerId
    const payerObjType = this.$route.query.payerObjType
    const payerObjId = this.$route.query.payerObjId
    
    
    this.batchPayFeeOrderInfo.ownerId = ownerId
    this.batchPayFeeOrderInfo.payerObjType = payerObjType
    this.batchPayFeeOrderInfo.payerObjId = payerObjId
    
    this._loadBatchFees()
  },
  methods: {
    async _loadBatchFees() {
      try {
        const params = {
          page: 1,
          row: 500,
          communityId: this.communityId,
          ownerId: this.batchPayFeeOrderInfo.ownerId,
          payerObjType: this.batchPayFeeOrderInfo.payerObjType,
          payerObjId: this.batchPayFeeOrderInfo.payerObjId,
          state: '2008001'
        }
        
        const response = await listOwnerFee(params)
        const fees = response.data
        
        if (!fees || fees.length < 1) {
          return
        }
        
        const processedFees = this._initSelectAll(fees)
        this.batchPayFeeOrderInfo.batchFees = processedFees
        
        if (processedFees.length > 0) {
          this._swatchPayerObj(processedFees[0])
        }
        
        this._doComputeTotalFee()
      } catch (error) {
        console.error('加载费用失败:', error)
        this.$message.error('加载费用失败')
      }
    },
    
    _initSelectAll(fees) {
      return fees.map(fee => {
        fee.childs = fee.childs.map(feeConfig => {
          feeConfig.selectedFeeIds = feeConfig.childs.map(child => child.feeId)
          feeConfig.childs = feeConfig.childs.map(child => {
            child.receivedAmount = child.oweAmount
            return child
          })
          this._computeDiscount(feeConfig)
          return feeConfig
        })
        return fee
      })
    },
    
    selectAllFee(feeConfig) {
      if (feeConfig.selectedFeeIds.length == feeConfig.childs.length) {
        feeConfig.selectedFeeIds = []
      } else {
        feeConfig.selectedFeeIds = feeConfig.childs.map(fee => fee.feeId)
      }
      this._computeDiscount(feeConfig)
      this._doComputeTotalFee()
    },
    
    selectOneFee(feeConfig) {
      this._computeDiscount(feeConfig)
      this._doComputeTotalFee()
    },
    
    selectOneDiscount(feeConfig) {
      console.log(feeConfig)
      this._doComputeTotalFee()
    },
    
    _swatchPayerObj(payerObj) {
      this.batchPayFeeOrderInfo.payerObjId = payerObj.payerObjId
      this.batchPayFeeOrderInfo.acctAmount = payerObj.acctAmount
      this.batchPayFeeOrderInfo.integralQuantity = payerObj.integralQuantity
      this.batchPayFeeOrderInfo.fees = payerObj.childs
      this._queryFeeDeposit(payerObj)
    },
    
    switchSimplifyFeeFlag(fee) {
      fee.showFlag = fee.showFlag == 'Y' ? 'N' : 'Y'
      this.$forceUpdate()
    },
    
    _openPayFee(payType) {
      if (this.batchPayFeeOrderInfo.feePrices <= 0) {
        this.$message.warning('未选择费用')
        return
      }
      
      const payerObjs = this.batchPayFeeOrderInfo.batchFees.map(fee => ({
        payerObjId: fee.payerObjId,
        payerObjName: fee.payerObjName,
        payerObjType: fee.payerObjType
      }))
      
      this.$refs.batchPayConfirm.open({
        fees: this.batchPayFeeOrderInfo.batchFees,
        feePrices: this.batchPayFeeOrderInfo.feePrices,
        payType: payType,
        discount: this.batchPayFeeOrderInfo.discount,
        ownerId: this.batchPayFeeOrderInfo.ownerId,
        rules: this.batchPayFeeOrderInfo.rules,
        ruleAmount: this.batchPayFeeOrderInfo.ruleAmount,
        payerObjs: payerObjs
      })
    },
    
    _doComputeTotalFee() {
      let totalFee = 0
      let discountFee = 0
      
      this.batchPayFeeOrderInfo.batchFees.forEach(obj => {
        obj.childs.forEach(feeConfig => {
          feeConfig.selectedFeeIds.forEach(feeId => {
            feeConfig.childs.forEach(fee => {
              if (fee.feeId == feeId) {
                totalFee += parseFloat(fee.receivedAmount)
              }
            })
          })
          
          if (!feeConfig.selectedDiscountIds || feeConfig.selectedDiscountIds.length == 0) {
            return
          }
          
          feeConfig.selectedDiscountIds.forEach(discountId => {
            feeConfig.discounts.forEach(discount => {
              if (discount.discountId == discountId) {
                totalFee += parseFloat(discount.discountPrice)
                discountFee += parseFloat(discount.discountPrice)
              }
            })
          })
        })
      })
      
      const ruleAmount = parseFloat(this.batchPayFeeOrderInfo.ruleAmount) || 0
      totalFee += ruleAmount
      
      this.batchPayFeeOrderInfo.feePrices = totalFee.toFixed(2)
      this.batchPayFeeOrderInfo.discount = discountFee.toFixed(2)
      this.$forceUpdate()
    },
    
    async _computeDiscount(feeConfig) {
      if (feeConfig.selectedFeeIds.length == 0) {
        feeConfig.discounts = []
        feeConfig.selectedDiscountIds = []
        return
      }
      
      try {
        const data = {
          communityId: this.communityId,
          feeIds: feeConfig.selectedFeeIds
        }
        
        const response = await computeFeeDiscount(data)
        
        if (response.data && response.data.length > 0) {
          feeConfig.discounts = response.data
          feeConfig.selectedDiscountIds = feeConfig.discounts.map(discount => discount.discountId)
          this._doComputeTotalFee()
        }
      } catch (error) {
        console.error('计算折扣失败:', error)
      }
    },
    
    depositPay() {
      this.$router.push({
        path: '/fee/refundDepositFee',
        query: {
          roomId: this.batchPayFeeOrderInfo.payObjId,
          ownerId: this.batchPayFeeOrderInfo.ownerId
        }
      })
    },
    
    async _queryFeeDeposit(payerObj) {
      try {
        const params = {
          communityId: this.communityId,
          payerObjId: payerObj.payerObjId,
          ownerId: payerObj.ownerId,
          page: 1,
          row: 100,
          state: '1400'
        }
        
        const response = await queryFeeDeposit(params)
        let deposit = 0.0
        
        if (response.data && response.data.length > 0) {
          response.data.forEach(data => {
            deposit += parseFloat(data.receivedAmount)
          })
        }
        
        this.batchPayFeeOrderInfo.depositAmount = deposit.toFixed(2)
      } catch (error) {
        console.error('查询押金失败:', error)
      }
    },
    
    _allSelectOrNot(selectType) {
      this.batchPayFeeOrderInfo.batchFees.forEach(fee => {
        fee.childs.forEach(feeConfig => {
          feeConfig.selectedFeeIds = []
          this._computeDiscount(feeConfig)
        })
      })
      
      if (selectType != 'Y') {
        this._doComputeTotalFee()
        return
      }
      
      this.batchPayFeeOrderInfo.batchFees.forEach(fee => {
        fee.childs.forEach(feeConfig => {
          feeConfig.selectedFeeIds = feeConfig.childs.map(child => child.feeId)
          feeConfig.childs.forEach(child => {
            child.receivedAmount = child.oweAmount
          })
          this._computeDiscount(feeConfig)
        })
      })
      
      this._doComputeTotalFee()
    },
    
    _currentSelectOrNot(selectType) {
      const payerObjId = this.batchPayFeeOrderInfo.payerObjId
      const roomConfig = this.batchPayFeeOrderInfo.batchFees.find(fee => fee.payerObjId == payerObjId)
      
      if (!roomConfig) return
      
      roomConfig.childs.forEach(feeConfig => {
        feeConfig.selectedFeeIds = []
        this._computeDiscount(feeConfig)
      })
      
      if (selectType != 'Y') {
        this._doComputeTotalFee()
        return
      }
      
      roomConfig.childs.forEach(feeConfig => {
        feeConfig.selectedFeeIds = feeConfig.childs.map(child => child.feeId)
        feeConfig.childs.forEach(child => {
          child.receivedAmount = child.oweAmount
        })
        this._computeDiscount(feeConfig)
      })
      
      this._doComputeTotalFee()
    },
    
    _openBatchPrePayDialog() {
      const payerObjs = this.batchPayFeeOrderInfo.batchFees.map(fee => ({
        payerObjId: fee.payerObjId,
        payerObjName: fee.payerObjName,
        payerObjType: fee.payerObjType
      }))
      
      this.$refs.batchPrePayFeeDialog.open({
        payerObjs: payerObjs
      })
    },
    
    _notifyPreFeeOrder(param) {
      this.batchPayFeeOrderInfo.rules = param.rules
      this.batchPayFeeOrderInfo.ruleAmount = param.ruleAmount
      this._doComputeTotalFee()
    },
    _onPaymentSuccess(payload) {
      try {
        const { receiptIds, printUrl } = payload || {}
        this._loadBatchFees()
        this.$refs.batchPayFeeResult && this.$refs.batchPayFeeResult.open({ receiptIds, printUrl })
      } catch (e) {
        // no-op
      }
    },
    handleClose() {
      this.$router.go(-1)
    },
    dateFormat,
  }
}
</script>

<style lang="scss" scoped>
.batch-pay-fee-order-container {
  padding: 20px;
  
  .payer-list-card {
    height: 100%;
    
    .payer-list {
      max-height: 700px;
      overflow-y: auto;
    }
    
    .payer-item {
      padding: 15px;
      margin-bottom: 10px;
      border: 1px solid #e6e6e6;
      border-radius: 4px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        border-color: #409eff;
        background-color: #f5f7fa;
      }
      
      &.payer-item-selected {
        border-color: #409eff;
        background-color: #ecf5ff;
      }
      
      .payer-name {
        font-size: 18px;
        font-weight: bold;
        margin-bottom: 5px;
      }
      
      .payer-detail {
        font-size: 14px;
        color: #666;
      }
    }
  }
  
  .account-info {
    padding: 15px 20px;
    background: #f0f7ff;
    border-bottom: 1px solid #d0e3ff;
    
    .account-items {
      display: flex;
      flex-wrap: wrap;
      gap: 20px;
      align-items: center;
      
      .account-item {
        font-size: 14px;
        color: #666;
        
        .amount {
          color: #4caf50;
          font-weight: bold;
        }
      }
    }
  }
  
  .fee-list-container {
    max-height: 650px;
    overflow-y: auto;
    margin-top: 20px;
    
    .fee-category {
      margin-bottom: 10px;
      
      .fee-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15px;
        background: white;
        cursor: pointer;
        border: 1px solid #e6e6e6;
        border-radius: 4px;
        
        .fee-header-left {
          display: flex;
          align-items: center;
          
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
          
          .fee-info {
            margin-left: 10px;
            
            .fee-name {
              font-weight: 600;
              font-size: 16px;
              margin-bottom: 5px;
            }
            
            .fee-period {
              color: #666;
            }
          }
        }
        
        .fee-header-right {
          display: flex;
          align-items: center;
          
          .owe-amount {
            font-weight: 600;
            font-size: 16px;
            color: #f44336;
            margin-right: 10px;
          }
        }
      }
      
      .fee-details {
        margin-top: 5px;
        
        .fee-detail-item,
        .discount-item {
          margin-bottom: 5px;
        }
        
        .fee-detail-content {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 15px;
          background: #f0f7ff;
          border-bottom: 1px solid #d0e3ff;
          
          .fee-detail-left {
            display: flex;
            align-items: center;
              
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
            
            .fee-period-detail,
            .discount-name {
              font-weight: 600;
              font-size: 16px;
              margin-left: 10px;
            }
          }
          
          .fee-detail-middle {
            flex-grow: 1;
            text-align: center;
            
            .fee-remark,
            .discount-rule {
              font-size: 12px;
            }
          }
          
          .fee-detail-right {
            .receivable-amount,
            .discount-amount {
              font-weight: 600;
              font-size: 16px;
              color: #f44336;
            }
          }
        }
      }
    }
  }
  
  .payment-summary {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
    padding: 20px;
    background: #f9f9f9;
    border-radius: 4px;
    
    .summary-items {
      display: flex;
      gap: 30px;
      
      .summary-item {
        font-size: 16px;
        
        .summary-amount {
          font-size: 30px;
          
          &.fee-amount {
            color: red;
          }
        }
      }
    }
    
    .payment-buttons {
      display: flex;
      gap: 10px;
    }
  }
  
  .text-right {
    text-align: right;
  }
}
</style>