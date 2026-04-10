<template>
  <div class="admin-repair-container padding">
    <div class="flex-container">
      <div class="tree-container">
        <community-repair-tree ref="communityRepairTree" @selectCommunity="handleSelectCommunity" 
          @selectRepairSetting="handleSelectRepairSetting" @selectState="handleSelectState" />
      </div>
      <div class="content-container">
        <el-card class="search-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminRepair.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input v-model.trim="searchForm.repairId" :placeholder="$t('adminRepair.search.repairId')" clearable />
            </el-col>
            <el-col :span="6">
              <el-input v-model.trim="searchForm.repairName" :placeholder="$t('adminRepair.search.repairName')" clearable />
            </el-col>
            <el-col :span="6">
              <el-input v-model.trim="searchForm.tel" :placeholder="$t('adminRepair.search.tel')" clearable />
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="table-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminRepair.list.title') }}</span>
          </div>
          <el-table :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="repairId" :label="$t('adminRepair.table.repairId')" align="center" />
            <el-table-column prop="communityName" :label="$t('adminRepair.table.communityName')" align="center" />
            <el-table-column prop="repairObjName" :label="$t('adminRepair.table.repairObjName')" align="center" />
            <el-table-column prop="repairTypeName" :label="$t('adminRepair.table.repairTypeName')" align="center" />
            <el-table-column :label="$t('adminRepair.table.maintenanceType')" align="center">
              <template slot-scope="scope">
                {{ getMaintenanceTypeName(scope.row.maintenanceType) }}
              </template>
            </el-table-column>
            <el-table-column prop="repairName" :label="$t('adminRepair.table.repairName')" align="center" />
            <el-table-column prop="tel" :label="$t('adminRepair.table.tel')" align="center" />
            <el-table-column :label="$t('adminRepair.table.appointmentTime')" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.appointmentTime }}</div>
                <div>~{{ scope.row.timeout }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('adminRepair.table.createTime')" align="center" />
            <el-table-column prop="submitHours" :label="$t('adminRepair.table.submitHours')" align="center">
              <template slot-scope="scope">
                {{ scope.row.submitHours || '0' }}
              </template>
            </el-table-column>
            <el-table-column prop="finishTime" :label="$t('adminRepair.table.finishTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.finishTime || '-' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminRepair.table.state')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.state === '1800' && (scope.row.returnVisitFlag === '001' || scope.row.returnVisitFlag === '002')">
                  {{ scope.row.stateName }}({{ $t('adminRepair.table.timedTask') }})
                </span>
                <span v-else>
                  {{ scope.row.stateName }}
                </span>
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
          <el-pagination
            :current-page="pagination.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handlePageChange"
          />
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { listAdminOwnerRepairs } from '@/api/work/adminRepairApi'
import CommunityRepairTree from '@/components/work/CommunityRepairTree'

export default {
  name: 'AdminRepairList',
  components: {
    CommunityRepairTree
  },
  data() {
    return {
      loading: false,
      searchForm: {
        communityId: '',
        repairId: '',
        state: '',
        repairType: '',
        repairName: '',
        tel: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await listAdminOwnerRepairs(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('adminRepair.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleSelectCommunity({ communityId }) {
      this.searchForm.communityId = communityId
      this.searchForm.repairType = ''
      this.searchForm.state = ''
      this.getList()
    },
    handleSelectRepairSetting({ communityId, repairType }) {
      this.searchForm.communityId = communityId
      this.searchForm.repairType = repairType
      this.searchForm.state = ''
      this.getList()
    },
    handleSelectState({ communityId, repairType, state }) {
      this.searchForm.communityId = communityId
      this.searchForm.repairType = repairType
      this.searchForm.state = state
      this.getList()
    },
    handleDetail(row) {
      this.$router.push(`/views/work/adminRepairDetail?repairId=${row.repairId}`)
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.getList()
    },
    handlePageChange(current) {
      this.pagination.current = current
      this.getList()
    },
    getMaintenanceTypeName(type) {
      switch (type) {
        case '1001': return this.$t('adminRepair.maintenanceType.paid')
        case '1002': return this.$t('adminRepair.maintenanceType.free')
        case '1003': return this.$t('adminRepair.maintenanceType.withMaterial')
        case '1004': return this.$t('adminRepair.maintenanceType.withoutMaterial')
        default: return '--'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-repair-container {
  padding: 20px;

  .flex-container {
    display: flex;

    .tree-container {
      width: 250px;
      margin-right: 20px;
    }

    .content-container {
      flex: 1;
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>