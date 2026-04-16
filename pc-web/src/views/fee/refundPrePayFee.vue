<template>
  <div class="refund-pre-pay-fee-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span class="">{{ $t('refundPrePayFee.title') }}</span>
        <div class="">
          <el-button type="primary" size="small" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-row>
        <el-col :span="24">
          <div class="content-wrapper">
            <el-form label-width="120px">
              <el-row>
                <el-col :span="12">
                  <el-form-item :label="$t('refundPrePayFee.orderNo')">
                    <span class="form-value">{{ refundPrePayFeeInfo.payOrderId }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('refundPrePayFee.orderType')">
                    <span class="form-value">
                      {{ refundPrePayFeeInfo.orderType == '2002' ? $t('refundPrePayFee.paymentOrder') : $t('refundPrePayFee.prepaidOrder') }}
                    </span>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="12">
                  <el-form-item :label="$t('refundPrePayFee.payer')">
                    <span class="form-value">{{ refundPrePayFeeInfo.payerObjName }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('refundPrePayFee.owner')">
                    <span class="form-value">{{ refundPrePayFeeInfo.ownerName }}</span>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="24">
                  <el-form-item :label="$t('refundPrePayFee.feeItems')">
                    <el-table
                      :data="refundPrePayFeeInfo.refundFees"
                      border
                      style="width: 100%"
                      class="fee-table"
                    >
                      <el-table-column width="60" align="center">
                        <template slot="header">
                          <el-checkbox
                            :indeterminate="isIndeterminate"
                            v-model="checkAll"
                            @change="handleCheckAllChange"
                          />
                        </template>
                        <template slot-scope="scope">
                          <el-checkbox
                            v-model="refundPrePayFeeInfo.selectDetailIds"
                            :label="scope.row.detailId"
                            @change="handleCheckedChange"
                          />
                        </template>
                      </el-table-column>
                      <el-table-column
                        prop="feeName"
                        :label="$t('refundPrePayFee.feeName')"
                        align="center"
                      />
                      <el-table-column
                        :label="$t('refundPrePayFee.paymentPeriod')"
                        align="center"
                      >
                        <template slot-scope="scope">
                          {{ scope.row.startTime }}~{{ scope.row.endTime }}
                        </template>
                      </el-table-column>
                      <el-table-column
                        prop="receivedAmount"
                        :label="$t('refundPrePayFee.actualAmount')"
                        align="center"
                      />
                    </el-table>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="24">
                  <el-form-item
                    :label="$t('refundPrePayFee.refundReason')"
                    required
                  >
                    <el-input
                      type="textarea"
                      :rows="4"
                      v-model="refundPrePayFeeInfo.reason"
                      :placeholder="$t('refundPrePayFee.refundReasonPlaceholder')"
                    />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="24" class="action-buttons">
                  <el-button
                    type="primary"
                    @click="submitReturnPayFee"
                    class="submit-btn"
                  >
                    {{ $t('refundPrePayFee.submitRefund') }}
                  </el-button>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { getPayFeeOrders, applyRefundPrePayFee } from '@/api/fee/refundPrePayFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'RefundPrePayFeeList',
  data() {
    return {
      refundPrePayFeeInfo: {
        feeId: '',
        payOrderId: '',
        orderType: '',
        payerObjName: '',
        ownerName: '',
        communityId: '',
        selectDetailIds: [],
        refundFees: [],
        reason: ''
      },
      checkAll: false,
      isIndeterminate: false
    }
  },
  created() {
    this.initData()
  },
  watch: {
    'refundPrePayFeeInfo.selectDetailIds': {
      handler(newVal) {
        const checkedCount = newVal.length
        this.checkAll = checkedCount === this.refundPrePayFeeInfo.refundFees.length
        this.isIndeterminate = checkedCount > 0 && checkedCount < this.refundPrePayFeeInfo.refundFees.length
      },
      deep: true
    }
  },
  methods: {
    initData() {
      this.refundPrePayFeeInfo.payOrderId = this.$route.query.payOrderId
      this.listPayFeeOrders()
    },
    async listPayFeeOrders() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: getCommunityId(),
          payOrderId: this.refundPrePayFeeInfo.payOrderId
        }
        
        this.refundPrePayFeeInfo.selectDetailIds = []
        const { data } = await getPayFeeOrders(params)
        
        if (data && data.length > 0) {
          Object.assign(this.refundPrePayFeeInfo, data[0])
          this.refundPrePayFeeInfo.refundFees = data[0].payFeeOrderDetailDtos || []
          
          // 默认选中所有费用项
          this.refundPrePayFeeInfo.selectDetailIds = this.refundPrePayFeeInfo.refundFees.map(item => item.detailId)
        }
      } catch (error) {
        console.error('获取缴费订单失败:', error)
        this.$message.error(this.$t('refundPrePayFee.fetchError'))
      }
    },
    handleCheckAllChange(val) {
      if (val) {
        this.refundPrePayFeeInfo.selectDetailIds = this.refundPrePayFeeInfo.refundFees.map(item => item.detailId)
      } else {
        this.refundPrePayFeeInfo.selectDetailIds = []
      }
      this.isIndeterminate = false
    },
    handleCheckedChange() {
      // watch 会自动处理状态更新
    },
    refundPrePayFeeValidate() {
      if (!this.refundPrePayFeeInfo.reason || this.refundPrePayFeeInfo.reason.trim() === '') {
        this.$message.error(this.$t('refundPrePayFee.reasonRequired'))
        return false
      }
      
      if (this.refundPrePayFeeInfo.reason.length > 200) {
        this.$message.error(this.$t('refundPrePayFee.reasonTooLong'))
        return false
      }
      
      return true
    },
    async submitReturnPayFee() {
      if (!this.refundPrePayFeeValidate()) {
        return
      }
      
      if (this.refundPrePayFeeInfo.selectDetailIds.length === 0) {
        this.$message.error(this.$t('refundPrePayFee.selectFeeItems'))
        return
      }
      
      try {
        this.refundPrePayFeeInfo.communityId = getCommunityId()
        const result = await applyRefundPrePayFee(this.refundPrePayFeeInfo)
        
        if (result.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(result.msg || this.$t('refundPrePayFee.operationFailed'))
        }
      } catch (error) {
        console.error('提交退费申请失败:', error)
        this.$message.error(this.$t('refundPrePayFee.operationFailed'))
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.refund-pre-pay-fee-container {
  padding: 20px;
  

  
  .content-wrapper {
    padding: 0 20px;
    
    .form-value {
      color: #606266;
    }
    
    .fee-table {
      margin-top: 10px;
      
      ::v-deep .el-checkbox {
        margin-right: 0;
      }
    }
    
    .action-buttons {
      text-align: right;
      padding: 20px 0;
      
      .submit-btn {
        min-width: 120px;
      }
    }
  }
}
</style>