<template>
  <div class="app-container">
    <div class="resizable-layout">
      <!-- 左侧：房屋筛选树 -->
      <div class="tree-panel" :style="{ width: treeWidth + 'px' }">
        <div class="tree-body">
          <div class="tree-all-row" @click="_showAllRooms">
            <el-button type="text" @click="_showAllRooms">
              {{ $t('common.all') }}
            </el-button>
          </div>
          <div class="tree-scroll">
            <room-tree-div-has-owner ref="roomTreeDiv" :auto-select-first="false" storage-key="invoiceApplyFilterRoom"
              @selectFloor="handleSelectFloor" @selectUnit="handleSelectUnit" @selectRoom="handleSelectRoom"
              @treeEmpty="handleTreeEmpty" />
          </div>
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
          <el-tabs v-model="activeTab" @tab-click="handleTabClick">
            <el-tab-pane :label="$t('invoiceApply.tabInvoice')" name="invoice">
              <invoice-apply-record v-if="activeTab === 'invoice'" ref="invoiceApplyRecord" :conditions="conditions"
                :room-name="treeRoomName" />
            </el-tab-pane>
            <el-tab-pane :label="$t('invoiceApply.tabInvoiceRed')" name="invoiceRed">
              <invoice-red-record v-if="activeTab === 'invoiceRed'" ref="invoiceRedRecord"
                :conditions="conditions" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import InvoiceApplyRecord from '@/components/fee/invoiceApplyRecord'
import InvoiceRedRecord from '@/components/fee/invoiceRedRecord'
import RoomTreeDivHasOwner from '@/components/room/roomTreeDivHasOwner'

export default {
  name: 'InvoiceApplyList',
  components: {
    InvoiceApplyRecord,
    InvoiceRedRecord,
    RoomTreeDivHasOwner
  },
  data() {
    return {
      activeTab: 'invoice',
      conditions: {
        invoiceCode: '',
        invoiceKind: '',
        invoiceType: '',
        ownerName: '',
        applyTel: '',
        createUserName: '',
        state: '',
        roomId: '',
        roomNameLike: '',
        floorId: '',
        unitId: ''
      },
      // 房屋树筛选
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
    this.restoreTreeWidth()
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
    // 点击楼栋：按楼栋筛选(后端 left join building_unit 按 floor_id 过滤)
    handleSelectFloor(param) {
      this._clearRoomFilter()
      this.conditions.floorId = param.floorId
      this.listCurrentTab()
    },
    // 点击单元：按单元筛选(后端 left join building_unit 按 unit_id 过滤)
    handleSelectUnit(param) {
      this._clearRoomFilter()
      this.conditions.unitId = param.unitId
      this.listCurrentTab()
    },
    // 点击房屋：按房屋精确筛选
    handleSelectRoom(param) {
      this._clearRoomFilter()
      this.conditions.roomId = param.roomId
      this.treeRoomName = param.roomName
      this.listCurrentTab()
    },
    // 清空树筛选(房屋/楼栋/单元)与选房记忆
    _clearTreeFilter() {
      this.conditions.roomId = ''
      this.conditions.floorId = ''
      this.conditions.unitId = ''
      this.treeRoomName = ''
      if (this.$refs.roomTreeDiv) {
        this.$refs.roomTreeDiv.clearSelection()
      }
    },
    // 清空房屋维度全部筛选(树筛选 + 房屋编号输入)
    _clearRoomFilter() {
      this.conditions.roomNameLike = ''
      this._clearTreeFilter()
    },
    handleTreeEmpty() {
      // 无房屋时右侧仍展示全部记录, 不阻塞
    },
    // 全部：清除房屋筛选并重新查询
    _showAllRooms() {
      this._clearRoomFilter()
      this.listCurrentTab()
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
        roomNameLike: '',
        floorId: '',
        unitId: ''
      }
      this._clearTreeFilter()
      this.listCurrentTab()
    },
    // 两个 tab 共用查询条件与房屋树, 数据由各自子组件维护
    handleTabClick(tab) {
      this.activeTab = tab.name
    },
    // 刷新当前 tab 子组件(子组件按 v-if 懒加载, 首次挂载时自行加载)
    listCurrentTab() {
      const refName = this.activeTab === 'invoiceRed' ? 'invoiceRedRecord' : 'invoiceApplyRecord'
      const target = this.$refs[refName]
      if (target) {
        target.reload()
      }
    },
    _queryInvoiceApplyMethod() {
      // 输入房屋编号查询时清除树筛选(互斥)
      if (this.conditions.roomNameLike) {
        this._clearTreeFilter()
      }
      this.listCurrentTab()
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

.tree-body {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* "全部"入口与楼栋节点对齐(树组件 10px 内边距 + 展开箭头约 24px) */
.tree-all-row {
  padding: 5px 10px 5px 34px;
}

.tree-all-row .el-button {
  padding: 0;
  font-size: 14px;
}

.tree-scroll {
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

.tree-panel * {
  user-select: none;
}
</style>
