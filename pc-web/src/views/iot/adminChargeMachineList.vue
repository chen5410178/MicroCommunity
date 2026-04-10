<template>
  <div class="admin-charge-machine-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <select-admin-community ref="selectCommunity" @changeCommunity="handleCommunityChange"></select-admin-community>
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('adminChargeMachine.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input v-model="searchForm.machineName" :placeholder="$t('adminChargeMachine.search.machineName')"
                clearable @keyup.enter.native="handleSearch">
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-input v-model="searchForm.machineCode" :placeholder="$t('adminChargeMachine.search.machineCode')"
                clearable @keyup.enter.native="handleSearch">
              </el-input>
            </el-col>
            <el-col :span="2">
              <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card" style="margin-top: 20px;">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('adminChargeMachine.list.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="communityName" :label="$t('adminChargeMachine.table.communityName')" align="center">
            </el-table-column>
            <el-table-column prop="machineName" :label="$t('adminChargeMachine.table.machineName')" align="center">
            </el-table-column>
            <el-table-column prop="machineCode" :label="$t('adminChargeMachine.table.machineCode')" align="center">
            </el-table-column>
            <el-table-column prop="factoryName" :label="$t('adminChargeMachine.table.factoryName')" align="center">
            </el-table-column>
            <el-table-column prop="portCount" :label="$t('adminChargeMachine.table.portCount')" align="center">
            </el-table-column>
            <el-table-column prop="ruleName" :label="$t('adminChargeMachine.table.ruleName')" align="center">
            </el-table-column>
            <el-table-column prop="chargeTypeName" :label="$t('adminChargeMachine.table.chargeTypeName')" align="center">
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('adminChargeMachine.table.stateName')" align="center">
            </el-table-column>
            <el-table-column prop="heartbeatTime" :label="$t('adminChargeMachine.table.heartbeatTime')" align="center">
            </el-table-column>
            <el-table-column prop="monitorName" :label="$t('adminChargeMachine.table.monitorName')" align="center">
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="150">
              <template slot-scope="scope">
                <el-button size="mini" @click="handleDetail(scope.row)">
                  {{ $t('common.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" style="margin-top: 20px;">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminChargeMachine } from '@/api/iot/adminChargeMachineApi'
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import { jumpToIot } from '@/api/user/menuApi'

export default {
  name: 'AdminChargeMachineList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      loading: false,
      searchForm: {
        machineName: '',
        machineCode: '',
        communityId: '',
        chargeType: ''
      },
      tableData: [],
      page: {
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
          page: this.page.current,
          row: this.page.size,
          ...this.searchForm
        }
        const { data, total } = await listAdminChargeMachine(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('adminChargeMachine.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
      this.getList()
    },
    handleDetail(row) {
      jumpToIot('/#/pages/charge/adminChargeDetail?machineId=' + row.machineId)
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-charge-machine-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .clearfix {
    font-weight: bold;
    font-size: 16px;
  }
}
</style>