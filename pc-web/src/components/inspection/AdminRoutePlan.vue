<template>
    <div class="admin-route-plan">
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column
          prop="inspectionPlanName"
          :label="$t('adminRoutePlan.table.planName')"
          align="center"
        />
        <el-table-column
          prop="inspectionRouteName"
          :label="$t('adminRoutePlan.table.routeName')"
          align="center"
        />
        <el-table-column
          prop="inspectionPlanPeriodName"
          :label="$t('adminRoutePlan.table.planPeriod')"
          align="center"
        />
        <el-table-column
          prop="signTypeName"
          :label="$t('adminRoutePlan.table.signType')"
          align="center"
        />
        <el-table-column
          :label="$t('adminRoutePlan.table.dateRange')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.startDate }}~{{ scope.row.endDate }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('adminRoutePlan.table.timeRange')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.startTime }}~{{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column
          prop="beforeTime"
          :label="$t('adminRoutePlan.table.beforeTime')"
          align="center"
        />
        <el-table-column
          prop="createUserName"
          :label="$t('adminRoutePlan.table.creator')"
          align="center"
        />
        <el-table-column
          prop="createTime"
          :label="$t('adminRoutePlan.table.createTime')"
          align="center"
        />
        <el-table-column
          :label="$t('adminRoutePlan.table.inspectors')"
          align="center"
        >
          <template slot-scope="scope">
            <div v-for="(staff, index) in scope.row.staffs" :key="index">
              {{ staff.staffName }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="stateName"
          :label="$t('adminRoutePlan.table.status')"
          align="center"
        />
      </el-table>
  
      <el-pagination
        class="margin-top"
        :current-page.sync="pagination.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </template>
  
  <script>
  import { queryAdminRouteInspectionPlan } from '@/api/inspection/adminInspectionTaskDetailApi'
  
  export default {
    name: 'AdminRoutePlan',
    data() {
      return {
        loading: false,
        searchForm: {
          inspectionRouteId: '',
          inspectionPlanId: ''
        },
        tableData: [],
        pagination: {
          current: 1,
          size: 10,
          total: 0
        }
      }
    },
    methods: {
      initData(params) {
        console.log(params)
        this.searchForm.inspectionRouteId = params.inspectionRouteId
        this.searchForm.inspectionPlanId = params.inspectionPlanId
        this.loadData()
      },
      async loadData(_param) {
        try {
          this.loading = true
          const params = {
            ..._param,
            page: this.pagination.current,
            row: this.pagination.size
          }
          const { data, total } = await queryAdminRouteInspectionPlan(params)
          this.tableData = data
          this.pagination.total = total
        } catch (error) {
          this.$message.error(this.$t('common.fetchError'))
        } finally {
          this.loading = false
        }
      },
      handleSizeChange(val) {
        this.pagination.size = val
        this.loadData()
      },
      handleCurrentChange(val) {
        this.pagination.current = val
        this.loadData()
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .admin-route-plan {
    .margin-top {
      margin-top: 20px;
    }
  }
  </style>