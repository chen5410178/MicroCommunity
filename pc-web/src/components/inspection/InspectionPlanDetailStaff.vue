<template>
  <div class="inspection-plan-detail-staff">
    <el-table v-loading="loading" :data="inspectionPlanDetailStaffInfo.staffs" border style="width: 100%">
      <el-table-column prop="staffName" :label="$t('inspectionPlanDetail.staffName')" align="center" />
      <el-table-column prop="startTime" :label="$t('inspectionPlanDetail.startTime')" align="center" />
      <el-table-column prop="endTime" :label="$t('inspectionPlanDetail.endTime')" align="center" />
    </el-table>

    <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
      :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listInspectionPlanStaffs } from '@/api/inspection/inspectionPlanDetailApi'

export default {
  name: 'InspectionPlanDetailStaff',
  data() {
    return {
      inspectionPlanDetailStaffInfo: {
        staffs: [],
        inspectionPlanId: '',
        planUserName: '',
        inspectionStartTime: '',
        inspectionEndTime: ''
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
    open(params) {
      this.inspectionPlanDetailStaffInfo.inspectionPlanId = params.inspectionPlanId
      this.loadInspectionPlanDetailStaffData()
    },
    async switch(params) {
      this.inspectionPlanDetailStaffInfo.inspectionPlanId = params.inspectionPlanId
      this.loadInspectionPlanDetailStaffData()
    },
    async loadInspectionPlanDetailStaffData() {
      try {
        this.loading = true
        const params = {
          communityId: this.communityId,
          inspectionPlanId: this.inspectionPlanDetailStaffInfo.inspectionPlanId,
          page: this.page.current,
          row: this.page.size
        }
        const { inspectionPlanStaffs, total } = await listInspectionPlanStaffs(params)
        this.inspectionPlanDetailStaffInfo.staffs = inspectionPlanStaffs
        this.page.total = total
      } catch (error) {
        console.error('获取巡检人员列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadInspectionPlanDetailStaffData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadInspectionPlanDetailStaffData()
    }
  }
}
</script>

<style lang="scss" scoped>
.inspection-plan-detail-staff {
  padding: 20px;

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>