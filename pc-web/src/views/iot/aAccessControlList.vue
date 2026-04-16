<template>
  <div class="access-control-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <select-admin-community @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('aAccessControl.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input v-model="searchForm.machineCode" :placeholder="$t('aAccessControl.search.machineCode')"
                clearable />
            </el-col>
            <el-col :span="6">
              <el-input v-model="searchForm.machineName" :placeholder="$t('aAccessControl.search.machineName')"
                clearable />
            </el-col>
            <el-col :span="6">
              <el-input v-model="searchForm.locationName" :placeholder="$t('aAccessControl.search.locationName')"
                clearable />
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="handleSearch">
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card" style="margin-top: 20px;">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('aAccessControl.list.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="communityName" :label="$t('aAccessControl.table.communityName')" align="center" />
            <el-table-column prop="machineCode" :label="$t('aAccessControl.table.machineCode')" align="center" />
            <el-table-column prop="machineName" :label="$t('aAccessControl.table.machineName')" align="center" />
            <el-table-column prop="machineIp" :label="$t('aAccessControl.table.machineIp')" align="center">
              <template slot-scope="scope">
                {{ scope.row.machineIp || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="machineMac" :label="$t('aAccessControl.table.machineMac')" align="center">
              <template slot-scope="scope">
                {{ scope.row.machineMac || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="promptSwitch" :label="$t('aAccessControl.table.promptSwitch')" align="center">
              <template slot-scope="scope">
                {{ scope.row.promptSwitch === 'ON' ? $t('common.yes') : $t('common.no') }}
              </template>
            </el-table-column>
            <el-table-column prop="direction" :label="$t('aAccessControl.table.direction')" align="center">
              <template slot-scope="scope">
                {{ scope.row.direction === '3306' ? $t('aAccessControl.direction.in') :
                  $t('aAccessControl.direction.out')
                }}
              </template>
            </el-table-column>
            <el-table-column prop="implBean" :label="$t('aAccessControl.table.implBean')" align="center" />
            <el-table-column prop="locationName" :label="$t('aAccessControl.table.locationName')" align="center" />
            <el-table-column prop="stateName" :label="$t('aAccessControl.table.stateName')" align="center" />
            <el-table-column prop="heartbeatTime" :label="$t('aAccessControl.table.heartbeatTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="150">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleDetail(scope.row)">
                  {{ $t('common.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminAccessControl } from '@/api/iot/aAccessControlApi'
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import { jumpToIot } from '@/api/user/menuApi'

export default {
  name: 'AAccessControlList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      loading: false,
      searchForm: {
        machineCode: '',
        machineName: '',
        locationName: '',
        communityId: ''
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
        const { data, total } = await listAdminAccessControl(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('aAccessControl.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
      this.handleSearch()
    },
    handleDetail(row) {
      jumpToIot('/#/pages/accessControl/aAccessControlDetail?machineId=' + row.machineId)
    }
  }
}
</script>

<style lang="scss" scoped>
.access-control-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>