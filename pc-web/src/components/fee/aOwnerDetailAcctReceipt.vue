<template>
  <div class="acct-receipt-container">
    <el-table :data="feeReceipts" border>
      <el-table-column prop="acctName" :label="$t('adminRoomFee.accountName')" align="center" />
      <el-table-column prop="acctTypeName" :label="$t('adminRoomFee.accountType')" align="center" />
      <el-table-column prop="ownerName" :label="$t('adminRoomFee.owner')" align="center" />
      <el-table-column prop="receivedAmount" :label="$t('adminRoomFee.prepaidAmount')" align="center" />
      <el-table-column prop="primeRateName" :label="$t('adminRoomFee.paymentMethod')" align="center" />
      <el-table-column :label="$t('adminRoomFee.totalAmount')" align="center">
        <template slot-scope="scope">
          {{ scope.row.amount }}{{ $t('adminRoomFee.yuan') }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('adminRoomFee.paymentTime')" align="center" />
      <el-table-column prop="arId" :label="$t('adminRoomFee.receiptId')" align="center" />
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
import { listAdminAccountReceipt } from '@/api/fee/adminRoomFeeApi'

export default {
  name: 'AOwnerDetailAcctReceipt',
  data() {
    return {
      feeReceipts: [],
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
        ownerId: this.ownerId
      }
      
      listAdminAccountReceipt(params).then(res => {
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
    }
  }
}
</script>