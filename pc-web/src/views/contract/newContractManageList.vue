<template>
  <div class="new-contract-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('newContractManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model.trim="searchForm.contractNameLike"
            :placeholder="$t('newContractManage.search.contractNamePlaceholder')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model.trim="searchForm.contractCode"
            :placeholder="$t('newContractManage.search.contractCodePlaceholder')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.contractType"
            :placeholder="$t('newContractManage.search.contractTypePlaceholder')" style="width:100%">
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

    <!-- 合同列表 -->
    <el-card class="table-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('newContractManage.list.title') }}</span>
        <el-button type="primary" size="small" @click="handleAdd">
          <i class="el-icon-plus"></i>
          {{ $t('newContractManage.list.draft') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width:100%">
        <el-table-column prop="contractName" :label="$t('newContractManage.table.contractName')" align="center" />
        <el-table-column prop="contractCode" :label="$t('newContractManage.table.contractCode')" align="center" />
        <el-table-column prop="parentContractCode" :label="$t('newContractManage.table.parentContractCode')"
          align="center">
          <template slot-scope="scope">
            {{ scope.row.parentContractCode || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="contractTypeName" :label="$t('newContractManage.table.contractType')" align="center" />
        <el-table-column prop="operator" :label="$t('newContractManage.table.operator')" align="center" />
        <el-table-column prop="amount" :label="$t('newContractManage.table.amount')" align="center" />
        <el-table-column prop="startTime" :label="$t('newContractManage.table.startTime')" align="center" />
        <el-table-column prop="endTime" :label="$t('newContractManage.table.endTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('newContractManage.table.state')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="220">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
            <el-button size="mini" @click="handlePrint(scope.row)">
              {{ $t('common.print') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="note-text">
        {{ $t('newContractManage.list.note') }}
      </div>

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 编辑合同组件 -->
    <edit-contract ref="editContract" @success="fetchData" />

    <!-- 删除合同组件 -->
    <delete-contract ref="deleteContract" @success="fetchData" />
  </div>
</template>

<script>
import { getContractList, getContractTypeList } from '@/api/contract/newContractManageApi'
import EditContract from '@/components/contract/editContract'
import DeleteContract from '@/components/contract/deleteContract'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'NewContractManageList',
  components: {
    EditContract,
    DeleteContract
  },
  data() {
    return {
      loading: false,
      searchForm: {
        contractNameLike: '',
        contractCode: '',
        contractType: '',
        state: ''
      },
      tableData: [],
      contractTypes: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.fetchData()
    this.fetchContractTypes()
  },
  methods: {
    async fetchData() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm,
          communityId: this.communityId
        }
        const { data, total } = await getContractList(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('newContractManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async fetchContractTypes() {
      try {
        const params = {
          page: 1,
          row: 100
        }
        const { data } = await getContractTypeList(params)
        this.contractTypes = data
      } catch (error) {
        console.error('获取合同类型失败:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.fetchData()
    },
    handleReset() {
      this.searchForm = {
        contractNameLike: '',
        contractCode: '',
        contractType: '',
        state: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$router.push('/views/contract/addContract')
    },
    handleEdit(row) {
      this.$refs.editContract.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteContract.open(row)
    },
    handlePrint(row) {
      window.open(`/#/views/contract/printContract?contractTypeId=${row.contractType}&contractId=${row.contractId}`)
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.fetchData()
    }
  }
}
</script>

<style lang="scss" scoped>
.new-contract-manage-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    margin-bottom: 20px;
  }

  .note-text {
    margin: 15px 0;
    color: #999;
    font-size: 14px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>