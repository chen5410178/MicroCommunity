<template>
  <div class="inspection-route-point-container">
    <div class="text-right margin-bottom">
      <el-button type="primary" size="small" @click="_openAddInspectionRoutePointModal">
        {{ $t('common.add') }}
      </el-button>
    </div>

    <el-table :data="inspectionRoutePointInfo.inspectionPoints" border style="width: 100%">
      <el-table-column prop="inspectionId" :label="$t('inspectionRoute.pointId')" align="center" />
      <el-table-column prop="inspectionName" :label="$t('inspectionRoute.pointName')" align="center" />
      <el-table-column prop="pointTypeName" :label="$t('inspectionRoute.pointType')" align="center" />
      <el-table-column prop="pointObjName" :label="$t('inspectionRoute.pointLocation')" align="center" />
      <el-table-column :label="$t('inspectionRoute.startTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.pointStartTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('inspectionRoute.endTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.pointEndTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('inspectionRoute.sortNumber')" align="center">
        <template slot-scope="scope">
          {{ scope.row.sortNumber || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="_openEditInspectionRoutePointModel(scope.row)">
            {{ $t('common.edit') }}
          </el-button>
          <el-button size="mini" type="danger" @click="_openDeleteInspectionRoutePointModel(scope.row)">
            {{ $t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page.sync="page.current" :page-size="page.size" :total="page.total"
      layout="total, prev, pager, next" @current-change="handlePageChange" />

    <choose-inspection-route-point ref="chooseInspectionRoutePoint" @success="handleSuccess" />
    <edit-inspection-route-point ref="editInspectionRoutePoint" @success="handleSuccess" />
    <delete-inspection-route-point ref="deleteInspectionRoutePoint" @success="handleSuccess" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listInspectionRoutePoints } from '@/api/inspection/inspectionRouteApi'
import ChooseInspectionRoutePoint from './ChooseInspectionRoutePoint'
import EditInspectionRoutePoint from './EditInspectionRoutePoint'
import DeleteInspectionRoutePoint from './DeleteInspectionRoutePoint'

export default {
  name: 'InspectionRoutePoint',
  components: {
    ChooseInspectionRoutePoint,
    EditInspectionRoutePoint,
    DeleteInspectionRoutePoint
  },
  data() {
    return {
      inspectionRoutePointInfo: {
        inspectionPoints: [],
        inspectionRouteId: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  beforeDestroy() {
  },
  methods: {
    open(params) {
      this.inspectionRoutePointInfo.inspectionRouteId = params.inspectionRouteId
      this._listInspectionRoutePoints()
    },
    loadData(params) {
      this.inspectionRoutePointInfo.inspectionRouteId = params.inspectionRouteId
      this._listInspectionRoutePoints()
    },
    async _listInspectionRoutePoints() {
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId,
          inspectionRouteId: this.inspectionRoutePointInfo.inspectionRouteId
        }
        const { inspectionPoints, total } = await listInspectionRoutePoints(params)
        this.inspectionRoutePointInfo.inspectionPoints = inspectionPoints
        this.page.total = total
      } catch (error) {
        console.error('获取巡检点列表失败:', error)
      }
    },
    handleSwitch(params) {
      this.inspectionRoutePointInfo.inspectionRouteId = params.inspectionRouteId
      this._listInspectionRoutePoints()
    },
    _openAddInspectionRoutePointModal() {
      this.$refs.chooseInspectionRoutePoint.open({
        inspectionRouteId: this.inspectionRoutePointInfo.inspectionRouteId
      })
    },
    _openEditInspectionRoutePointModel(point) {
      this.$refs.editInspectionRoutePoint.open(point)
    },
    _openDeleteInspectionRoutePointModel(point) {
      this.$refs.deleteInspectionRoutePoint.open(point)
    },
    handlePageChange(currentPage) {
      this.page.current = currentPage
      this._listInspectionRoutePoints()
    },
    handleSuccess() {
      this._listInspectionRoutePoints()
    }
  }
}
</script>

<style lang="scss" scoped>
.inspection-route-point-container {
  padding: 10px;

  .margin-bottom {
    margin-bottom: 20px;
  }
}
</style>