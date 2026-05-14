<template>
  <div class="transaction-log-manage-container animated fadeInRight">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between items-center">
            <span>{{ $t('transactionLogManage.queryCondition') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input
                v-model="searchForm.transactionId"
                :placeholder="$t('transactionLogManage.placeholder.transactionId')"
                clearable
              />
            </el-col>
            <el-col :span="8">
              <el-input
                v-model="searchForm.appId"
                :placeholder="$t('transactionLogManage.placeholder.appId')"
                clearable
              />
            </el-col>
            <el-col :span="6">
              <el-input
                v-model="searchForm.serviceCode"
                :placeholder="$t('transactionLogManage.placeholder.serviceCode')"
                clearable
              />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="handleSearch">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="handleReset">
                <i class="el-icon-refresh"></i>
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between items-center">
            <span>{{ $t('transactionLogManage.transactionLog') }}</span>
          </div>
          <el-table
            v-loading="loading"
            :data="tableData"
            border
            style="width: 100%"
          >
            <el-table-column
              prop="transactionId"
              :label="$t('transactionLogManage.table.transactionId')"
              align="center"
            />
            <el-table-column
              prop="srcIp"
              :label="$t('transactionLogManage.table.srcIp')"
              align="center"
            />
            <el-table-column
              prop="appId"
              :label="$t('transactionLogManage.table.appId')"
              align="center"
            />
            <el-table-column
              prop="serviceCode"
              :label="$t('transactionLogManage.table.serviceCode')"
              align="center"
            />
            <el-table-column
              prop="costTime"
              :label="$t('transactionLogManage.table.costTime')"
              align="center"
            />
            <el-table-column
              prop="state"
              :label="$t('transactionLogManage.table.state')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.state === 'F' ? $t('transactionLogManage.state.fail') : $t('transactionLogManage.state.success') }}
              </template>
            </el-table-column>
            <el-table-column
              prop="createTime"
              :label="$t('transactionLogManage.table.createTime')"
              align="center"
            />
            <el-table-column
              :label="$t('common.operation')"
              align="center"
              width="150"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleViewMessage(scope.row)"
                >
                  {{ $t('transactionLogManage.viewMessage') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            :current-page.sync="pagination.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-card>
      </el-col>
    </el-row>

    <transaction-log-message
      :visible.sync="dialogVisible"
      :log-id="currentLogId"
    />
  </div>
</template>

<script>
import { queryTransactionLog } from '@/api/log/transactionLogManageApi'
import TransactionLogMessage from '@/components/log/TransactionLogMessage'

export default {
  name: 'TransactionLogManageList',
  components: {
    TransactionLogMessage
  },
  data() {
    return {
      loading: false,
      searchForm: {
        transactionId: '',
        appId: '',
        serviceCode: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      currentLogId: ''
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
          page: this.pagination.current,
          row: this.pagination.size,
          transactionId: this.searchForm.transactionId.trim(),
          appId: this.searchForm.appId.trim(),
          serviceCode: this.searchForm.serviceCode.trim()
        }
        const { data, total } = await queryTransactionLog(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('transactionLogManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        transactionId: '',
        appId: '',
        serviceCode: ''
      }
      this.handleSearch()
    },
    handleViewMessage(row) {
      this.currentLogId = row.logId
      this.dialogVisible = true
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
.transaction-log-manage-container {
  padding: 20px;

  .el-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>