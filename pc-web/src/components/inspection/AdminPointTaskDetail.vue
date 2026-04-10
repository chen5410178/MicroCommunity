<template>
  <div class="detail-container">
    <el-form :inline="true" class="search-form text-left">
      <el-form-item :label="$t('aInspectionPlanDetail.staffName')">
        <el-input v-model="searchForm.planUserName"  clearable />
      </el-form-item>
      <el-form-item :label="$t('aInspectionPlanDetail.startTime')">
        <el-date-picker v-model="searchForm.inspectionStartTime" type="datetime"
           value-format="yyyy-MM-dd HH:mm:ss" />
      </el-form-item>
      <el-form-item :label="$t('aInspectionPlanDetail.endTime')">
        <el-date-picker v-model="searchForm.inspectionEndTime" type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">
          {{ $t('common.search') }}
        </el-button>
      </el-form-item>
    </el-form>

    <el-table :data="details" border style="width: 100%" v-loading="loading">
      <el-table-column prop="taskDetailId" :label="$t('aInspectionPlanDetail.detailId')" align="center" width="120" />
      <el-table-column prop="inspectionName" :label="$t('aInspectionPlanDetail.pointName')" align="center" />
      <el-table-column prop="inspectionPlanName" :label="$t('aInspectionPlanDetail.planName')" align="center" />
      <el-table-column prop="routeName" :label="$t('aInspectionPlanDetail.route')" align="center" />
      <el-table-column :label="$t('aInspectionPlanDetail.staffTime')" align="center">
        <template slot-scope="scope">
          <div>{{ scope.row.planInsTime }}</div>
          <div>{{ scope.row.planEndTime }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('aInspectionPlanDetail.pointTime')" align="center">
        <template slot-scope="scope">
          <div>{{ scope.row.pointStartTime }}</div>
          <div>{{ scope.row.pointEndTime }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('aInspectionPlanDetail.actualTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.inspectionTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('aInspectionPlanDetail.signStatus')" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.inspectionState === '60000' ? 'success' : 'danger'">
            {{ scope.row.inspectionStateName || '-' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="planUserName" :label="$t('aInspectionPlanDetail.planStaff')" align="center" />
      <el-table-column prop="actUserName" :label="$t('aInspectionPlanDetail.actualStaff')" align="center">
        <template slot-scope="scope">
          {{ scope.row.actUserName || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="signTypeName" :label="$t('aInspectionPlanDetail.signType')" align="center" />
      <el-table-column prop="taskStateName" :label="$t('aInspectionPlanDetail.taskStatus')" align="center" />
      <el-table-column :label="$t('aInspectionPlanDetail.pointStatus')" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.state === '20200408' ? 'danger' : ''">
            {{ scope.row.stateName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" :label="$t('aInspectionPlanDetail.inspectionResult')" align="center">
        <template slot-scope="scope">
          {{ scope.row.description || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('aInspectionPlanDetail.photos')" align="center">
        <template slot-scope="scope">
          <el-image v-for="(photo, index) in scope.row.photos" :key="index"
            style="width: 60px; height: 60px; margin-right: 5px;" :src="photo.url" :preview-src-list="[photo.url]"
            fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('aInspectionPlanDetail.createTime')" align="center" />
    </el-table>

    <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
      :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { listAdminInspectionTaskDetails } from '@/api/inspection/aInspectionPlanDetailApi'

export default {
  name: 'AdminPointTaskDetail',
  data() {
    return {
      loading: false,
      details: [],
      taskId: '',
      inspectionRouteId: '',
      searchForm: {
        planUserName: '',
        inspectionStartTime: '',
        inspectionEndTime: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    async loadData(params) {
      this.taskId = params.taskId
      this.inspectionRouteId = params.inspectionRouteId
      await this.getDetailList()
    },
    async getDetailList() {
      try {
        this.loading = true
        const params = {
          taskId: this.taskId,
          inspectionRouteId: this.inspectionRouteId,
          planUserName: this.searchForm.planUserName,
          inspectionStartTime: this.searchForm.inspectionStartTime,
          inspectionEndTime: this.searchForm.inspectionEndTime,
          page: this.page.current,
          row: this.page.size
        }
        const { inspectionTaskDetails, total } = await listAdminInspectionTaskDetails(params)
        this.details = inspectionTaskDetails
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getDetailList()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getDetailList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getDetailList()
    }
  }
}
</script>

<style scoped>
.detail-container {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.el-image {
  cursor: pointer;
}
</style>