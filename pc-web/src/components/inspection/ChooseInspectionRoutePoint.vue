<template>
  <el-dialog :title="$t('inspectionRoute.choosePointTitle')" :visible.sync="visible" width="70%" @close="handleClose">
    <el-row :gutter="20">
      <el-col :span="24">

          <el-row :gutter="20" class="margin-bottom">
            <el-col :span="18"></el-col>
            <el-col :span="6">
              <el-input v-model="chooseInspectionRoutePointInfo.inspectionName"
                :placeholder="$t('inspectionRoute.pointNamePlaceholder')" clearable class="search-input">
                <el-button slot="append" type="primary" @click="queryPoints">
                  <i class="el-icon-search"></i>
                </el-button>
                <el-button slot="append" type="primary" @click="resetPoints">
                  <i class="el-icon-refresh"></i>
                </el-button>
              </el-input>
            </el-col>
          </el-row>

          <el-table :data="chooseInspectionRoutePointInfo.points" border style="width: 100%"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column prop="inspectionId" :label="$t('inspectionRoute.pointId')" align="center" />
            <el-table-column prop="inspectionName" :label="$t('inspectionRoute.pointName')" align="center" />
          </el-table>

          <el-pagination :current-page.sync="page.current" :page-size="page.size" :total="page.total"
            layout="total, prev, pager, next" @current-change="handlePageChange" />

          <div class="text-right margin-top">
            <el-button @click="handleClose">
              {{ $t('common.cancel') }}
            </el-button>
            <el-button type="primary" @click="_chooseInspectionRoutePoint">
              {{ $t('common.confirm') }}
            </el-button>
          </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listInspectionPoints, saveInspectionRoutePoint } from '@/api/inspection/inspectionRouteApi'

export default {
  name: 'ChooseInspectionRoutePoint',
  data() {
    return {
      visible: false,
      chooseInspectionRoutePointInfo: {
        points: [],
        inspectionName: '',
        inspectionRouteId: '',
        selectPoints: []
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: '',
      multipleSelection: []
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.visible = true
      this.chooseInspectionRoutePointInfo.inspectionRouteId = params.inspectionRouteId
      this._loadAllPointsInfo(1, 10)
    },
    async _loadAllPointsInfo(page, row) {
      try {
        const params = {
          page,
          row,
          inspectionName: this.chooseInspectionRoutePointInfo.inspectionName,
          relationship: '0',
          inspectionRouteId: this.chooseInspectionRoutePointInfo.inspectionRouteId,
          communityId: this.communityId
        }
        const { inspectionPoints, total } = await listInspectionPoints(params)
        this.chooseInspectionRoutePointInfo.points = inspectionPoints
        this.page.total = total
      } catch (error) {
        console.error('获取巡检点列表失败:', error)
      }
    },
    async _chooseInspectionRoutePoint() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning(this.$t('inspectionRoute.choosePointWarning'))
        return
      }

      try {
        const params = {
          communityId: this.communityId,
          inspectionRouteId: this.chooseInspectionRoutePointInfo.inspectionRouteId,
          points: this.multipleSelection.map(item => ({
            inspectionId: item.inspectionId,
            inspectionName: item.inspectionName
          }))
        }
        await saveInspectionRoutePoint(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        console.error('添加巡检点失败:', error)
      }
    },
    queryPoints() {
      this._loadAllPointsInfo(1, 10)
    },
    resetPoints() {
      this.chooseInspectionRoutePointInfo.inspectionName = ''
      this._loadAllPointsInfo(1, 10)
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handlePageChange(currentPage) {
      this._loadAllPointsInfo(currentPage, 10)
    },
    handleClose() {
      this.visible = false
      this.chooseInspectionRoutePointInfo = {
        points: [],
        inspectionName: '',
        inspectionRouteId: '',
        selectPoints: []
      }
      this.multipleSelection = []
    }
  }
}
</script>

<style lang="scss" scoped>
.search-input {
  width: 100%;
}

.margin-bottom {
  margin-bottom: 20px;
}

.margin-top {
  margin-top: 20px;
}
</style>