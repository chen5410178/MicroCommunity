<template>
  <div class="admin-inspection-plan-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="3">
        <select-admin-community ref="selectCommunity" @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="21">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminInspectionPlan.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="searchForm.inspectionPlanId"
                :placeholder="$t('adminInspectionPlan.search.inspectionPlanId')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.adminInspectionPlanName"
                :placeholder="$t('adminInspectionPlan.search.inspectionPlanName')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.staffNameLike" :placeholder="$t('adminInspectionPlan.search.staffName')"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-select v-model="searchForm.state" :placeholder="$t('adminInspectionPlan.search.state')" clearable>
                <el-option v-for="item in stateOptions" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="4" class="text-right">
              <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
              <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
            </el-col>
          </el-row>
          
        
        </el-card>

        <el-card class="box-card mt-20">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminInspectionPlan.list.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="inspectionPlanName" :label="$t('adminInspectionPlan.table.planName')" align="center" />
            <el-table-column prop="communityName" :label="$t('adminInspectionPlan.table.communityName')" align="center" />
            <el-table-column prop="inspectionRouteName" :label="$t('adminInspectionPlan.table.inspectionRoute')"
              align="center" />
            <el-table-column prop="inspectionPlanPeriodName" :label="$t('adminInspectionPlan.table.planPeriod')"
              align="center" />
            <el-table-column prop="signTypeName" :label="$t('adminInspectionPlan.table.signType')" align="center" />
            <el-table-column :label="$t('adminInspectionPlan.table.dateRange')" align="center">
              <template slot-scope="scope">
                {{ scope.row.startDate }}~{{ scope.row.endDate }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminInspectionPlan.table.timeRange')" align="center">
              <template slot-scope="scope">
                {{ scope.row.startTime }}~{{ scope.row.endTime }}
              </template>
            </el-table-column>
            <el-table-column prop="beforeTime" :label="$t('adminInspectionPlan.table.beforeTime')" align="center" />
            <el-table-column prop="createUserName" :label="$t('adminInspectionPlan.table.creator')" align="center" />
            <el-table-column prop="createTime" :label="$t('adminInspectionPlan.table.createTime')" align="center" />
            <el-table-column prop="stateName" :label="$t('adminInspectionPlan.table.state')" align="center" />
            <el-table-column :label="$t('adminInspectionPlan.table.inspectionStaff')" align="center">
              <template slot-scope="scope">
                <div v-for="(staff, i) in scope.row.staffs" :key="i">{{ staff.staffName }}</div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleDetail(scope.row)">
                  {{ $t('common.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-row class="mt-20">
            <el-col :span="12">
              <div class="tip-text">
                {{ $t('adminInspectionPlan.tip.ensureValid') }}
              </div>
            </el-col>
            <el-col :span="12" class="text-right">
              <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
                :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange" @current-change="handleCurrentChange" />
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import { listAdminInspectionPlans } from '@/api/inspection/adminInspectionPlanApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'AdminInspectionPlanList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      loading: false,
      searchForm: {
        inspectionPlanId: '',
        adminInspectionPlanName: '',
        staffNameLike: '',
        state: '',
        communityId: ''
      },
      tableData: [],
      stateOptions: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getStateOptions()
    this.getList()
  },
  methods: {
    async getStateOptions() {
      try {
        const res = await getDict('inspection_plan', 'state')
        this.stateOptions = res.data
      } catch (error) {
        this.$message.error(this.$t('adminInspectionPlan.error.fetchStateFailed'))
      }
    },
    async getList() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const res = await listAdminInspectionPlans(params)
        this.tableData = res.data
        this.pagination.total = res.total
      } catch (error) {
        this.$message.error(this.$t('adminInspectionPlan.error.fetchListFailed'))
      } finally {
        this.loading = false
      }
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
      this.getList()
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        inspectionPlanId: '',
        adminInspectionPlanName: '',
        staffNameLike: '',
        state: '',
        communityId: this.searchForm.communityId
      }
      this.getList()
    },
    handleDetail(row) {
      window.open(`/#/pages/inspection/aInspectionPlanDetail?inspectionPlanId=${row.inspectionPlanId}`)
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-inspection-plan-container {
  padding: 20px;

  .mt-10 {
    margin-top: 10px;
  }

  .mt-20 {
    margin-top: 20px;
  }

  .text-right {
    text-align: right;
  }

  .tip-text {
    color: #999;
    font-size: 12px;
    line-height: 30px;
  }
}
</style>