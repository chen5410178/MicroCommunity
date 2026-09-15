<template>
  <div>
    <div class="flex justify-between margin-bottom-sm">
      <span>{{ $t('invoiceApply.redTitle') }}</span>
    </div>

    <invoice-red-table :list-data="invoiceReds" />

    <el-pagination class="margin-top-sm" :current-page="page.current" :page-sizes="[10, 20, 30, 50]"
      :page-size="page.size" :total="page.total" layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange" @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { listInvoiceReds } from '@/api/fee/invoiceApplyApi'
import { getCommunityId } from '@/api/community/communityApi'
import InvoiceRedTable from '@/components/fee/invoiceRedTable'

export default {
  name: 'RoomInvoiceRed',
  components: {
    InvoiceRedTable
  },
  data() {
    return {
      roomId: '',
      invoiceReds: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    // 由 roomCreateFeeList 的 changeTab 调用(按 :key 随房屋切换重新挂载)
    open(params) {
      if (!params || !params.roomId) return
      this.roomId = params.roomId
      this.page.current = 1
      this.listInvoiceReds()
    },
    async listInvoiceReds() {
      if (!this.roomId) return
      try {
        const res = await listInvoiceReds({
          roomId: this.roomId,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        })
        this.invoiceReds = res.data || []
        this.page.total = res.total || 0
      } catch (error) {
        // 商城未开启或调用异常时后端返回空分页, 此处兜底为空列表, 不弹错误提示
        this.invoiceReds = []
        this.page.total = 0
      }
    },
    handleSizeChange(size) {
      this.page.size = size
      this.listInvoiceReds()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this.listInvoiceReds()
    }
  }
}
</script>
