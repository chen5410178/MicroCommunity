<template>
  <div class="admin-charge-order-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <select-admin-community @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="20">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('adminChargeOrder.search.title') }}</span>
          </div>
          <el-form :inline="true" :model="searchForm" class="search-form text-left">
            <el-form-item :label="$t('adminChargeOrder.search.orderId')">
              <el-input v-model="searchForm.orderId" :placeholder="$t('adminChargeOrder.search.orderIdPlaceholder')"
                clearable />
            </el-form-item>
            <el-form-item :label="$t('adminChargeOrder.search.personName')">
              <el-input v-model="searchForm.personName" :placeholder="$t('adminChargeOrder.search.personNamePlaceholder')"
                clearable />
            </el-form-item>
            <el-form-item :label="$t('adminChargeOrder.search.personTel')">
              <el-input v-model="searchForm.personTel" :placeholder="$t('adminChargeOrder.search.personTelPlaceholder')"
                clearable />
            </el-form-item>
            <el-form-item :label="$t('adminChargeOrder.search.machineName')">
              <el-input v-model="searchForm.machineName"
                :placeholder="$t('adminChargeOrder.search.machineNamePlaceholder')" clearable />
            </el-form-item>
            <el-form-item :label="$t('adminChargeOrder.search.portName')">
              <el-input v-model="searchForm.portName" :placeholder="$t('adminChargeOrder.search.portNamePlaceholder')"
                clearable />
            </el-form-item>
           

            <el-form-item :label="$t('adminChargeOrder.search.state')">
              <el-select v-model="searchForm.state" :placeholder="$t('adminChargeOrder.search.statePlaceholder')"
                clearable>
                <el-option v-for="item in stateOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('adminChargeOrder.search.startTime')">
              <el-date-picker v-model="searchForm.queryStartTime" type="datetime"
                :placeholder="$t('adminChargeOrder.search.startTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-form-item>
            <el-form-item :label="$t('adminChargeOrder.search.endTime')">
              <el-date-picker v-model="searchForm.queryEndTime" type="datetime"
                :placeholder="$t('adminChargeOrder.search.endTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="table-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminChargeOrder.table.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="orderId" :label="$t('adminChargeOrder.table.orderId')" align="center" />
            <el-table-column prop="personName" :label="$t('adminChargeOrder.table.personName')" align="center" />
            <el-table-column prop="personTel" :label="$t('adminChargeOrder.table.personTel')" align="center" />
            <el-table-column prop="machineName" :label="$t('adminChargeOrder.table.machineName')" align="center" />
            <el-table-column prop="portName" :label="$t('adminChargeOrder.table.portName')" align="center" />
            <el-table-column :label="$t('adminChargeOrder.table.chargeHours')" align="center">
              <template slot-scope="scope">
                {{ scope.row.chargeHours === 999 ? $t('adminChargeOrder.table.byQuantity') :
                  `${scope.row.chargeHours}${$t('adminChargeOrder.table.hours')}` }}
              </template>
            </el-table-column>
            <el-table-column prop="energy" :label="$t('adminChargeOrder.table.energy')" align="center" />
            <el-table-column prop="startTime" :label="$t('adminChargeOrder.table.startTime')" align="center" />
            <el-table-column prop="endTime" :label="$t('adminChargeOrder.table.endTime')" align="center" />
            <el-table-column prop="acctDetailId" :label="$t('adminChargeOrder.table.acctDetailId')" align="center"
              class-name="pointer" />
            <el-table-column prop="durationPrice" :label="$t('adminChargeOrder.table.durationPrice')" align="center" />
            <el-table-column prop="servicePrice" :label="$t('adminChargeOrder.table.servicePrice')" align="center" />
            <el-table-column prop="amount" :label="$t('adminChargeOrder.table.amount')" align="center" />
            <el-table-column prop="stateName" :label="$t('adminChargeOrder.table.stateName')" align="center" />
            <el-table-column :label="$t('adminChargeOrder.table.remark')" align="center">
              <template slot-scope="scope">
                <div style="width: 100px;">{{ scope.row.remark || '-' }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('adminChargeOrder.table.createTime')" align="center" />
          </el-table>
          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminChargeMachineOrder } from '@/api/iot/adminChargeOrderApi'
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'

export default {
  name: 'AdminChargeOrderList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      loading: false,
      searchForm: {
        orderId: '',
        personName: '',
        personTel: '',
        machineName: '',
        portName: '',
        communityId: '',
        state: '',
        queryStartTime: '',
        queryEndTime: '',
        chargeType: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      stateOptions: [
        { value: '', label: this.$t('adminChargeOrder.state.all') },
        { value: '1001', label: this.$t('adminChargeOrder.state.charging') },
        { value: '2002', label: this.$t('adminChargeOrder.state.completed') },
        { value: '3003', label: this.$t('adminChargeOrder.state.failed') }
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
        const { data, total } = await listAdminChargeMachineOrder(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('adminChargeOrder.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
      this.getList()
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
.admin-charge-order-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;

    .search-form {
      margin-bottom: -18px;
    }
  }

  .table-card {
    margin-bottom: 20px;
  }

  .pointer {
    cursor: pointer;
  }
}
</style>