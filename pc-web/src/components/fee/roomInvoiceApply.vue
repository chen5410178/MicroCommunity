<template>
  <div>
    <div class="flex justify-between margin-bottom-sm">
      <span>{{ $t('invoiceApply.title') }}</span>
      <el-button type="primary" size="small" @click="_invoiceApply">
        {{ $t('invoiceApply.apply') }}
      </el-button>
    </div>

    <invoice-apply-table :list-data="invoiceApplys" :show-room-column="false" @detail="_openInvoiceApplyDetail"
      @red="_openRedDialog" @submit="_reSubmitInvoiceApply" @delete="_openDeleteInvoiceApplyModel" />

    <el-pagination class="margin-top-sm" :current-page="page.current" :page-sizes="[10, 20, 30, 50]"
      :page-size="page.size" :total="page.total" layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange" @current-change="handleCurrentChange" />

    <delete-invoice-apply ref="deleteDialog" @success="listInvoiceApplys" />
    <invoice-red-dialog ref="redDialog" @success="listInvoiceApplys" />
  </div>
</template>

<script>
import { listInvoiceApply, reSubmitInvoiceApply } from '@/api/fee/invoiceApplyApi'
import { getCommunityId } from '@/api/community/communityApi'
import DeleteInvoiceApply from '@/components/fee/deleteInvoiceApply'
import InvoiceApplyTable from '@/components/fee/invoiceApplyTable'
import InvoiceRedDialog from '@/components/fee/invoiceRedDialog'

export default {
  name: 'RoomInvoiceApply',
  components: {
    DeleteInvoiceApply,
    InvoiceApplyTable,
    InvoiceRedDialog
  },
  data() {
    return {
      roomId: '',
      roomName: '',
      invoiceApplys: [],
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
      this.roomName = params.roomName || ''
      this.page.current = 1
      this.listInvoiceApplys()
    },
    async listInvoiceApplys() {
      if (!this.roomId) return
      try {
        const res = await listInvoiceApply({
          roomId: this.roomId,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        })
        this.invoiceApplys = res.data || []
        this.page.total = res.total || 0
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      }
    },
    handleSizeChange(size) {
      this.page.size = size
      this.listInvoiceApplys()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this.listInvoiceApplys()
    },
    _invoiceApply() {
      this.$router.push({
        path: '/views/fee/ownerApplyInvoice',
        query: { roomId: this.roomId, roomName: this.roomName }
      })
    },
    _openInvoiceApplyDetail(row) {
      this.$router.push(`/views/fee/invoiceApplyDetail?applyId=${row.applyId}`)
    },
    _openRedDialog(row) {
      this.$refs.redDialog.open(row)
    },
    async _reSubmitInvoiceApply(row) {
      try {
        const res = await reSubmitInvoiceApply({ applyId: row.applyId })
        const data = res.data || {}
        if (data.state === 'F') {
          this.$message.error(data.failReason || this.$t('invoiceApply.submitFailed'))
        } else {
          this.$message.success(this.$t('invoiceApply.submitSuccess'))
        }
        this.listInvoiceApplys()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    _openDeleteInvoiceApplyModel(row) {
      this.$refs.deleteDialog.open(row)
    }
  }
}
</script>
