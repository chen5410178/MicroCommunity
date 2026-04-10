<template>
  <div class="resource-supplier-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('resourceSupplierManage.searchTitle') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.rsId" :placeholder="$t('resourceSupplierManage.supplierNo')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.supplierName" :placeholder="$t('resourceSupplierManage.supplierName')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.tel" :placeholder="$t('resourceSupplierManage.phone')" clearable />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            {{ $t('resourceSupplierManage.search') }}
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>
            {{ $t('resourceSupplierManage.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 供应商列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('resourceSupplierManage.supplier') }}</span>
        <el-button type="primary" size="small" @click="handleAdd">
          <i class="el-icon-plus"></i>
          {{ $t('resourceSupplierManage.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="rsId" :label="$t('resourceSupplierManage.no')" align="center" />
        <el-table-column prop="supplierName" :label="$t('resourceSupplierManage.name')" align="center" />
        <el-table-column prop="address" :label="$t('resourceSupplierManage.address')" align="center" />
        <el-table-column prop="tel" :label="$t('resourceSupplierManage.contact')" align="center" />
        <el-table-column prop="contactName" :label="$t('resourceSupplierManage.contactName')" align="center" />
        <el-table-column prop="accountBank" :label="$t('resourceSupplierManage.bank')" align="center" />
        <el-table-column prop="bankAccountNumber" :label="$t('resourceSupplierManage.account')" align="center" />
        <el-table-column prop="remark" :label="$t('resourceSupplierManage.remark')" align="center" />
        <el-table-column :label="$t('resourceSupplierManage.operation')" align="center" width="180">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('resourceSupplierManage.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('resourceSupplierManage.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 组件 -->
    <add-resource-supplier ref="addDialog" @success="handleSuccess" />
    <edit-resource-supplier ref="editDialog" @success="handleSuccess" />
    <delete-resource-supplier ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { listResourceSuppliers } from '@/api/resource/resourceSupplierManageApi'
import AddResourceSupplier from '@/components/resource/AddResourceSupplier'
import EditResourceSupplier from '@/components/resource/EditResourceSupplier'
import DeleteResourceSupplier from '@/components/resource/DeleteResourceSupplier'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ResourceSupplierManageList',
  components: {
    AddResourceSupplier,
    EditResourceSupplier,
    DeleteResourceSupplier
  },
  data() {
    return {
      loading: false,
      searchForm: {
        rsId: '',
        supplierName: '',
        tel: ''
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
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm,
          communityId: getCommunityId()
        }
        const { data, total } = await listResourceSuppliers(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(error.message)
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
        rsId: '',
        supplierName: '',
        tel: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$refs.addDialog.open()
    },
    handleEdit(row) {
      this.$refs.editDialog.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleSuccess() {
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
.resource-supplier-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-input {
      width: 100%;
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