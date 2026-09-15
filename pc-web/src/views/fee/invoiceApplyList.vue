<template>
  <div class="app-container">
    <div class="resizable-layout">
      <!-- 左侧：房屋筛选树 -->
      <div class="tree-panel" :style="{ width: treeWidth + 'px' }">
        <div class="tree-panel-header">
          <span>{{ $t('invoiceApply.roomFilterTitle') }}</span>
          <el-button v-if="treeHasRoom" type="text" size="small" @click="_showAllRooms">
            {{ $t('invoiceApply.allRooms') }}
          </el-button>
        </div>
        <div class="tree-body">
          <room-tree-div-has-owner ref="roomTreeDiv" :auto-select-first="false" storage-key="invoiceApplyFilterRoom"
            @selectRoom="handleSelectRoom" @treeEmpty="handleTreeEmpty" />
        </div>
      </div>

      <!-- 拖拽分隔线 -->
      <div class="resize-handle" @mousedown="startResize" @mouseover="showResizeCursor" @mouseleave="hideResizeCursor">
        <div class="resize-line"></div>
      </div>

      <!-- 右侧：发票记录 -->
      <div class="content-panel" :style="{ width: 'calc(100% - ' + (treeWidth + 10) + 'px)' }">
        <el-card>
          <div slot="header" class="text-left">
            <span>{{ $t('invoiceApply.searchTitle') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="conditions.invoiceCode" :placeholder="$t('invoiceApply.invoiceCodePlaceholder')" />
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.invoiceKind" :placeholder="$t('invoiceApply.invoiceKindPlaceholder')">
                <el-option :label="$t('invoiceApply.invoiceKindAll')" value="" />
                <el-option :label="$t('invoiceApply.normalInvoice')" value="普票" />
                <el-option :label="$t('invoiceApply.specialInvoice')" value="专票" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.invoiceType" :placeholder="$t('invoiceApply.invoiceTypePlaceholder')">
                <el-option :label="$t('invoiceApply.invoiceTypeAll')" value="" />
                <el-option :label="$t('invoiceApply.personal')" value="1001" />
                <el-option :label="$t('invoiceApply.company')" value="2002" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.state" :placeholder="$t('invoiceApply.table.submitState')">
                <el-option :label="$t('invoiceApply.submitStateAll')" value="" />
                <el-option :label="$t('invoiceApply.submitStateSubmit')" value="S" />
                <el-option :label="$t('invoiceApply.submitStateFail')" value="F" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model="conditions.ownerName" :placeholder="$t('invoiceApply.ownerNamePlaceholder')" />
            </el-col>
            <el-col :span="4">
              <el-input v-model="conditions.createUserName" :placeholder="$t('invoiceApply.applicantPlaceholder')" />
            </el-col>
            <el-col :span="4" class="margin-top-sm">
              <el-input v-model="conditions.applyTel" :placeholder="$t('invoiceApply.phonePlaceholder')" />
            </el-col>
            <el-col :span="4" class="margin-top-sm">
              <el-input v-model="conditions.roomNameLike" :placeholder="$t('invoiceApply.roomNamePlaceholder')"
                clearable />
            </el-col>
            <el-col :span="4" class="margin-top-sm">
              <el-button type="primary" @click="_queryInvoiceApplyMethod">
                <i class="el-icon-search" />
                <span>{{ $t('common.search') }}</span>
              </el-button>
              <el-button size="small" @click="_resetConditionMethod">
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>
        <el-card class="margin-top">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('invoiceApply.title') }}</span>
            <el-button type="primary" size="small" class="float-right" @click="_invoiceApply">
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
            <el-table-column prop="invoiceType" :label="$t('invoiceApply.table.invoiceType')" align="center">
              <template slot-scope="scope">
                {{ scope.row.invoiceType === '1001' ? $t('invoiceApply.personal') : $t('invoiceApply.company') }}
              </template>
            </el-table-column>
            <el-table-column prop="ownerName" :label="$t('invoiceApply.table.ownerName')" align="center" />
            <el-table-column :label="$t('invoiceApply.table.roomName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.roomName || '-' }}
              </template>
            </el-table-column>
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
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" @click="_openInvoiceApplyDetail(scope.row)">
                  {{ $t('common.detail') }}
                </el-button>
                <el-button v-if="scope.row.state === 'F'" size="mini" type="primary"
                  @click="_reSubmitInvoiceApply(scope.row)">
                  {{ $t('invoiceApply.reSubmit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="_openDeleteInvoiceApplyModel(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </div>
    </div>

    <delete-invoice-apply ref="deleteDialog" @success="listInvoiceApplys" />
  </div>
</template>

<script>
import { listInvoiceApply, reSubmitInvoiceApply } from '@/api/fee/invoiceApplyApi'
import { getCommunityId } from '@/api/community/communityApi'
import DeleteInvoiceApply from '@/components/fee/deleteInvoiceApply'
import RoomTreeDivHasOwner from '@/components/room/roomTreeDivHasOwner'

export default {
  name: 'InvoiceApplyList',
  components: {
    DeleteInvoiceApply,
    RoomTreeDivHasOwner
  },
  data() {
    return {
      conditions: {
        invoiceCode: '',
        invoiceKind: '',
        invoiceType: '',
        ownerName: '',
        applyTel: '',
        createUserName: '',
        state: '',
        roomId: '',
        roomNameLike: ''
      },
      invoiceApplys: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      // 房屋树筛选
      treeHasRoom: true,
      treeRoomName: '',
      // 拖拽相关
      treeWidth: 250,
      minTreeWidth: 200,
      maxTreeWidth: 600,
      isResizing: false,
      startX: 0,
      startWidth: 0
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.restoreTreeWidth()
    this.listInvoiceApplys()
  },
  mounted() {
    document.addEventListener('mousemove', this.onMouseMove)
    document.addEventListener('mouseup', this.onMouseUp)
  },
  beforeDestroy() {
    document.removeEventListener('mousemove', this.onMouseMove)
    document.removeEventListener('mouseup', this.onMouseUp)
  },
  methods: {
    // 点击房屋树：按房屋精确筛选(与房屋编号输入互斥)
    handleSelectRoom(param) {
      this.conditions.roomId = param.roomId
      this.conditions.roomNameLike = ''
      this.treeRoomName = param.roomName
      this.page.current = 1
      this.listInvoiceApplys()
    },
    handleTreeEmpty() {
      this.treeHasRoom = false
    },
    // 全部房屋：清除树筛选与输入筛选
    _showAllRooms() {
      this.conditions.roomId = ''
      this.conditions.roomNameLike = ''
      this.treeRoomName = ''
      if (this.$refs.roomTreeDiv) {
        this.$refs.roomTreeDiv.clearSelection()
      }
      this.page.current = 1
      this.listInvoiceApplys()
    },
    _resetConditionMethod() {
      this.conditions = {
        invoiceCode: '',
        invoiceKind: '',
        invoiceType: '',
        ownerName: '',
        applyTel: '',
        createUserName: '',
        state: '',
        roomId: '',
        roomNameLike: ''
      }
      this.treeRoomName = ''
      if (this.$refs.roomTreeDiv) {
        this.$refs.roomTreeDiv.clearSelection()
      }
      this.page.current = 1
      this.listInvoiceApplys()
    },
    async listInvoiceApplys() {
      try {
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId
        }
        const res = await listInvoiceApply(params)
        this.invoiceApplys = res.data
        this.page.total = res.total
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
    _queryInvoiceApplyMethod() {
      // 输入房屋编号查询时清除树筛选(互斥)
      if (this.conditions.roomNameLike) {
        this.conditions.roomId = ''
        this.treeRoomName = ''
        if (this.$refs.roomTreeDiv) {
          this.$refs.roomTreeDiv.clearSelection()
        }
      }
      this.page.current = 1
      this.listInvoiceApplys()
    },
    _invoiceApply() {
      if (this.conditions.roomId) {
        this.$router.push({
          path: '/views/fee/ownerApplyInvoice',
          query: { roomId: this.conditions.roomId, roomName: this.treeRoomName }
        })
        return
      }
      this.$router.push('/views/fee/ownerApplyInvoice')
    },
    _openInvoiceApplyDetail(row) {
      this.$router.push(`/views/fee/invoiceApplyDetail?applyId=${row.applyId}`)
    },
    _downloadInvoicePdf(row) {
      if (!row.mallPdfUrl) {
        this.$message.warning(this.$t('invoiceApply.noInvoicePdf'))
        return
      }
      window.open(row.mallPdfUrl)
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
    restoreTreeWidth() {
      const savedWidth = localStorage.getItem('invoiceApplyTreeWidth')
      if (savedWidth) {
        const width = parseInt(savedWidth)
        if (width >= this.minTreeWidth && width <= this.maxTreeWidth) {
          this.treeWidth = width
        }
      }
    },
    saveTreeWidth() {
      localStorage.setItem('invoiceApplyTreeWidth', this.treeWidth.toString())
    },
    startResize(event) {
      event.preventDefault()
      this.isResizing = true
      this.startX = event.clientX
      this.startWidth = this.treeWidth
      document.body.style.cursor = 'col-resize'
      document.body.style.userSelect = 'none'
    },
    onMouseMove(event) {
      if (!this.isResizing) return
      const deltaX = event.clientX - this.startX
      const newWidth = this.startWidth + deltaX
      if (newWidth >= this.minTreeWidth && newWidth <= this.maxTreeWidth) {
        this.treeWidth = newWidth
      }
    },
    onMouseUp() {
      if (this.isResizing) {
        this.isResizing = false
        this.saveTreeWidth()
        document.body.style.cursor = ''
        document.body.style.userSelect = ''
      }
    },
    showResizeCursor() {
      if (!this.isResizing) {
        document.body.style.cursor = 'col-resize'
      }
    },
    hideResizeCursor() {
      if (!this.isResizing) {
        document.body.style.cursor = ''
      }
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
  height: calc(100vh - 120px);
  overflow: hidden;
}

.resizable-layout {
  display: flex;
  flex-direction: row;
  height: 100%;
}

.tree-panel {
  background: #fff;
  border-radius: 4px;
  padding: 10px;
  border: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.tree-panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 8px;
  margin-bottom: 6px;
  border-bottom: 1px solid #ebeef5;
  font-size: 14px;
  color: #303133;
}

.tree-body {
  flex: 1;
  min-height: 0;
  overflow: hidden;
}

.content-panel {
  overflow-y: auto;
}

.resize-handle {
  width: 10px;
  cursor: col-resize;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: center;
}

.resize-handle:hover .resize-line {
  background-color: #409eff;
}

.resize-line {
  width: 2px;
  height: 30px;
  background-color: #dcdfe6;
  border-radius: 1px;
  transition: background-color 0.2s;
}

.border-radius {
  border-radius: 4px;
}

.float-right {
  float: right;
}

.text-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.tree-panel * {
  user-select: none;
}
</style>
