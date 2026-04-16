<template>
  <div class="his-fee-container">
    <el-table :data="feeDetails" border>
      <el-table-column prop="feeName" :label="$t('adminRoomFee.feeName')" align="center" />
      <el-table-column prop="payerObjName" :label="$t('adminRoomFee.payerObj')" align="center" />
      <el-table-column prop="cycles" :label="$t('adminRoomFee.cycles')" align="center" />
      <el-table-column :label="$t('adminRoomFee.amount')" align="center">
        <template slot-scope="scope">
          {{ scope.row.receivableAmount }}/{{ scope.row.receivedAmount }}<br>
          <div v-if="scope.row.acctAmount > 0">
            {{ $t('adminRoomFee.accountDeduction') }}: {{ scope.row.acctAmount }}<br>
          </div>
          <div v-for="(item, index) in scope.row.payFeeDetailDiscountDtoList" :key="index">
            {{ item.discountName }}: {{ Math.abs(item.discountPrice) }}<br>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="primeRateName" :label="$t('adminRoomFee.paymentMethod')" align="center" />
      <el-table-column :label="$t('adminRoomFee.paymentPeriod')" align="center">
        <template slot-scope="scope">
          {{ dfTime(scope.row.startTime) }}~<br>
          {{ dfTime(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('adminRoomFee.paymentTime')" align="center" />
      <el-table-column prop="cashierName" :label="$t('adminRoomFee.cashier')" align="center" />
      <el-table-column prop="stateName" :label="$t('adminRoomFee.status')" align="center" />
      <el-table-column prop="remark" :label="$t('adminRoomFee.remark')" align="center" />
    </el-table>

    <el-pagination class="margin-top" :current-page="page.current" :page-size="page.size" :total="page.total"
      layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { queryAdminFeeDetail } from '@/api/fee/adminRoomFeeApi'
import { dateFormat } from '@/utils/dateUtil'


export default {
  name: 'ARoomDetailHisFee',

  data() {
    return {
      feeDetails: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.ownerId = params.ownerId
      this.feeId = params.feeId
      this.loadData()
    },
    async loadData() {
      const params = {
        page: this.page.current,
        row: this.page.size,
        ownerId: this.ownerId,
        feeId: this.feeId
      }

      const res = await queryAdminFeeDetail(params)
      this.feeDetails = res.feeDetails
      this.page.total = res.total

    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    },
    dfTime(time) {
      return dateFormat(time)
    }
  }
}
</script>