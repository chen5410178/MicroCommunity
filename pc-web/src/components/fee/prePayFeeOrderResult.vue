<template>
  <el-dialog
    :title="$t('prePayFeeOrderResult.title')"
    :visible.sync="visible"
    width="700px"
    :before-close="handleClose"
    class="pre-pay-fee-order-result-dialog"
  >
    <div class="result-content">
      <el-card v-if="receiptData" class="receipt-card">
        <div slot="header" class="receipt-header">
          <h3>{{ $t('prePayFeeOrderResult.receipt') }}</h3>
          <el-button type="primary" size="small" @click="handlePrint">
            <i class="el-icon-printer"></i>
            {{ $t('prePayFeeOrderResult.print') }}
          </el-button>
        </div>
        
        <div class="receipt-body">
          <!-- 收据头部信息 -->
          <div class="receipt-top">
            <div class="receipt-title">{{ $t('prePayFeeOrderResult.receiptTitle') }}</div>
            <div class="receipt-no">
              <span>{{ $t('prePayFeeOrderResult.receiptNo') }}：</span>
              <strong>{{ receiptData.receiptNo }}</strong>
            </div>
          </div>
          
          <!-- 缴费信息 -->
          <el-row class="receipt-info">
            <el-col :span="12">
              <div class="info-item">
                <label>{{ $t('prePayFeeOrderResult.payerObject') }}：</label>
                <span>{{ receiptData.payerObjName }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <label>{{ $t('prePayFeeOrderResult.ownerName') }}：</label>
                <span>{{ receiptData.ownerName }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <label>{{ $t('prePayFeeOrderResult.paymentTime') }}：</label>
                <span>{{ receiptData.createTime }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <label>{{ $t('prePayFeeOrderResult.paymentMethod') }}：</label>
                <span>{{ getPaymentMethodName(receiptData.primeRate) }}</span>
              </div>
            </el-col>
          </el-row>
          
          <!-- 费用明细表格 -->
          <el-table 
            :data="receiptData.feeDetails" 
            border 
            class="fee-details-table"
            :show-header="true"
          >
            <el-table-column 
              :label="$t('prePayFeeOrderResult.feeName')" 
              align="center"
              prop="feeName"
            />
            <el-table-column 
              :label="$t('prePayFeeOrderResult.amount')" 
              align="center"
              width="120"
            >
              <template slot-scope="scope">
                ￥{{ scope.row.amount }}
              </template>
            </el-table-column>
            <el-table-column 
              :label="$t('prePayFeeOrderResult.period')" 
              align="center"
              prop="period"
            />
          </el-table>
          
          <!-- 总计金额 -->
          <div class="total-amount">
            <div class="total-label">{{ $t('prePayFeeOrderResult.totalAmount') }}：</div>
            <div class="total-value">￥{{ receiptData.totalAmount }}</div>
          </div>
          
          <!-- 备注信息 -->
          <div v-if="receiptData.remark" class="remark-section">
            <label>{{ $t('prePayFeeOrderResult.remark') }}：</label>
            <span>{{ receiptData.remark }}</span>
          </div>
          
          <!-- 收据底部信息 -->
          <div class="receipt-bottom">
            <div class="operator-info">
              <span>{{ $t('prePayFeeOrderResult.operator') }}：{{ receiptData.operator }}</span>
            </div>
            <div class="receipt-footer">
              <p>{{ $t('prePayFeeOrderResult.receiptFooter') }}</p>
            </div>
          </div>
        </div>
      </el-card>
      
      <!-- 支付成功提示 -->
      <div v-else class="success-message">
        <el-result icon="success" :title="$t('prePayFeeOrderResult.paymentSuccess')">
          <template slot="extra">
            <el-button type="primary" @click="handleClose">
              {{ $t('prePayFeeOrderResult.backToList') }}
            </el-button>
            <el-button @click="handleViewDetails">
              {{ $t('prePayFeeOrderResult.viewDetails') }}
            </el-button>
          </template>
        </el-result>
      </div>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('prePayFeeOrderResult.close') }}</el-button>
      <el-button v-if="receiptData" type="primary" @click="handleDownload">
        {{ $t('prePayFeeOrderResult.download') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getReceiptInfo } from '@/api/fee/prePayFeeOrderApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'PrePayFeeOrderResult',
  data() {
    return {
      visible: false,
      receiptData: null,
      paymentMethods: []
    }
  },
  methods: {
    async open(paymentData) {
      this.visible = true
      await this.loadPaymentMethods()
      
      if (paymentData) {
        this.loadReceiptData(paymentData)
      }
    },
    
    handleClose() {
      this.visible = false
      this.receiptData = null
      this.$emit('closed')
    },
    
    async loadPaymentMethods() {
      try {
        this.paymentMethods = await getDict('pay_fee_detail', 'prime_rate')
      } catch (error) {
        console.error('获取支付方式字典失败:', error)
      }
    },
    
    getPaymentMethodName(primeRate) {
      const method = this.paymentMethods.find(item => item.statusCd === primeRate)
      return method ? method.name : primeRate
    },
    
    async loadReceiptData(paymentData) {
      try {
        const response = await getReceiptInfo({
          feeId: paymentData.feeId,
          communityId: paymentData.communityId
        })
        
        if (response.code === 0) {
          this.receiptData = response.data
        } else {
          this.$message.error(response.msg || this.$t('prePayFeeOrderResult.loadReceiptFailed'))
        }
      } catch (error) {
        this.$message.error(this.$t('prePayFeeOrderResult.requestFailed'))
      }
    },
    
    handlePrint() {
      window.print()
    },
    
    handleDownload() {
      // 实现下载收据功能
      this.$message.info(this.$t('prePayFeeOrderResult.downloadFeature'))
    },
    
    handleViewDetails() {
      // 跳转到详情页面
      this.$router.push('/fee/pre-pay-fee-order-details')
      this.handleClose()
    }
  }
}
</script>

<style lang="scss" scoped>
.pre-pay-fee-order-result-dialog {
  .result-content {
    .receipt-card {
      .receipt-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        h3 {
          margin: 0;
          color: #333;
        }
      }
      
      .receipt-body {
        .receipt-top {
          text-align: center;
          margin-bottom: 20px;
          padding-bottom: 15px;
          border-bottom: 2px solid #333;
          
          .receipt-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 10px;
          }
          
          .receipt-no {
            font-size: 14px;
            color: #666;
          }
        }
        
        .receipt-info {
          margin-bottom: 20px;
          
          .info-item {
            margin-bottom: 10px;
            
            label {
              font-weight: bold;
              color: #333;
            }
            
            span {
              color: #666;
            }
          }
        }
        
        .fee-details-table {
          margin: 20px 0;
          
          ::v-deep .el-table__header {
            background-color: #f5f7fa;
          }
        }
        
        .total-amount {
          display: flex;
          justify-content: flex-end;
          align-items: center;
          margin: 20px 0;
          padding: 15px;
          background-color: #f9f9f9;
          border-radius: 4px;
          
          .total-label {
            font-size: 16px;
            font-weight: bold;
            margin-right: 10px;
          }
          
          .total-value {
            font-size: 20px;
            color: red;
            font-weight: bold;
          }
        }
        
        .remark-section {
          margin: 15px 0;
          padding: 10px;
          background-color: #f5f7fa;
          border-radius: 4px;
          
          label {
            font-weight: bold;
          }
        }
        
        .receipt-bottom {
          margin-top: 30px;
          padding-top: 15px;
          border-top: 1px dashed #ddd;
          
          .operator-info {
            text-align: right;
            margin-bottom: 20px;
            font-size: 14px;
            color: #666;
          }
          
          .receipt-footer {
            text-align: center;
            font-size: 12px;
            color: #999;
            border-top: 1px solid #ddd;
            padding-top: 10px;
          }
        }
      }
    }
    
    .success-message {
      padding: 40px 0;
    }
  }
  
  .dialog-footer {
    text-align: right;
  }
}

@media print {
  .pre-pay-fee-order-result-dialog {
    ::v-deep .el-dialog {
      width: 100% !important;
      margin: 0 !important;
    }
    
    ::v-deep .el-dialog__header,
    ::v-deep .el-dialog__footer {
      display: none;
    }
    
    .receipt-card {
      box-shadow: none;
      border: none;
    }
  }
}
</style>