<template>
  <div class="animated fadeInRight padding">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <div>{{ $t('inspectionPlan.queryCondition') }}</div>
      </div>
      <div class="card-content">
        <el-row :gutter="20">
          <el-col :span="5">
            <el-input :placeholder="$t('inspectionPlan.inspectionPlanIdPlaceholder')"
              v-model="searchConditions.inspectionPlanId" clearable />
          </el-col>
          <el-col :span="5">
            <el-input :placeholder="$t('inspectionPlan.inspectionPlanNamePlaceholder')"
              v-model="searchConditions.inspectionPlanName" clearable />
          </el-col>
          <el-col :span="5">
            <el-input :placeholder="$t('inspectionPlan.staffNamePlaceholder')" v-model="searchConditions.staffNameLike"
              clearable />
          </el-col>
          <el-col :span="5">
            <el-select v-model="searchConditions.state" :placeholder="$t('inspectionPlan.statePlaceholder')"
              style="width:100%">
              <el-option v-for="(item, index) in stateOptions" :key="index" :label="item.name" :value="item.statusCd" />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="fetchInspectionPlans">
              {{ $t('inspectionPlan.query') }}
            </el-button>
            <el-button @click="resetSearch">
              {{ $t('inspectionPlan.reset') }}
            </el-button>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <el-card class="box-card margin-top">
      <div slot="header" class="flex justify-between">
        <div>{{ $t('inspectionPlan.inspectionPlanTitle') }}</div>
        <div style="">
          <el-button type="primary" size="small" @click="openAddModal">
            {{ $t('inspectionPlan.add') }}
          </el-button>
        </div>
      </div>
      <el-table :data="planList" border style="width: 100%">
        <el-table-column prop="inspectionPlanName" :label="$t('inspectionPlan.planName')" align="center" />
        <el-table-column prop="inspectionRouteName" :label="$t('inspectionPlan.planRoute')" align="center" />
        <el-table-column prop="inspectionPlanPeriodName" :label="$t('inspectionPlan.planCycle')" align="center" />
        <el-table-column prop="signTypeName" :label="$t('inspectionPlan.signMethod')" align="center" />
        <el-table-column :label="$t('inspectionPlan.dateRange')" align="center">
          <template slot-scope="scope">
            {{ scope.row.startDate }}~{{ scope.row.endDate }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('inspectionPlan.timeRange')" align="center">
          <template slot-scope="scope">
            {{ scope.row.startTime }}~{{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="beforeTime" :label="$t('inspectionPlan.taskAdvance')" align="center" />
        <el-table-column prop="createUserName" :label="$t('inspectionPlan.maker')" align="center" />
        <el-table-column prop="createTime" :label="$t('inspectionPlan.createTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('inspectionPlan.status')" align="center" />
        <el-table-column :label="$t('inspectionPlan.inspectionStaff')" align="center">
          <template slot-scope="scope">
            <div v-for="(staff, i) in scope.row.staffs" :key="i">
              {{ staff.staffName }}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('inspectionPlan.operation')" align="center" width="280">
          <template slot-scope="scope">
            <el-button-group>
              <el-button size="mini" @click="openEditModal(scope.row)">
                {{ $t('inspectionPlan.modify') }}
              </el-button>
              <el-button size="mini" @click="openDeleteModal(scope.row)">
                {{ $t('inspectionPlan.delete') }}
              </el-button>
              <el-button v-if="scope.row.state === '2020025'" size="mini"
                @click="openStateModal(scope.row, '2020026', $t('inspectionPlan.disable'))">
                {{ $t('inspectionPlan.disable') }}
              </el-button>
              <el-button v-else size="mini" @click="openStateModal(scope.row, '2020025', $t('inspectionPlan.enable'))">
                {{ $t('inspectionPlan.enable') }}
              </el-button>
              <el-button size="mini" @click="viewDetail(scope.row)">
                {{ $t('inspectionPlan.detail') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="18" class="text-left table-desc">
          <div>{{ $t('inspectionPlan.note') }}</div>
        </el-col>
        <el-col :span="6">
          <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" layout="total, sizes, prev, pager, next" :total="pagination.total"
            @size-change="handleSizeChange" @current-change="handlePageChange" />
        </el-col>
      </el-row>
    </el-card>

    <edit-inspection-plan ref="editModal" @success="handleSuccess" />
    <delete-inspection-plan ref="deleteModal" @success="handleSuccess" />
    <inspection-plan-state ref="stateModal" @success="handleSuccess" />
  </div>
</template>

<script>
import { listInspectionPlans } from '@/api/inspection/inspectionPlanApi'
import EditInspectionPlan from '@/components/inspection/editInspectionPlan'
import DeleteInspectionPlan from '@/components/inspection/deleteInspectionPlan'
import InspectionPlanState from '@/components/inspection/inspectionPlanState'
import { getDict } from '@/api/community/communityApi'
export default {
  name: 'InspectionPlanList',
  components: {
    EditInspectionPlan,
    DeleteInspectionPlan,
    InspectionPlanState
  },
  data() {
    return {
      searchConditions: {
        inspectionPlanId: '',
        inspectionPlanName: '',
        staffNameLike: '',
        state: ''
      },
      planList: [],
      stateOptions: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  async created() {
    await this.loadDictData()
    this.fetchInspectionPlans()
  },
  methods: {
    async loadDictData() {
      try {
        this.stateOptions = await getDict('inspection_plan', 'state')
      } catch (error) {
        console.error('Failed to load dictionary data:', error)
      }
    },

    async fetchInspectionPlans() {
      try {
        const params = {
          ...this.searchConditions,
          page: this.pagination.current,
          row: this.pagination.size
        }

        const { data, total } = await listInspectionPlans(params)
        this.planList = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('inspectionPlan.fetchError'))
      }
    },

    resetSearch() {
      this.searchConditions = {
        inspectionPlanId: '',
        inspectionPlanName: '',
        staffNameLike: '',
        state: ''
      }
      this.pagination.current = 1
      this.fetchInspectionPlans()
    },

    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchInspectionPlans()
    },

    handlePageChange(page) {
      this.pagination.current = page
      this.fetchInspectionPlans()
    },

    openAddModal() {
      this.$router.push({ path: '/views/inspection/addInspectionPlan' })
    },

    openEditModal(plan) {
      this.$refs.editModal.open(plan)
    },

    openDeleteModal(plan) {
      this.$refs.deleteModal.open(plan)
    },

    openStateModal(plan, state, stateName) {
      this.$refs.stateModal.open({
        inspectionPlanId: plan.inspectionPlanId,
        state,
        stateName
      })
    },

    viewDetail(plan) {
      window.open(`/#/pages/inspection/inspectionPlanDetail?inspectionPlanId=${plan.inspectionPlanId}`)
    },

    handleSuccess() {
      this.fetchInspectionPlans()
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.card-content {
  padding: 0px;
}
</style>