<template>
  <div class="contract-change-audit-history-orders-container animated fadeInRight">
    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('contractChangeAuditHistoryOrders.title') }}</span>
          </div>
          <div class="table-wrapper">
            <el-table v-loading="loading" :data="tableData" border style="width: 100%">
              <el-table-column prop="contractCode" :label="$t('contractChangeAuditHistoryOrders.contractCode')"
                align="center" />
              <el-table-column prop="contractName" :label="$t('contractChangeAuditHistoryOrders.contractName')"
                align="center" />
              <el-table-column prop="contractTypeName" :label="$t('contractChangeAuditHistoryOrders.contractTypeName')"
                align="center" />
              <el-table-column prop="stateName" :label="$t('contractChangeAuditHistoryOrders.stateName')"
                align="center" />
              <el-table-column prop="createTime" :label="$t('contractChangeAuditHistoryOrders.createTime')"
                align="center" />
              <el-table-column :label="$t('contractChangeAuditHistoryOrders.operation')" align="center" width="150">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleViewDetail(scope.row)">
                    {{ $t('contractChangeAuditHistoryOrders.view') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
              :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
              @current-change="handleCurrentChange" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryContractChangeHistoryTask } from '@/api/contract/contractChangeAuditHistoryOrdersApi'

export default {
  name: 'ContractChangeAuditHistoryOrdersList',
  data() {
    return {
      loading: false,
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      conditions: {
        AuditOrdersId: '',
        userName: '',
        auditLink: ''
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
          ...this.conditions
        }
        const { data, total } = await queryContractChangeHistoryTask(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('contractChangeAuditHistoryOrders.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    },
    handleViewDetail(row) {
      this.$router.push({
        path: '/pages/admin/contractChangeDetails',
        query: { planId: row.planId }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.contract-change-audit-history-orders-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .table-wrapper {
    margin-top: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>