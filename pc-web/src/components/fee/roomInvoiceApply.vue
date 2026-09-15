<template>
  <div>
    <div class="flex justify-between margin-bottom-sm">
      <span></span>
      <el-button type="primary" size="small" @click="_invoiceApply">
        {{ $t('invoiceApply.apply') }}
      </el-button>
    </div>
    <el-table :data="invoiceApplys" border style="width: 100%">
      <el-table-column prop="applyId" :label="$t('invoiceApply.table.id')" align="center" />
      <el-table-column :label="$t('invoiceApply.table.invoiceKind')" align="center">
        <template slot-scope="scope">
          {{ scope.row.invoiceKind || $t('invoiceApply.normalInvoice') }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('invoiceApply.table.invoiceType')" align="center">
        <template slot-scope="scope">
          {{ scope.row.invoiceType === '1001' ? $t('invoiceApply.personal') : $t('invoiceApply.company') }}
        </template>
      </el-table-column>
      <el-table-column prop="ownerName" :label="$t('invoiceApply.table.ownerName')" align="center" />
      <el-table-column :label="$t('invoiceApply.table.applicant')" align="center">
        <template slot-scope="scope">
          {{ scope.row.createUserName }}({{ scope.row.applyTel }})
        </template>
      </el-table-column>
      <el-table-column prop="invoiceName" :label="$t('invoiceApply.table.invoiceName')" align="center" />
      <el-table-column prop="invoiceNum" :label="$t('invoiceApply.table.taxNumber')" align="center" />
      <el-table-column prop="invoiceAddress" :label="$t('invoiceApply.table.addressPhone')" align="center" />
      <el-table-column prop="invoiceAmount" :label="$t('invoiceApply.table.amount')" align="center" />
      <el-table-column prop="invoiceCode" :label="$t('invoiceApply.table.invoiceCode')" align="center">
        <template slot-scope="scope">
          {{ scope.row.invoiceCode || $t('invoiceApply.notUploaded') }}
        </template>
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('invoiceApply.table.submitState')" align="center" />
      <el-table-column :label="$t('invoiceApply.table.mallState')" align="center">
        <template slot-scope="scope">
          {{ mallStateName(scope.row.mallState) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('invoiceApply.table.failReason')" align="center" width="200">
        <template slot-scope="scope">
          <el-tooltip :content="failReason(scope.row)" :disabled="failReason(scope.row) === '-'" placement="top">
            <div class="text-ellipsis">{{ failReason(scope.row) }}</div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('invoiceApply.table.applyTime')" align="center" />
      <el-table-column :label="$t('invoiceApply.table.invoicePdf')" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.mallPdfUrl" type="text" @click="_downloadInvoicePdf(scope.row)">
            {{ $t('invoiceApply.downloadInvoice') }}
          </el-button>
          <span v-else>{{ $t('invoiceApply.noInvoicePdf') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="scope.row.mallState === 'DONE'" size="mini" type="warning" @click="_openRedDialog(scope.row)">
            {{ $t('invoiceApply.invoiceRed') }}
          </el-button>
          <el-button size="mini" @click="_openInvoiceApplyDetail(scope.row)">
            {{ $t('common.detail') }}
          </el-button>
          <el-button size="mini" type="danger" @click="_openDeleteInvoiceApplyModel(scope.row)">
            {{ $t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination class="margin-top" :current-page="page.current" :page-sizes="[10, 20, 30, 50]"
      :page-size="page.size" :total="page.total" layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange" @current-change="handleCurrentChange" />

    <delete-invoice-apply ref="deleteDialog" @success="listInvoiceApplys" />
    <invoice-red-dialog ref="redDialog" @success="listInvoiceApplys" />
  </div>
</template>

<script>
import { listInvoiceApply } from '@/api/fee/invoiceApplyApi'
import { getCommunityId } from '@/api/community/communityApi'
import DeleteInvoiceApply from '@/components/fee/deleteInvoiceApply'
import InvoiceRedDialog from '@/components/fee/invoiceRedDialog'

export default {
  name: 'RoomInvoiceApply',
  components: {
    DeleteInvoiceApply,
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
    _openDeleteInvoiceApplyModel(row) {
      this.$refs.deleteDialog.open(row)
    },
    mallStateName(mallState) {
      if (!mallState) {
        return '-'
      }
      const map = {
        AUDIT: this.$t('invoiceApply.mallState.AUDIT'),
        WAIT: this.$t('invoiceApply.mallState.WAIT'),
        DONE: this.$t('invoiceApply.mallState.DONE'),
        FAIL: this.$t('invoiceApply.mallState.FAIL'),
        RED: this.$t('invoiceApply.mallState.RED')
      }
      return map[mallState] || mallState
    },
    failReason(row) {
      if (row.failReason) {
        return row.failReason
      }
      if (row.state === 'F') {
        return row.remark
      }
      return '-'
    },
    _downloadInvoicePdf(row) {
      if (!row.mallPdfUrl) {
        this.$message.warning(this.$t('invoiceApply.noInvoicePdf'))
        return
      }
      window.open(row.mallPdfUrl)
    }
  }
}
</script>

<style scoped>
.text-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
