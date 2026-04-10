<template>
  <div class="invoice-apply-detail-fee">
    <el-table :data="fees" border style="width: 100%">
      <el-table-column prop="itemTypeName" :label="$t('invoiceApplyDetailFee.type')" align="center"></el-table-column>
      <el-table-column prop="itemName" :label="$t('invoiceApplyDetailFee.name')" align="center"></el-table-column>
      <el-table-column prop="itemAmount" :label="$t('invoiceApplyDetailFee.amount')" align="center"></el-table-column>
      <el-table-column prop="payTime" :label="$t('invoiceApplyDetailFee.payTime')" align="center"></el-table-column>
      <el-table-column prop="remark" :label="$t('invoiceApplyDetailFee.remark')" align="center"></el-table-column>
      <el-table-column :label="$t('invoiceApplyDetailFee.paymentId')" align="center">
        <template slot-scope="scope">
          {{ scope.row.itemObjId }}
          <!-- （<a href="javascript:void(0)" v-if="scope.row.itemType === '2002'" @click="viewFeeDetail(scope.row)">{{ $t('common.view') }}</a>
          <a href="javascript:void(0)" v-else @click="viewAcctDetail(scope.row)">{{ $t('common.view') }}</a>） -->
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page.current"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="page.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.total">
    </el-pagination>
  </div>
</template>

<script>
import { getInvoiceApplyItemList } from '@/api/fee/invoiceApplyDetailApi'

export default {
  name: 'InvoiceApplyDetailFee',
  props: {
    applyId: {
      type: String,
      required: true
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
  watch: {
    applyId: {
      immediate: true,
      handler() {
        if (this.applyId) {
          this.loadData()
        }
      }
    }
  },
  methods: {
    async loadData() {
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          applyId: this.applyId
        }
        const res = await getInvoiceApplyItemList(params)
        if (res.code === 0) {
          this.fees = res.data.map(item => ({
            ...item,
            itemTypeName: item.itemType === '2002' ? this.$t('invoiceApplyDetailFee.feeType') : this.$t('invoiceApplyDetailFee.accountType')
          }))
          this.page.total = res.total
        }
      } catch (error) {
        this.$message.error(this.$t('invoiceApplyDetailFee.fetchError'))
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    },
    viewFeeDetail(fee) {
      this.$router.push({ path: '/views/fee/feeDetail', query: { feeId: fee.feeId }})
    },
    viewAcctDetail(fee) {
      this.$router.push({ path: '/views/owner/ownerDetail', query: { ownerId: fee.ownerId, currentTab: 'ownerDetailAccountReceipt' }})
    }
  }
}
</script>

<style scoped>
.invoice-apply-detail-fee {
  margin-top: 20px;
}
</style>