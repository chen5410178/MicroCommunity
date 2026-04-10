<template>
  <div class="refund-deposit-container">
    <el-table :data="fees" border>
      <el-table-column prop="payerObjName" :label="$t('adminRoomFee.payerObj')" align="center" />
      <el-table-column prop="feeName" :label="$t('adminRoomFee.feeName')" align="center" />
      <el-table-column :label="$t('adminRoomFee.paymentPeriod')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime }}~{{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column prop="receivedAmount" :label="$t('adminRoomFee.amount')" align="center" />
      <el-table-column prop="createTime" :label="$t('adminRoomFee.paymentTime')" align="center" />
      <el-table-column prop="stateName" :label="$t('adminRoomFee.status')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center" width="120">
        <template slot-scope="scope">
          <el-button size="mini" @click="toRefund(scope.row)">
            {{ $t('adminRoomFee.refund') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page="page.current"
      :page-size="page.size"
      :total="page.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { queryAdminFeeDeposit } from '@/api/fee/adminRoomFeeApi'

export default {
  name: 'ARefundDeposit',
  props: {
    roomId: {
      type: String,
      default: ''
    },
    ownerId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      fees: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open() {
      this.page.current = 1
      this.loadData()
    },
    /**
     * Load the data for the table.
     * @param {Object} params The paramaters to be passed to the API.
     * @param {Number} params.page The current page number.
     * @param {Number} params.row The size of page.
     * @param {String} params.payerObjId The id of the payee object.
     * @param {String} params.ownerId The id of the owner.
     * @param {String} params.state The state of the fee. '1400' for the refund of deposit.
     */
    loadData() {
      const params = {
        page: this.page.current,
        row: this.page.size,
        payerObjId: this.roomId,
        ownerId: this.ownerId,
        state: '1400'
      }
      
      queryAdminFeeDeposit(params).then(res => {
        if (res.code === 0) {
          this.fees = res.data
          this.page.total = res.total
        }
      })
    },
    toRefund(fee) {
      console.log(fee)
      this.$router.push(`/pages/fee/refundDepositFee?roomId=${this.roomId}`)
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    }
  }
}
</script>