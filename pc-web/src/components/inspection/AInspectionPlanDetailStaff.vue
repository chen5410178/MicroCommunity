<template>
  <div class="staff-container">
    <el-table :data="staffs" border style="width: 100%" v-loading="loading">
      <el-table-column prop="staffName" :label="$t('aInspectionPlanDetail.staffName')" align="center" />
      <el-table-column prop="startTime" :label="$t('aInspectionPlanDetail.startTime')" align="center" />
      <el-table-column prop="endTime" :label="$t('aInspectionPlanDetail.endTime')" align="center" />
    </el-table>

    <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
      :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { listAdminInspectionPlanStaffs } from '@/api/inspection/aInspectionPlanDetailApi'

export default {
  name: 'AInspectionPlanDetailStaff',
  data() {
    return {
      loading: false,
      staffs: [],
      inspectionPlanId: '',
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    async loadData(params) {
      this.inspectionPlanId = params.inspectionPlanId
      await this.getStaffList()
    },
    async getStaffList() {
      try {
        this.loading = true
        const params = {
          inspectionPlanId: this.inspectionPlanId,
          page: this.page.current,
          row: this.page.size
        }
        const { inspectionPlanStaffs, total } = await listAdminInspectionPlanStaffs(params)
        this.staffs = inspectionPlanStaffs
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getStaffList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getStaffList()
    }
  }
}
</script>

<style scoped>
.staff-container {
  padding: 20px;
}
</style>