<template>
  <div class="point-plan-container">
    <el-table 
      :data="pointPlanInfo.plans" 
      border 
      style="width: 100%; margin-top: 20px;"
      v-loading="loading"
    >
      <el-table-column prop="inspectionPlanName" :label="$t('pointPlan.table.planName')" align="center" />
      <el-table-column prop="inspectionRouteName" :label="$t('pointPlan.table.routeName')" align="center" />
      <el-table-column prop="inspectionPlanPeriodName" :label="$t('pointPlan.table.planPeriod')" align="center" />
      <el-table-column prop="signTypeName" :label="$t('pointPlan.table.signType')" align="center" />
      <el-table-column :label="$t('pointPlan.table.dateRange')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startDate }} ~ {{ scope.row.endDate }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('pointPlan.table.timeRange')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime }} ~ {{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column prop="beforeTime" :label="$t('pointPlan.table.beforeTime')" align="center" />
      <el-table-column prop="createUserName" :label="$t('pointPlan.table.creator')" align="center" />
      <el-table-column prop="createTime" :label="$t('pointPlan.table.createTime')" align="center" />
      <el-table-column :label="$t('pointPlan.table.staffs')" align="center">
        <template slot-scope="scope">
          <div v-for="(staff, index) in scope.row.staffs" :key="index">
            {{ staff.staffName }}
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('pointPlan.table.state')" align="center">
        <template slot-scope="scope">
          <el-tag :type="getStateTagType(scope.row.state)">
            {{ scope.row.stateName }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="margin-top"
      :current-page.sync="page.current"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="page.size"
      :total="page.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { queryPointInspectionPlan } from '@/api/inspection/inspectionPointApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'PointPlan',
  data() {
    return {
      pointPlanInfo: {
        plans: [],
        inspectionId: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      loading: false,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    loadData(point) {
      if (!point) return
      
      this.pointPlanInfo.inspectionId = point.inspectionId
      this._loadPointPlanData(this.page.current, this.page.size)
    },
    
    async _loadPointPlanData(page, size) {
      this.loading = true
      
      try {
        const params = {
          communityId: this.communityId,
          inspectionId: this.pointPlanInfo.inspectionId,
          page: page,
          row: size
        }
        
        const response = await queryPointInspectionPlan(params)
        this.pointPlanInfo.plans = response.data
        this.page.total = response.total
      } catch (error) {
        console.error('获取巡检计划失败:', error)
        this.$message.error(this.$t('pointPlan.fetchError'))
      } finally {
        this.loading = false
      }
    },
    
    getStateTagType(state) {
      // 根据状态返回不同的标签类型
      if (state === 'active') return 'success'
      if (state === 'inactive') return 'danger'
      if (state === 'pending') return 'warning'
      return 'info'
    },
    
    handleSizeChange(size) {
      this.page.size = size
      this._loadPointPlanData(this.page.current, size)
    },
    
    handleCurrentChange(page) {
      this.page.current = page
      this._loadPointPlanData(page, this.page.size)
    }
  }
}
</script>

<style scoped>
.point-plan-container {
  padding: 0px;
}

.margin-top {
  margin-top: 20px;
}
</style>