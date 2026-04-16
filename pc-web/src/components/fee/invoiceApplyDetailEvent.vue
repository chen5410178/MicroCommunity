<template>
  <div class="invoice-apply-detail-event">
    <el-table :data="events" border style="width: 100%">
      <el-table-column prop="eventTypeName" :label="$t('invoiceApplyDetailEvent.type')" align="center"></el-table-column>
      <el-table-column prop="createUserName" :label="$t('invoiceApplyDetailEvent.operator')" align="center"></el-table-column>
      <el-table-column prop="remark" :label="$t('invoiceApplyDetailEvent.remark')" align="center"></el-table-column>
      <el-table-column prop="createTime" :label="$t('invoiceApplyDetailEvent.time')" align="center"></el-table-column>
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
import { getInvoiceEventList } from '@/api/fee/invoiceApplyDetailApi'

export default {
  name: 'InvoiceApplyDetailEvent',
  props: {
    applyId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      events: [],
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
        const res = await getInvoiceEventList(params)
        if (res.code === 0) {
          this.events = res.data
          this.page.total = res.total
        }
      } catch (error) {
        this.$message.error(this.$t('invoiceApplyDetailEvent.fetchError'))
      }
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

<style scoped>
.invoice-apply-detail-event {
  margin-top: 20px;
}
</style>