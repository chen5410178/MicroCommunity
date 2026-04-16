<template>
  <div class="contract-type-spec-manage-container">
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('contractTypeSpecManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.specCd" :placeholder="$t('contractTypeSpecManage.search.specCd')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.specName" :placeholder="$t('contractTypeSpecManage.search.specName')" clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.specShow" :placeholder="$t('contractTypeSpecManage.search.specShow')"
            style="width:100%">
            <el-option :label="$t('contractTypeSpecManage.search.all')" value="" />
            <el-option :label="$t('contractTypeSpecManage.search.yes')" value="Y" />
            <el-option :label="$t('contractTypeSpecManage.search.no')" value="N" />
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

    <el-card class="table-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('contractTypeSpecManage.table.title') }}</span>
        <div style="float:right">
          <el-button type="primary" size="small" @click="handleGoBack">
            {{ $t('common.back') }}
          </el-button>
          <el-button type="primary" size="small" @click="handleAdd">
            {{ $t('common.add') }}
          </el-button>
        </div>
      </div>
      <el-table v-loading="loading" :data="tableData" border style="width:100%">
        <el-table-column prop="specCd" :label="$t('contractTypeSpecManage.table.specCd')" align="center" />
        <el-table-column prop="specName" :label="$t('contractTypeSpecManage.table.specName')" align="center" />
        <el-table-column prop="required" :label="$t('contractTypeSpecManage.table.required')" align="center">
          <template slot-scope="scope">
            {{ scope.row.required === 'Y' ? $t('common.yes') : $t('common.no') }}
          </template>
        </el-table-column>
        <el-table-column prop="specShow" :label="$t('contractTypeSpecManage.table.specShow')" align="center">
          <template slot-scope="scope">
            {{ scope.row.specShow === 'Y' ? $t('common.yes') : $t('common.no') }}
          </template>
        </el-table-column>
        <el-table-column prop="specValueType" :label="$t('contractTypeSpecManage.table.specValueType')" align="center">
          <template slot-scope="scope">
            {{ formatSpecValueType(scope.row.specValueType) }}
          </template>
        </el-table-column>
        <el-table-column prop="specType" :label="$t('contractTypeSpecManage.table.specType')" align="center">
          <template slot-scope="scope">
            {{ formatSpecType(scope.row.specType) }}
          </template>
        </el-table-column>
        <el-table-column prop="listShow" :label="$t('contractTypeSpecManage.table.listShow')" align="center">
          <template slot-scope="scope">
            {{ scope.row.listShow === 'Y' ? $t('common.yes') : $t('common.no') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <add-contract-type-spec ref="addDialog" @success="handleSuccess" />
    <edit-contract-type-spec ref="editDialog" @success="handleSuccess" />
    <delete-contract-type-spec ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { queryContractTypeSpec } from '@/api/contract/contractTypeSpecManageApi'
import AddContractTypeSpec from '@/components/contract/addContractTypeSpec'
import EditContractTypeSpec from '@/components/contract/editContractTypeSpec'
import DeleteContractTypeSpec from '@/components/contract/deleteContractTypeSpec'

export default {
  name: 'ContractTypeSpecManageList',
  components: {
    AddContractTypeSpec,
    EditContractTypeSpec,
    DeleteContractTypeSpec
  },
  data() {
    return {
      loading: false,
      searchForm: {
        specCd: '',
        specName: '',
        specShow: '',
        contractTypeId: this.$route.query.contractTypeId || ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      typeName: this.$route.query.typeName || ''
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
        const { data, total } = await queryContractTypeSpec(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('contractTypeSpecManage.fetchError'))
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
        specCd: '',
        specName: '',
        specShow: '',
        contractTypeId: this.$route.query.contractTypeId || ''
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
    handleAdd() {
      this.$refs.addDialog.open({
        contractTypeId: this.searchForm.contractTypeId
      })
    },
    handleEdit(row) {
      this.$refs.editDialog.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleGoBack() {
      this.$router.go(-1)
    },
    handleSuccess() {
      this.getList()
    },
    formatSpecValueType(type) {
      const map = {
        '1001': this.$t('contractTypeSpecManage.specValueType.string'),
        '2002': this.$t('contractTypeSpecManage.specValueType.integer'),
        '3003': this.$t('contractTypeSpecManage.specValueType.amount'),
        '4004': this.$t('contractTypeSpecManage.specValueType.date'),
        '5005': this.$t('contractTypeSpecManage.specValueType.time')
      }
      return map[type] || type
    },
    formatSpecType(type) {
      const map = {
        '2233': this.$t('contractTypeSpecManage.specType.input'),
        '3344': this.$t('contractTypeSpecManage.specType.select')
      }
      return map[type] || type
    }
  }
}
</script>

<style lang="scss" scoped>
.contract-type-spec-manage-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>