<template>
  <div class="route-plan-container">
    <el-table :data="routePlanInfo.plans" border style="width: 100%">
      <el-table-column prop="inspectionPlanName" :label="$t('inspectionRoute.planName')" align="center" />
      <el-table-column prop="inspectionRouteName" :label="$t('inspectionRoute.planRoute')" align="center" />
      <el-table-column prop="inspectionPlanPeriodName" :label="$t('inspectionRoute.planCycle')" align="center" />
      <el-table-column prop="signTypeName" :label="$t('inspectionRoute.signType')" align="center" />
      <el-table-column :label="$t('inspectionRoute.dateRange')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startDate }}~{{ scope.row.endDate }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('inspectionRoute.timeRange')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime }}~{{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column prop="beforeTime" :label="$t('inspectionRoute.taskAdvance')" align="center" />
      <el-table-column prop="createUserName" :label="$t('inspectionRoute.creator')" align="center" />
      <el-table-column prop="createTime" :label="$t('inspectionRoute.createTime')" align="center" />
      <el-table-column :label="$t('inspectionRoute.inspector')" align="center">
        <template slot-scope="scope">
          <div v-for="(staff, i) in scope.row.staffs" :key="i">
            {{ staff.staffName }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('inspectionRoute.status')" align="center" />
    </el-table>

    <el-pagination :current-page.sync="page.current" :page-size="page.size" :total="page.total"
      layout="total, prev, pager, next" @current-change="handlePageChange" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryRouteInspectionPlan } from '@/api/inspection/inspectionRouteApi'

export default {
  name: 'RoutePlan',
  data() {
    return {
      routePlanInfo: {
        plans: [],
        inspectionRouteId: '',
        inspectionPlanId: ''
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
    loadData(params) {
      this.routePlanInfo.inspectionRouteId = params.inspectionRouteId
      this.routePlanInfo.inspectionPlanId = params.inspectionPlanId || ''
      this._loadRoutePlanData(1, 10)
    },
    handleSwitch(params) {
      this.routePlanInfo.inspectionRouteId = params.inspectionRouteId
      this.routePlanInfo.inspectionPlanId = params.inspectionPlanId || ''
      this._loadRoutePlanData(1, 10)
    },
    async _loadRoutePlanData(page = 1, size = 10) {
      try {
        const params = {
          communityId: this.communityId,
          inspectionRouteId: this.routePlanInfo.inspectionRouteId,
          inspectionPlanId: this.routePlanInfo.inspectionPlanId,
          page,
          row: size
        }
        const { data, total } = await queryRouteInspectionPlan(params)
        this.routePlanInfo.plans = data
        this.page.total = total
      } catch (error) {
        console.error('获取巡检计划失败:', error)
      }
    },
    handlePageChange(currentPage) {
      this._loadRoutePlanData(currentPage, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.route-plan-container {
  padding: 20px;
  height: 100%;

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>