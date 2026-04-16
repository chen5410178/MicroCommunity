<template>
  <div>
    <el-card v-if="fees.length > 0" class="margin-bottom-sm">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('payFeeDeposit.depositInfo') }}</span>
        <el-button type="primary" size="small" style="float: right;" @click="queryPayFeeDeposit">
          <i class="el-icon-refresh"></i>
          {{ $t('payFeeDeposit.refresh') }}
        </el-button>
      </div>

      <el-table :data="fees" border style="width: 100%">
        <el-table-column prop="payerObjName" align="center" :label="$t('payFeeDeposit.payerObj')" />
        <el-table-column prop="feeName" align="center" :label="$t('payFeeDeposit.feeName')" />
        <el-table-column align="center" :label="$t('payFeeDeposit.timeRange')">
          <template slot-scope="scope">
            {{ scope.row.startTime }}~{{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="receivedAmount" align="center" :label="$t('payFeeDeposit.amount')" />
        <el-table-column prop="createTime" align="center" :label="$t('payFeeDeposit.paymentTime')" />
        <el-table-column align="center" :label="$t('payFeeDeposit.operation')" width="120">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="openRefundDeposit(scope.row)">
              {{ $t('payFeeDeposit.refund') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <refund-deposit-fee ref="refundDepositFee" @success="listFeeDeposit"></refund-deposit-fee>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import RefundDepositFee from './refundDepositFee'
import { queryFeeDeposit } from '@/api/fee/payFeeOrderApi'

export default {
  name: 'PayFeeDeposit',
  components: {
    RefundDepositFee
  },
  data() {
    return {
      dialogVisible: false,
      fees: [],
      payerObjId: '',
      payerObjType: '',
      ownerId: '',
      communityId: ''
    }
  },
  methods: {
    open(params) {
      this.payerObjId = params.payerObjId
      this.payerObjType = params.payerObjType
      this.ownerId = params.ownerId || ''
      this.listFeeDeposit()
    },
    close() {
      this.dialogVisible = false
    },
    async listFeeDeposit() {
      try {
        this.communityId = await getCommunityId()
        const params = {
          page: 1,
          row: 20,
          payerObjId: this.payerObjId,
          ownerId: this.ownerId,
          communityId: this.communityId,
          state: '1400'
        }

        const response = await queryFeeDeposit(params)
        if (response.code === 0) {
          this.fees = response.data || []
        }
      } catch (error) {
        console.error('查询押金信息失败:', error)
      }
    },
    queryPayFeeDeposit() {
      this.listFeeDeposit()
    },
    openRefundDeposit(fee) {
      this.$refs.refundDepositFee.open(fee)
    },
    handleClose() {
      this.fees = []
      this.payerObjId = ''
      this.payerObjType = ''
      this.ownerId = ''
    }
  }
}
</script>

<style scoped>
.el-table {
  margin-top: 20px;
}
</style>