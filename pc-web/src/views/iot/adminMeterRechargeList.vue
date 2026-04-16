<template>
  <div class="admin-meter-recharge-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <select-admin-community @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminMeterRecharge.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="searchForm.machineNameLike"
                :placeholder="$t('adminMeterRecharge.machineNamePlaceholder')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.addressLike" :placeholder="$t('adminMeterRecharge.addressPlaceholder')"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-select v-model="searchForm.state" :placeholder="$t('adminMeterRecharge.statePlaceholder')" clearable>
                <el-option v-for="item in stateOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.roomNameLike" :placeholder="$t('adminMeterRecharge.roomNamePlaceholder')"
                clearable />
            </el-col>
            <el-col :span="4" class="text-right">
              <el-button type="primary" @click="handleSearch">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="handleReset">
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 15px;">
            <el-col :span="4">
              <el-date-picker v-model="searchForm.queryStartTime" type="datetime"
                :placeholder="$t('adminMeterRecharge.startTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%;" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.queryEndTime" type="datetime"
                :placeholder="$t('adminMeterRecharge.endTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%;" />
            </el-col>
           
          </el-row>
        </el-card>

        <el-card class="box-card" style="margin-top: 20px;">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('adminMeterRecharge.list.title') }}</span>
            <el-button type="primary" size="mini" @click="handleSearch">
              {{ $t('common.refresh') }}
            </el-button>
          </div>

          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="communityName" :label="$t('adminMeterRecharge.table.communityName')" align="center" />
            <el-table-column prop="roomName" :label="$t('adminMeterRecharge.table.roomName')" align="center" />
            <el-table-column prop="typeName" :label="$t('adminMeterRecharge.table.typeName')" align="center" />
            <el-table-column prop="machineName" :label="$t('adminMeterRecharge.table.machineName')" align="center" />
            <el-table-column prop="address" :label="$t('adminMeterRecharge.table.address')" align="center" />
            <el-table-column prop="chargeMoney" :label="$t('adminMeterRecharge.table.chargeMoney')" align="center" />
            <el-table-column prop="chargeDegrees" :label="$t('adminMeterRecharge.table.chargeDegrees')" align="center" />
            <el-table-column prop="createTime" :label="$t('adminMeterRecharge.table.createTime')" align="center" />
            <el-table-column prop="state" :label="$t('adminMeterRecharge.table.state')" align="center">
              <template slot-scope="scope">
                <el-tag :type="getStateTagType(scope.row.state)" size="small">
                  {{ getStateText(scope.row.state) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" :label="$t('adminMeterRecharge.table.remark')" align="center">
              <template slot-scope="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" style="margin-top: 20px;" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import { listAdminMeterCharge } from '@/api/iot/adminMeterRechargeApi'

export default {
  name: 'AdminMeterRechargeList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      loading: false,
      searchForm: {
        machineNameLike: '',
        addressLike: '',
        state: '',
        roomNameLike: '',
        communityId: '',
        detailType: '1001',
        queryStartTime: '',
        queryEndTime: '',
        page: 1,
        row: 10
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      stateOptions: [
        { value: '', label: this.$t('adminMeterRecharge.statePlaceholder') },
        { value: 'W', label: this.$t('adminMeterRecharge.stateWaiting') },
        { value: 'F', label: this.$t('adminMeterRecharge.stateFailed') },
        { value: 'C', label: this.$t('adminMeterRecharge.stateCompleted') }
      ]
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

        const res = await listAdminMeterCharge(params)
        this.tableData = res.data
        this.pagination.total = res.total
      } catch (error) {
        this.$message.error(this.$t('adminMeterRecharge.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
      this.handleSearch()
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        machineNameLike: '',
        addressLike: '',
        state: '',
        roomNameLike: '',
        communityId: this.searchForm.communityId,
        detailType: '1001',
        queryStartTime: '',
        queryEndTime: '',
        page: 1,
        row: 10
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    getStateTagType(state) {
      switch (state) {
        case 'W': return 'warning'
        case 'F': return 'danger'
        case 'C': return 'success'
        default: return 'info'
      }
    },
    getStateText(state) {
      switch (state) {
        case 'W': return this.$t('adminMeterRecharge.stateWaiting')
        case 'F': return this.$t('adminMeterRecharge.stateFailed')
        case 'C': return this.$t('adminMeterRecharge.stateCompleted')
        default: return '-'
      }
    }
  }
}
</script>

<style scoped>
.admin-meter-recharge-container {
  padding: 20px;
}

.search-form {
  margin-bottom: 0;
}
</style>