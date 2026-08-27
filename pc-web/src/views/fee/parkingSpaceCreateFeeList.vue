<template>
  <div class="parking-space-create-fee-container">
    <div class="resizable-layout" :class="{ resizing: isResizing }">
      <el-card v-if="isTreeEmpty" class="empty-page">
        <i class="el-icon-document"></i>
        <span>{{ $t('parkingSpaceCreateFee.parkingSpaceEmpty') }}</span>
      </el-card>
      <template v-else>
        <div class="tree-panel" :style="{ width: treeWidth + 'px' }">
          <parking-space-tree-has-owner ref="parkingSpaceTree"
            @selectParkingSpace="handleSelectParkingSpace" @treeEmpty="handleTreeEmpty"></parking-space-tree-has-owner>
        </div>

        <!-- 拖拽分隔线 -->
        <div class="resize-handle" @mousedown="startResize" @mouseover="showResizeCursor" @mouseleave="hideResizeCursor">
          <div class="resize-line"></div>
        </div>

        <!-- 右侧内容区域 -->
        <div class="content-panel" :style="{ width: 'calc(100% - ' + (treeWidth + 10) + 'px)' }">
          <el-card>
            <div slot="header" class="card-header">
              <span>{{ parkingSpaceCreateFeeInfo.psName }}</span>
              <div class="header-tools">
                <el-button v-if="hasPrivilege('502022073096990007')" type="primary" size="small"
                  @click="_openFeeImportExcel">
                  <i class="el-icon-download"></i>
                  {{ $t('parkingSpaceCreateFee.customTemplate') }}
                </el-button>
                <el-button v-if="hasPrivilege('502022073096990007')" type="primary" size="small"
                  @click="_openDoCreateRoomFee">
                  <i class="el-icon-plus"></i>
                  {{ $t('parkingSpaceCreateFee.customImport') }}
                </el-button>
              </div>
            </div>

            <div class="tab-section">
              <el-tabs v-model="parkingSpaceCreateFeeInfo._currentTab"
                @tab-click="changeTab(parkingSpaceCreateFeeInfo._currentTab)">
                <el-tab-pane :label="$t('parkingSpaceCreateFee.parkingSpaceFee')" name="simplifyParkingSpaceFee">
                  <simplify-parking-space-fee v-if="parkingSpaceCreateFeeInfo._currentTab === 'simplifyParkingSpaceFee'"
                    ref="simplifyParkingSpaceFee"></simplify-parking-space-fee>
                </el-tab-pane>

                <el-tab-pane :label="$t('parkingSpaceCreateFee.parkingSpaceCar')" name="parkingSpaceCarDetail">
                  <parking-space-car-detail v-if="parkingSpaceCreateFeeInfo._currentTab === 'parkingSpaceCarDetail'"
                    ref="parkingSpaceCarDetail"></parking-space-car-detail>
                </el-tab-pane>

                <el-tab-pane :label="$t('parkingSpaceCreateFee.paymentHistory')" name="simplifyHisFee">
                  <simplify-his-fee v-if="parkingSpaceCreateFeeInfo._currentTab === 'simplifyHisFee'"
                    ref="simplifyHisFee"></simplify-his-fee>
                </el-tab-pane>

                <el-tab-pane :label="$t('parkingSpaceCreateFee.prepayment')" name="prePayFeeDetail">
                  <pre-pay-fee-detail v-if="parkingSpaceCreateFeeInfo._currentTab === 'prePayFeeDetail'"
                    ref="prePayFeeDetail"></pre-pay-fee-detail>
                </el-tab-pane>

                <el-tab-pane :label="$t('parkingSpaceCreateFee.setFee')" name="feeDetailParkingSpaceFeeRule">
                  <fee-detail-parking-space-fee-rule v-if="parkingSpaceCreateFeeInfo._currentTab === 'feeDetailParkingSpaceFeeRule'"
                    ref="feeDetailParkingSpaceFeeRule"></fee-detail-parking-space-fee-rule>
                </el-tab-pane>

                <el-tab-pane v-if="parkingSpaceCreateFeeInfo.ownerId" :label="$t('parkingSpaceCreateFee.house')"
                  name="simplifyOwnerRooms">
                  <simplify-owner-rooms v-if="parkingSpaceCreateFeeInfo._currentTab === 'simplifyOwnerRooms'"
                    ref="simplifyOwnerRooms"></simplify-owner-rooms>
                </el-tab-pane>

                <el-tab-pane v-if="parkingSpaceCreateFeeInfo.ownerId" :label="$t('parkingSpaceCreateFee.owner')"
                  name="carDetailOwner">
                  <car-detail-owner v-if="parkingSpaceCreateFeeInfo._currentTab === 'carDetailOwner'"
                    ref="carDetailOwner"></car-detail-owner>
                </el-tab-pane>

                <el-tab-pane v-if="hasPrivilege('502020092373407363')" :label="$t('parkingSpaceCreateFee.reprintReceipt')"
                  name="simplifyFeeReceipt">
                  <simplify-fee-receipt v-if="parkingSpaceCreateFeeInfo._currentTab === 'simplifyFeeReceipt'"
                    ref="simplifyFeeReceipt"></simplify-fee-receipt>
                </el-tab-pane>
              </el-tabs>
            </div>
          </el-card>
        </div>
      </template>
    </div>

    <!-- 模态框组件 -->
    <delete-fee ref="deleteFee" @success="handleSuccess" />
    <edit-fee ref="editFee" @success="handleSuccess" />
    <split-fee ref="splitFee" @success="handleSuccess" />
    <export-parking-space-fee-import-excel ref="exportParkingSpaceFeeImportExcel" />
    <do-import-create-fee ref="doImportCreateFee" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'

import ParkingSpaceTreeHasOwner from '@/components/car/parkingSpaceTreeHasOwner'
import SimplifyParkingSpaceFee from '@/components/simplify/simplifyParkingSpaceFee'
import SimplifyHisFee from '@/components/fee/simplifyHisFee'
import PrePayFeeDetail from '@/components/fee/prePayFeeDetail'
import SimplifyOwnerRooms from '@/components/fee/simplifyOwnerRooms'
import CarDetailOwner from '@/components/fee/carDetailOwner'
import SimplifyFeeReceipt from '@/components/fee/simplifyFeeReceipt'
import FeeDetailParkingSpaceFeeRule from '@/components/fee/feeDetailParkingSpaceFeeRule'
import ParkingSpaceCarDetail from '@/components/fee/parkingSpaceCarDetail'
import DeleteFee from '@/components/fee/deleteFee'
import EditFee from '@/components/fee/editFee'
import SplitFee from '@/components/fee/splitFee'

import ExportParkingSpaceFeeImportExcel from '@/components/fee/exportParkingSpaceFeeImportExcel'
import DoImportCreateFee from '@/components/fee/doImportCreateFee'

export default {
  name: 'ParkingSpaceCreateFeeList',
  components: {
    ParkingSpaceTreeHasOwner,
    SimplifyParkingSpaceFee,
    SimplifyHisFee,
    PrePayFeeDetail,
    SimplifyOwnerRooms,
    CarDetailOwner,
    SimplifyFeeReceipt,
    FeeDetailParkingSpaceFeeRule,
    ParkingSpaceCarDetail,
    DeleteFee,
    EditFee,
    SplitFee,
    ExportParkingSpaceFeeImportExcel,
    DoImportCreateFee
  },
  data() {
    return {
      communityId: '',
      isTreeEmpty: false,
      // 拖拽相关数据
      treeWidth: 250,
      minTreeWidth: 200,
      maxTreeWidth: 600,
      isResizing: false,
      startX: 0,
      startWidth: 0,
      parkingSpaceCreateFeeInfo: {
        psId: '',
        psName: '',
        paId: '',
        areaNum: '',
        sectionId: '',
        sectionName: '',
        num: '',
        ownerId: '',
        ownerName: '',
        carId: '',
        carNum: '',
        startTime: '',
        endTime: '',
        _currentTab: 'simplifyParkingSpaceFee'
      }
    }
  },
  async created() {
    this.communityId = await getCommunityId()
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
    handleTreeEmpty() {
      this.isTreeEmpty = true
    },
    handleSelectParkingSpace(param) {
      this.parkingSpaceCreateFeeInfo.psId = param.psId
      this.parkingSpaceCreateFeeInfo.psName = param.psName
      this.parkingSpaceCreateFeeInfo.paId = param.paId
      this.parkingSpaceCreateFeeInfo.areaNum = param.areaNum
      this.parkingSpaceCreateFeeInfo.sectionId = param.sectionId
      this.parkingSpaceCreateFeeInfo.sectionName = param.sectionName
      this.parkingSpaceCreateFeeInfo.num = param.num
      this.parkingSpaceCreateFeeInfo.ownerId = param.ownerId
      this.parkingSpaceCreateFeeInfo.ownerName = param.ownerName
      this.parkingSpaceCreateFeeInfo.carId = param.carId
      this.parkingSpaceCreateFeeInfo.carNum = param.carNum
      this.parkingSpaceCreateFeeInfo.startTime = param.startTime
      this.parkingSpaceCreateFeeInfo.endTime = param.endTime
      this.changeTab(this.parkingSpaceCreateFeeInfo._currentTab)
    },
    changeTab(tabName) {
      this.parkingSpaceCreateFeeInfo._currentTab = tabName
      setTimeout(() => {
        if (this.$refs[tabName]) {
          this.$refs[tabName].open({
            psId: this.parkingSpaceCreateFeeInfo.psId,
            psName: this.parkingSpaceCreateFeeInfo.psName,
            paId: this.parkingSpaceCreateFeeInfo.paId,
            sectionId: this.parkingSpaceCreateFeeInfo.sectionId,
            num: this.parkingSpaceCreateFeeInfo.num,
            ownerId: this.parkingSpaceCreateFeeInfo.ownerId,
            ownerName: this.parkingSpaceCreateFeeInfo.ownerName,
            carId: this.parkingSpaceCreateFeeInfo.carId,
            carNum: this.parkingSpaceCreateFeeInfo.carNum,
            startTime: this.parkingSpaceCreateFeeInfo.startTime,
            endTime: this.parkingSpaceCreateFeeInfo.endTime,
            payerObjId: this.parkingSpaceCreateFeeInfo.psId,
            payerObjType: '6666'
          })
        }
      }, 500)
    },
    _openFeeImportExcel() {
      this.$refs.exportParkingSpaceFeeImportExcel.open()
    },
    _openDoCreateRoomFee() {
      this.$refs.doImportCreateFee.open()
    },
    handleSuccess() {
      // 处理成功回调
    },
    restoreTreeWidth() {
      const savedWidth = localStorage.getItem('parkingSpaceCreateFeeTreeWidth')
      if (savedWidth) {
        const width = parseInt(savedWidth)
        if (width >= this.minTreeWidth && width <= this.maxTreeWidth) {
          this.treeWidth = width
        }
      }
    },
    saveTreeWidth() {
      localStorage.setItem('parkingSpaceCreateFeeTreeWidth', this.treeWidth.toString())
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

<style lang="scss" scoped>
.parking-space-create-fee-container {
  padding: 20px;
  height: calc(100vh - 120px);
  overflow: hidden;

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

  .tree-panel * {
    user-select: none;
  }

  .resizable-layout.resizing * {
    user-select: none !important;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .header-tools {
      .el-button {
        margin-left: 10px;
      }
    }
  }

  .tab-section {
    margin-top: 20px;
  }
}
</style>
