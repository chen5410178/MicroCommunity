<template>
  <div class="receipt-container">
    <el-table :data="feeReceipts" border>
      <el-table-column prop="objName" :label="$t('adminRoomFee.feeType')" align="center" />
      <el-table-column prop="payObjName" :label="$t('adminRoomFee.owner')" align="center" />
      <el-table-column prop="feeName" :label="$t('adminRoomFee.feeName')" align="center" />
      <el-table-column :label="$t('adminRoomFee.paymentPeriod')" align="center">
        <template slot-scope="scope">
          {{ dsTime(scope.row.startTime) }}~<br>
          {{ dsTime(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('adminRoomFee.totalAmount')" align="center">
        <template slot-scope="scope">
          {{ scope.row.amount }}{{ $t('adminRoomFee.yuan') }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('adminRoomFee.paymentTime')" align="center" />
      <el-table-column prop="receiptId" :label="$t('adminRoomFee.receiptId')" align="center" />
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
import { queryAdminFeeReceipt } from '@/api/fee/adminRoomFeeApi'
import {dateFormat} from '@/utils/dateUtil'

export default {
  name: 'ARoomDetailReceipt',
  data() {
    return {
      feeReceipts: [],
      ownerId: '',
      feeId: '',
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
      this.page.current = 1
      this.loadData()
    },
    loadData() {
      const params = {
        page: this.page.current,
        row: this.page.size,
        payObjId: this.ownerId,
        feeId: this.feeId
      }
      
      queryAdminFeeReceipt(params).then(res => {
        this.feeReceipts = res.data
        this.page.total = res.total
      })
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    },
    dsTime(time) {
      return dateFormat(time, 'yyyy-MM-dd')
    }
  }
}
</script>