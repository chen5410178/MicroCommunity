<template>
  <div class="expiration-contract-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('expirationContractManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model.trim="searchForm.contractNameLike"
            :placeholder="$t('expirationContractManage.search.contractNameLike')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model.trim="searchForm.contractCode"
            :placeholder="$t('expirationContractManage.search.contractCode')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.contractType" :placeholder="$t('expirationContractManage.search.contractType')"
            style="width:100%">
            <el-option v-for="item in contractTypes" :key="item.contractTypeId" :label="item.typeName"
              :value="item.contractTypeId" />
          </el-select>
        </el-col>
        <el-col :span="6">
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

    <!-- 到期合同列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('expirationContractManage.list.title') }}</span>
      </div>
      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="contractName" :label="$t('expirationContractManage.table.contractName')" align="center" />
        <el-table-column prop="contractCode" :label="$t('expirationContractManage.table.contractCode')" align="center" />
        <el-table-column prop="contractTypeName" :label="$t('expirationContractManage.table.contractType')"
          align="center" />
        <el-table-column prop="partyA" :label="$t('expirationContractManage.table.partyA')" align="center" />
        <el-table-column prop="partyB" :label="$t('expirationContractManage.table.partyB')" align="center" />
        <el-table-column prop="operator" :label="$t('expirationContractManage.table.operator')" align="center" />
        <el-table-column prop="amount" :label="$t('expirationContractManage.table.amount')" align="center" />
        <el-table-column prop="startTime" :label="$t('expirationContractManage.table.startTime')" align="center" />
        <el-table-column prop="endTime" :label="$t('expirationContractManage.table.endTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('expirationContractManage.table.status')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleRenew(scope.row)">
              {{ $t('expirationContractManage.button.renew') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleStop(scope.row)">
              {{ $t('expirationContractManage.button.stop') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 终止合同弹窗 -->
    <stop-contract ref="stopContractDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { queryContract, queryContractType } from '@/api/contract/expirationContractManageApi'
import StopContract from '@/components/contract/StopContract'

export default {
  name: 'ExpirationContractManageList',
  components: {
    StopContract
  },
  data() {
    return {
      loading: false,
      searchForm: {
        contractNameLike: '',
        contractCode: '',
        contractType: '',
        expiration: '1'
      },
      tableData: [],
      contractTypes: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
    this.getContractTypes()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }
        const { data, total } = await queryContract(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('expirationContractManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getContractTypes() {
      try {
        const { data } = await queryContractType({
          page: 1,
          row: 50
        })
        this.contractTypes = data
      } catch (error) {
        console.error('获取合同类型失败:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        contractNameLike: '',
        contractCode: '',
        contractType: '',
        expiration: '1'
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
    handleRenew(row) {
      this.$router.push({
        path: '/views/contract/addContract',
        query: {
          contractId: row.contractId,
          contractCode: row.contractCode,
          contractName: row.contractName,
          stateName: row.stateName
        }
      })
    },
    handleStop(row) {
      this.$refs.stopContractDialog.open(row)
    },
    handleSuccess() {
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.expiration-contract-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: 0;
    }
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>