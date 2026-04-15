<template>
  <div class="app-container">
    <div class="resizable-layout">
      <!-- 左侧树形组件 -->
      <el-card v-if="isTreeEmpty" class="empty-page">
        <i class="el-icon-document"></i>
        <span>请添加房屋</span>
      </el-card>
      <template v-else>
      <div class="tree-panel" :style="{ width: treeWidth + 'px' }">
        <room-tree-div ref="roomTreeDiv" @selectRoom="handleSelectRoom" @treeEmpty="handleTreeEmpty"></room-tree-div>
      </div>

      <!-- 拖拽分隔线 -->
      <div class="resize-handle" @mousedown="startResize" @mouseover="showResizeCursor" @mouseleave="hideResizeCursor">
        <div class="resize-line"></div>
      </div>

      <!-- 右侧内容区域 -->
      <div class="content-panel" :style="{ width: 'calc(100% - ' + (treeWidth + 10) + 'px)' }">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ roomCreateFeeInfo.roomName }}</span>
            <div class="header-tools">
              <el-button v-if="hasPrivilege('502022073096990007')" size="small" @click="_openFeeImportExcel">
                <i class="el-icon-download"></i>
                {{ $t('roomCreateFee.customTemplate') }}
              </el-button>
              <el-button v-if="hasPrivilege('502022073096990007')" size="small" @click="_openDoCreateRoomFee">
                <i class="el-icon-plus"></i>
                {{ $t('roomCreateFee.customImport') }}
              </el-button>
              <el-button v-if="hasPrivilege('502022073096990007')" size="small" @click="_toBatchCreateFee(null, true)">
                <i class="el-icon-plus"></i>
                {{ $t('roomCreateFee.batchCreate') }}
              </el-button>
              <el-button size="small" @click="_downloadCollectionLetterOrder">
                <i class="el-icon-download"></i>
                {{ $t('roomCreateFee.batchCollectionLetter') }}
              </el-button>
            </div>
          </div>

          <div class="filter-container">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-input v-model="roomCreateFeeInfo.condition.roomNum"
                  :placeholder="$t('roomCreateFee.roomNumPlaceholder')" clearable
                  @blur="listRoomInRoomCreateFee"></el-input>
                <input-search-room ref="inputSearchRoom"></input-search-room>
              </el-col>
              <el-col :span="6">
                <el-input v-model="roomCreateFeeInfo.condition.ownerName"
                  :placeholder="$t('roomCreateFee.ownerNamePlaceholder')" clearable
                  @input="_inputRoomByOwner"></el-input>
                <input-search-room-by-owner ref="inputSearchRoomByOwner"></input-search-room-by-owner>
              </el-col>
              <el-col :span="6">
                <el-button type="primary" size="small" @click="_queryRoomCreateFeeMethod">
                  {{ $t('common.query') }}
                </el-button>
                <el-button size="small" @click="_resetRoomCreateFeeMethod">
                  {{ $t('common.reset') }}
                </el-button>
              </el-col>
            </el-row>
          </div>

          <el-tabs v-model="roomCreateFeeInfo._currentTab" @tab-click="changeTab(roomCreateFeeInfo._currentTab)">
            <el-tab-pane :label="$t('roomCreateFee.roomFee')" name="simplifyRoomFee">
              <simplify-room-fee v-if="roomCreateFeeInfo._currentTab === 'simplifyRoomFee'" ref="simplifyRoomFee"
                :key="'simplifyRoomFee' + roomCreateFeeInfo.roomId"></simplify-room-fee>
            </el-tab-pane>
            <el-tab-pane v-if="roomCreateFeeInfo.ownerId" :label="$t('roomCreateFee.paymentHistory')"
              name="simplifyHisFee">
              <simplify-his-fee v-if="roomCreateFeeInfo._currentTab === 'simplifyHisFee'" ref="simplifyHisFee"
                :key="'simplifyHisFee' + roomCreateFeeInfo.roomId"></simplify-his-fee>
            </el-tab-pane>
            <el-tab-pane :label="$t('roomCreateFee.prePayFee')" name="prePayFeeDetail">
              <pre-pay-fee-detail v-if="roomCreateFeeInfo._currentTab === 'prePayFeeDetail'" ref="prePayFeeDetail"
                :key="'prePayFeeDetail' + roomCreateFeeInfo.roomId"></pre-pay-fee-detail>
            </el-tab-pane>
            <el-tab-pane :label="$t('roomCreateFee.billingRules')" name="feeDetailFeeRule">
              <fee-detail-fee-rule v-if="roomCreateFeeInfo._currentTab === 'feeDetailFeeRule'" ref="feeDetailFeeRule"
                :key="'feeDetailFeeRule' + roomCreateFeeInfo.roomId"></fee-detail-fee-rule>
            </el-tab-pane>
            <el-tab-pane v-if="roomCreateFeeInfo.ownerId" :label="$t('roomCreateFee.rooms')" name="simplifyOwnerRooms">
              <simplify-owner-rooms v-if="roomCreateFeeInfo._currentTab === 'simplifyOwnerRooms'"
                ref="simplifyOwnerRooms" :key="'simplifyOwnerRooms' + roomCreateFeeInfo.roomId"></simplify-owner-rooms>
            </el-tab-pane>
            <el-tab-pane v-if="roomCreateFeeInfo.ownerId" :label="$t('roomCreateFee.owner')" name="carDetailOwner">
              <car-detail-owner v-if="roomCreateFeeInfo._currentTab === 'carDetailOwner'" ref="carDetailOwner"
                :key="'carDetailOwner' + roomCreateFeeInfo.roomId"></car-detail-owner>
            </el-tab-pane>
            <el-tab-pane :label="$t('roomCreateFee.callable')" name="simplifyCallable">
              <simplify-callable v-if="roomCreateFeeInfo._currentTab === 'simplifyCallable'" ref="simplifyCallable"
                :key="'simplifyCallable' + roomCreateFeeInfo.roomId"></simplify-callable>
            </el-tab-pane>
            <el-tab-pane :label="$t('roomCreateFee.receiptReprint')" v-if="hasPrivilege('502020092373407363')"
              name="simplifyFeeReceipt">
              <simplify-fee-receipt v-if="roomCreateFeeInfo._currentTab === 'simplifyFeeReceipt'"
                ref="simplifyFeeReceipt" :key="'simplifyFeeReceipt' + roomCreateFeeInfo.roomId"></simplify-fee-receipt>
            </el-tab-pane>
            <el-tab-pane :label="$t('roomCreateFee.meterReadingRecords')" name="simplifyMeterWaterLog">
              <simplify-meter-water-log v-if="roomCreateFeeInfo._currentTab === 'simplifyMeterWaterLog'"
                ref="simplifyMeterWaterLog"
                :key="'simplifyMeterWaterLog' + roomCreateFeeInfo.roomId"></simplify-meter-water-log>
            </el-tab-pane>
            <el-tab-pane :label="$t('roomCreateFee.deposit')" name="simplifyRefundDeposit">
              <simplify-refund-deposit v-if="roomCreateFeeInfo._currentTab === 'simplifyRefundDeposit'"
                ref="simplifyRefundDeposit"
                :key="'simplifyRefundDeposit' + roomCreateFeeInfo.roomId"></simplify-refund-deposit>
            </el-tab-pane>
            
          </el-tabs>
        </el-card>
      </div>
      </template>
    </div>

    <export-fee-import-excel ref="exportFeeImportExcel"></export-fee-import-excel>
    <do-import-create-fee ref="doImportCreateFee"></do-import-create-fee>
    <download-collection-letter-order ref="downloadCollectionLetterOrder"></download-collection-letter-order>
  </div>
</template>

<script>
import RoomTreeDiv from '@/components/room/roomTreeDiv'
import InputSearchRoom from '@/components/fee/inputSearchRoom'
import InputSearchRoomByOwner from '@/components/fee/inputSearchRoomByOwner'
import SimplifyRoomFee from '@/components/fee/simplifyRoomFee'
import SimplifyHisFee from '@/components/fee/simplifyHisFee'
import SimplifyOwnerRooms from '@/components/fee/simplifyOwnerRooms'
import CarDetailOwner from '@/components/fee/carDetailOwner'
import SimplifyCallable from '@/components/fee/simplifyCallable'
import SimplifyFeeReceipt from '@/components/fee/simplifyFeeReceipt'
import SimplifyMeterWaterLog from '@/components/fee/simplifyMeterWaterLog'
import SimplifyRefundDeposit from '@/components/fee/simplifyRefundDeposit'
import FeeDetailFeeRule from '@/components/fee/feeDetailFeeRule'
import PrePayFeeDetail from '@/components/fee/prePayFeeDetail'

import ExportFeeImportExcel from '@/components/fee/exportFeeImportExcel'
import DoImportCreateFee from '@/components/fee/doImportCreateFee'
import DownloadCollectionLetterOrder from '@/components/fee/downloadCollectionLetterOrder'
import { getCommunityId } from '@/api/community/communityApi'
import { queryRooms } from '@/api/fee/roomCreateFeeApi'

export default {
  name: 'RoomCreateFeeList',
  components: {
    RoomTreeDiv,
    InputSearchRoom,
    InputSearchRoomByOwner,
    SimplifyRoomFee,
    SimplifyHisFee,
    SimplifyOwnerRooms,
    CarDetailOwner,
    SimplifyCallable,
    SimplifyFeeReceipt,
    SimplifyMeterWaterLog,

    SimplifyRefundDeposit,
    FeeDetailFeeRule,

    ExportFeeImportExcel,
    DoImportCreateFee,
    DownloadCollectionLetterOrder,
    PrePayFeeDetail
  },
  data() {
    return {
      roomCreateFeeInfo: {
        roomName: '',
        roomId: '',
        ownerId: '',
        ownerName: '',
        roomType: '',
        _currentTab: 'simplifyRoomFee',
        condition: {
          roomNum: '',
          ownerName: ''
        },
        timer: null
      },
      communityId: '',
      // 新增：拖拽相关数据
      treeWidth: 250, // 默认树形组件宽度
      minTreeWidth: 200, // 最小宽度
      maxTreeWidth: 600, // 最大宽度
      isResizing: false,
      startX: 0,
      startWidth: 0,
      isTreeEmpty: false
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.initRoomTree()
    // 恢复保存的宽度
    this.restoreTreeWidth()
  },
  mounted() {
    // 添加全局鼠标事件监听
    document.addEventListener('mousemove', this.onMouseMove)
    document.addEventListener('mouseup', this.onMouseUp)
  },
  beforeDestroy() {
    // 移除全局事件监听
    document.removeEventListener('mousemove', this.onMouseMove)
    document.removeEventListener('mouseup', this.onMouseUp)
  },
  methods: {
    handleTreeEmpty() {
      this.isTreeEmpty = true
    },
    initRoomTree() {
      // this.$refs.roomTreeDiv.initRoomTreeDiv({
      //   callName: 'roomCreateFee'
      // })
    },
    handleSelectRoom(param) {
      this.roomCreateFeeInfo.ownerName = ''
      this.roomCreateFeeInfo.ownerId = ''
      this.roomCreateFeeInfo.roomId = param.roomId
      this.roomCreateFeeInfo.roomName = param.roomName

      this.roomCreateFeeInfo.condition.ownerName = ''
      this.roomCreateFeeInfo.condition.roomNum = ''
      this.listRoomInRoomCreateFee()
    },
    notifyRoomByOwner(room) {
      this.$set(this.roomCreateFeeInfo, 'ownerName', room.ownerName)
      this.$set(this.roomCreateFeeInfo, 'ownerId', room.ownerId)
      this.$set(this.roomCreateFeeInfo, 'roomId', room.roomId)
      this.$set(
        this.roomCreateFeeInfo,
        'roomName',
        `${room.floorNum}-${room.unitNum}-${room.roomNum}`
      )
      this.listRoomInRoomCreateFee()
    },
    async listRoomInRoomCreateFee() {
      const param = {
        page: 1,
        row: 1,
        communityId: this.communityId,
        roomId: this.roomCreateFeeInfo.roomId
      }

      if (this.roomCreateFeeInfo.condition.roomNum) {
        param.flag = '1'
        param.roomNum = this.roomCreateFeeInfo.condition.roomNum
        param.roomId = ''
        this.roomCreateFeeInfo.roomName = ''
        this.roomCreateFeeInfo.ownerName = ''
        this.roomCreateFeeInfo.ownerId = ''
        this.roomCreateFeeInfo.roomId = ''
      }

      try {
        const response = await queryRooms(param)
        const room = response.rooms[0]
        Object.assign(this.roomCreateFeeInfo, room)
        this.changeTab('simplifyRoomFee')
      } catch (error) {
        console.error('Request failed:', error)
      }
    },
    _inputRoomByOwner() {
      if (this.roomCreateFeeInfo.timer) {
        clearTimeout(this.roomCreateFeeInfo.timer)
      }
      this.roomCreateFeeInfo.timer = setTimeout(() => {
        this.$refs.inputSearchRoomByOwner.$emit('searchRoom', {
          callComponent: 'roomCreateFee',
          ownerName: this.roomCreateFeeInfo.condition.ownerName
        })
      }, 1500)
    },
    _queryRoomCreateFeeMethod() {
      this.listRoomInRoomCreateFee()
    },
    _resetRoomCreateFeeMethod() {
      this.$refs.inputSearchRoom.$emit('close', {})
      this.$refs.inputSearchRoomByOwner.$emit('close', {})
      this.roomCreateFeeInfo.roomNum = ''
      this.roomCreateFeeInfo.ownerName = ''
      this.roomCreateFeeInfo.ownerId = ''
      this.roomCreateFeeInfo.condition.ownerName = ''
      this.roomCreateFeeInfo.condition.roomNum = ''
      this.changeTab('simplifyRoomFee')
    },
    changeTab(tab) {
      this.roomCreateFeeInfo._currentTab = tab
      setTimeout(() => {
        if (this.$refs[tab]) {
          if (tab === 'ownerDetailAccount' || tab === 'ownerDetailAccountReceipt') {
            this.$refs[tab].open(this.roomCreateFeeInfo.ownerId, this.roomCreateFeeInfo.ownerName, this.roomCreateFeeInfo.roomId)
            return;
          }
          this.$refs[tab].open({
            ownerId: this.roomCreateFeeInfo.ownerId,
            ownerName: this.roomCreateFeeInfo.ownerName,
            payerObjId: this.roomCreateFeeInfo.roomId,
            payerObjName: this.roomCreateFeeInfo.roomName,
            roomId: this.roomCreateFeeInfo.roomId,
            roomName: this.roomCreateFeeInfo.roomName
          })
        }
      }, 500)
    },
    _roomCreateFeetoSimplifyAcceptance() {

      this.$router.push('/pages/property/simplifyAcceptance?tab=业务受理&searchType=1&searchValue=' + `${this.roomCreateFeeInfo.floorNum}-${this.roomCreateFeeInfo.unitNum}-${this.roomCreateFeeInfo.roomNum}`)

    },
    _toBatchCreateFee() {
      this.$router.push('/pages/fee/roomCreatePayFee')
    },
    _downloadCollectionLetterOrder() {
      this.$router.push('/views/fee/addOweFeeCallable?callableWay=EXCEL')
    },
    _downloadRoomCollectionLetterOrder(room) {
      this.$refs.downloadCollectionLetterOrder.open({
        roomId: room.roomId
      })
    },
    _openFeeImportExcel() {
      this.$refs.exportFeeImportExcel.open()
    },
    _openDoCreateRoomFee() {
      this.$refs.doImportCreateFee.open()
    },
    _openRoomCreateFeeComboModal() {
      this.$router.push(
        `/pages/property/createFeeByCombo?payerObjId=${this.roomCreateFeeInfo.roomId}&payerObjName=${this.roomCreateFeeInfo.roomName}&payerObjType=3333`
      )
    },
    restoreTreeWidth() {
      const savedWidth = localStorage.getItem('roomCreateFeeTreeWidth')
      if (savedWidth) {
        const width = parseInt(savedWidth)
        if (width >= this.minTreeWidth && width <= this.maxTreeWidth) {
          this.treeWidth = width
        }
      }
    },
    // 新增：保存树形组件宽度
    saveTreeWidth() {
      localStorage.setItem('roomCreateFeeTreeWidth', this.treeWidth.toString())
    },
    // 新增：开始拖拽调整大小
    startResize(event) {
      event.preventDefault()
      this.isResizing = true
      this.startX = event.clientX
      this.startWidth = this.treeWidth
      
      // 添加拖拽时的样式
      document.body.style.cursor = 'col-resize'
      document.body.style.userSelect = 'none'
    },
    // 新增：鼠标移动处理
    onMouseMove(event) {
      if (!this.isResizing) return
      
      const deltaX = event.clientX - this.startX
      const newWidth = this.startWidth + deltaX
      
      // 限制宽度范围
      if (newWidth >= this.minTreeWidth && newWidth <= this.maxTreeWidth) {
        this.treeWidth = newWidth
      }
    },
    // 新增：鼠标释放处理
    onMouseUp() {
      if (this.isResizing) {
        this.isResizing = false
        this.saveTreeWidth()
        
        // 恢复鼠标样式
        document.body.style.cursor = ''
        document.body.style.userSelect = ''
      }
    },
    // 新增：显示调整大小光标
    showResizeCursor() {
      if (!this.isResizing) {
        document.body.style.cursor = 'col-resize'
      }
    },
    // 新增：隐藏调整大小光标
    hideResizeCursor() {
      if (!this.isResizing) {
        document.body.style.cursor = ''
      }
    },
  }
}
</script>
<style scoped>
.app-container {
  padding: 20px;
  height: calc(100vh - 120px);
  overflow: hidden;
}

/* 新增：可调整大小的布局样式 */
.resizable-layout {
  display: flex;
  flex-direction: row;
  height: 100%;
}

.empty-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  color: #909399;
  font-size: 16px;

  i {
    font-size: 64px;
    margin-bottom: 20px;
  }
}

.tree-panel {
  background: #fff;
  border-radius: 4px;
  padding: 10px;
  border: 1px solid #e4e7ed;
  
}

.content-panel {
  overflow-y: auto;
}

/* 新增：拖拽分隔线样式 */
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

/* 拖拽时的样式 */
.resizable-layout.resizing {
  cursor: col-resize;
}

.resizable-layout.resizing .tree-panel,
.resizable-layout.resizing .content-panel {
  pointer-events: none;
}

.resizable-layout.resizing .resize-handle {
  pointer-events: auto;
}

/* 原有样式保持不变 */
.header-tools {
  float: right;
}

.filter-container {
  margin-bottom: 20px;
}

/* 确保树形组件在拖拽时不被选中 */
.tree-panel * {
  user-select: none;
}

/* 拖拽时禁用文本选择 */
.resizable-layout.resizing * {
  user-select: none !important;
}
</style>