<template>
  <div class="point-container">
    <el-table :data="points" border style="width: 100%" v-loading="loading">
      <el-table-column prop="inspectionId" :label="$t('aInspectionPlanDetail.pointId')" align="center" />
      <el-table-column prop="inspectionName" :label="$t('aInspectionPlanDetail.pointName')" align="center" />
      <el-table-column prop="pointTypeName" :label="$t('aInspectionPlanDetail.pointType')" align="center" />
      <el-table-column prop="pointObjName" :label="$t('aInspectionPlanDetail.pointLocation')" align="center" />
      <el-table-column :label="$t('aInspectionPlanDetail.startTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.pointStartTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('aInspectionPlanDetail.endTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.pointEndTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('aInspectionPlanDetail.sort')" align="center">
        <template slot-scope="scope">
          {{ scope.row.sortNumber || '-' }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
      :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { listAdminInspectionRoutePoints } from '@/api/inspection/aInspectionPlanDetailApi'

export default {
  name: 'AInspectionRoutePoint',
  data() {
    return {
      loading: false,
      points: [],
      inspectionRouteId: '',
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    async loadData(params) {
      this.inspectionRouteId = params.inspectionRouteId
      await this.getPointList()
    },
    async getPointList() {
      try {
        this.loading = true
        const params = {
          inspectionRouteId: this.inspectionRouteId,
          page: this.page.current,
          row: this.page.size
        }
        const { inspectionPoints, total } = await listAdminInspectionRoutePoints(params)
        this.points = inspectionPoints
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getPointList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getPointList()
    }
  }
}
</script>

<style scoped>
.point-container {
  padding: 20px;
}
</style>