<template>
  <el-dialog :title="$t('returnPayFee.fillRefundReason')" :visible.sync="visible" width="60%" @close="handleClose">
    <div class="return-pay-fee-container">
      <el-form ref="form" :model="returnPayFeeInfo" label-width="120px" label-position="left">
        <!-- 订单基本信息 -->
        <el-row :gutter="20" >
          <el-col :span="12">
            <el-form-item :label="$t('returnPayFee.orderId')">
              <span>{{ returnPayFeeInfo.payOrderId }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('returnPayFee.orderType')">
              <span>{{ returnPayFeeInfo.orderType == '2002' ? $t('returnPayFee.payOrder') : $t('returnPayFee.preOrder') }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" >
          <el-col :span="12">
            <el-form-item :label="$t('returnPayFee.payerObjName')">
              <span>{{ returnPayFeeInfo.payerObjName }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('returnPayFee.ownerName')">
              <span>{{ returnPayFeeInfo.ownerName }}</span>
            </el-form-item>
          </el-col>
        </el-row>

      <!-- 费用明细列表 -->
      <el-row :gutter="20" class="fee-details">
        <el-col :span="24">
          <h4>{{ $t('returnPayFee.feeDetails') }}</h4>
          <el-table :data="returnPayFeeInfo.refundFees" border style="width: 100%">
            <el-table-column type="selection" width="55" align="center">
              <template slot-scope="scope">
                <el-checkbox 
                  :value="returnPayFeeInfo.selectDetailIds.includes(scope.row.detailId)"
                  @change="selectOneRule(scope.row)"
                ></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column :label="$t('returnPayFee.feeName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.feeName }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('returnPayFee.feeTimePeriod')" align="center">
              <template slot-scope="scope">
                {{ scope.row.startTime }}~{{ scope.row.endTime }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('returnPayFee.receivedAmount')" align="center">
              <template slot-scope="scope">
                {{ scope.row.receivedAmount }}
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>

        <!-- 退费原因 -->
        <el-row :gutter="20" class="reason-section">
          <el-col :span="24">
            <el-form-item :label="$t('returnPayFee.refundReason')" prop="reason" :rules="[
              { required: true, message: $t('returnPayFee.reasonRequired'), trigger: 'blur' },
              { max: 200, message: $t('returnPayFee.reasonMaxLength'), trigger: 'blur' }
            ]">
              <el-input v-model="returnPayFeeInfo.reason" type="textarea" :rows="3"
                :placeholder="$t('returnPayFee.reasonPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitReturnPayFee" :loading="loading">
        {{ $t('returnPayFee.submitRefund') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveReturnPayFee } from '@/api/fee/refundDepositFeeApi'
import { listPayFeeOrder } from '@/api/report/reportPayFeeDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ReturnPayFee',
  data() {
    return {
      visible: false,
      loading: false,
      returnPayFeeInfo: {
        feeId: '',
        payOrderId: '',
        orderType: '',
        payerObjName: '',
        ownerName: '',
        communityId: '',
        selectDetailIds: [],
        refundFees: [],
        reason: ''
      }
    }
  },
  methods: {
    open(data) {
      this.refreshFeeDetailInfo()
      Object.assign(this.returnPayFeeInfo, data)
      this._listPayFeeOrders()
      this.visible = true
    },
    refreshFeeDetailInfo() {
      this.returnPayFeeInfo = {
        feeId: '',
        payOrderId: '',
        orderType: '',
        payerObjName: '',
        ownerName: '',
        communityId: '',
        selectDetailIds: [],
        refundFees: [],
        reason: ''
      }
    },
    async _listPayFeeOrders() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.returnPayFeeInfo.communityId || getCommunityId(),
          payOrderId: this.returnPayFeeInfo.payOrderId
        }
        this.returnPayFeeInfo.selectDetailIds = []
        
        // 调用获取费用订单详情的API
        const data = await listPayFeeOrder(params)
        if (data.data && data.data.length > 0) {
          const orderData = data.data[0]
          // 更新订单基本信息
          Object.assign(this.returnPayFeeInfo, {
            feeId: orderData.feeId || this.returnPayFeeInfo.feeId,
            orderType: orderData.orderType || this.returnPayFeeInfo.orderType,
            payerObjName: orderData.payerObjName || this.returnPayFeeInfo.payerObjName,
            ownerName: orderData.ownerName || this.returnPayFeeInfo.ownerName,
            communityId: orderData.communityId || this.returnPayFeeInfo.communityId
          })
          
          // 设置费用明细列表
          this.returnPayFeeInfo.refundFees = orderData.payFeeOrderDetailDtos || []
          
          // 默认选中所有费用项
          this.returnPayFeeInfo.refundFees.forEach(item => {
            this.returnPayFeeInfo.selectDetailIds.push(item.detailId)
          })
        } else {
          this.$message.warning(this.$t('returnPayFee.notFound'))
          this.returnPayFeeInfo.refundFees = []
        }
      } catch (error) {
        console.error('获取费用订单详情失败:', error)
        this.$message.error(this.$t('returnPayFee.fetchError'))
        this.returnPayFeeInfo.refundFees = []
      }
    },
    selectOneRule(item) {
      const index = this.returnPayFeeInfo.selectDetailIds.indexOf(item.detailId)
      if (index > -1) {
        this.returnPayFeeInfo.selectDetailIds.splice(index, 1)
      } else {
        this.returnPayFeeInfo.selectDetailIds.push(item.detailId)
      }
    },
    handleClose() {
      this.refreshFeeDetailInfo()
    },
    async submitReturnPayFee() {
      try {
        // 使用表单验证
        const valid = await this.$refs.form.validate()
        if (!valid) return
        
        const selectDetailIds = this.returnPayFeeInfo.selectDetailIds
        if (selectDetailIds.length === 0) {
          this.$message.error(this.$t('returnPayFee.selectFeeItems'))
          return
        }
        
        this.loading = true
        this.returnPayFeeInfo.communityId = this.returnPayFeeInfo.communityId || getCommunityId()
        
        await saveReturnPayFee(this.returnPayFeeInfo)
        
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
        this.refreshFeeDetailInfo()
      } catch (error) {
        console.error('提交退费申请失败:', error)
        this.$message.error(error.message || this.$t('returnPayFee.operationFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.return-pay-fee-container {
  .order-info {
    margin-bottom: 20px;
    padding: 15px;
    background-color: #f5f7fa;
    border-radius: 4px;
    
    .el-form-item {
      margin-bottom: 10px;
      
      .el-form-item__label {
        font-weight: bold;
      }
    }
  }
  
  .fee-details {
    margin-bottom: 20px;
    
    h4 {
      margin-bottom: 15px;
      color: #303133;
    }
  }
  
  .reason-section {
    .el-form-item__label {
      font-weight: bold;
    }
  }
}
</style>