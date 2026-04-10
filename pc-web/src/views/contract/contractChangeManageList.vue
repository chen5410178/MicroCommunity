<template>
  <div class="contract-change-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('contractChangeManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.contractName" :placeholder="$t('contractChangeManage.search.contractName')"
            clearable @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.contractCode" :placeholder="$t('contractChangeManage.search.contractCode')"
            clearable @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.contractType" :placeholder="$t('contractChangeManage.search.contractType')"
            style="width:100%" clearable>
            <el-option v-for="item in contractTypes" :key="item.contractTypeId" :label="item.typeName"
              :value="item.contractTypeId" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 合同变更信息 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('contractChangeManage.list.title') }}</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleToChangePage('contractChangeMainBody')">
            <i class="el-icon-plus"></i>
            {{ $t('contractChangeManage.list.mainBodyChange') }}
          </el-button>
          <el-button type="primary" size="small" @click="handleToChangePage('contractChangeLease')">
            <i class="el-icon-plus"></i>
            {{ $t('contractChangeManage.list.leaseAdjustment') }}
          </el-button>
          <el-button type="primary" size="small" @click="handleToChangePage('contractChangeAssets')">
            <i class="el-icon-plus"></i>
            {{ $t('contractChangeManage.list.assetsChange') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="contractName" :label="$t('contractChangeManage.table.contractName')" align="center" />
        <el-table-column prop="contractCode" :label="$t('contractChangeManage.table.contractCode')" align="center" />
        <el-table-column prop="contractTypeName" :label="$t('contractChangeManage.table.contractType')" align="center" />
        <el-table-column prop="partyA" :label="$t('contractChangeManage.table.partyA')" align="center" />
        <el-table-column prop="partyB" :label="$t('contractChangeManage.table.partyB')" align="center" />
        <el-table-column prop="planTypeName" :label="$t('contractChangeManage.table.changeType')" align="center" />
        <el-table-column prop="changePersonName" :label="$t('contractChangeManage.table.changePerson')" align="center" />
        <el-table-column prop="createTime" :label="$t('contractChangeManage.table.applyTime')" align="center" />
        <el-table-column prop="remark" :label="$t('contractChangeManage.table.remark')" align="center" />
        <el-table-column prop="stateName" :label="$t('contractChangeManage.table.state')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="180">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === '11'" type="text" size="small"
              @click="handleOpenDeleteDialog(scope.row)">
              {{ $t('common.cancel') }}
            </el-button>
            <el-button type="text" size="small" @click="handleToDetails(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />

      <div class="note">
        {{ $t('contractChangeManage.note') }}
      </div>
    </el-card>

    <!-- 删除确认对话框 -->
    <delete-contract-change-plan ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { queryContractChangePlan, queryContractType } from '@/api/contract/contractChangeManageApi'
import DeleteContractChangePlan from '@/components/contract/deleteContractChangePlan'

export default {
  name: 'ContractChangeManageList',
  components: {
    DeleteContractChangePlan
  },
  data() {
    return {
      loading: false,
      searchForm: {
        contractName: '',
        contractCode: '',
        contractType: ''
      },
      tableData: [],
      contractTypes: [],
      page: {
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
          page: this.page.current,
          row: this.page.size,
          ...this.searchForm
        }
        const { data, total } = await queryContractChangePlan(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('contractChangeManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getContractTypes() {
      try {
        const params = {
          page: 1,
          row: 50
        }
        const { data } = await queryContractType(params)
        this.contractTypes = data
      } catch (error) {
        console.error('获取合同类型失败:', error)
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        contractName: '',
        contractCode: '',
        contractType: ''
      }
      this.handleSearch()
    },
    handleToChangePage(type) {
      this.$router.push(`/views/contract/contractChangeDetail?param=${type}`)
    },
    handleToDetails(row) {
      this.$router.push(`/views/contract/contractChangeDetails?planId=${row.planId}`)
    },
    handleOpenDeleteDialog(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleSuccess() {
      this.getList()
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
.contract-change-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .list-wrapper {
    margin-bottom: 20px;

    .note {
      margin-top: 20px;
      color: #909399;
      font-size: 14px;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>