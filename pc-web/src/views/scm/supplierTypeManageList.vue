<template>
  <div class="supplier-type-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('supplierTypeManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.typeCd" :placeholder="$t('supplierTypeManage.search.typeCd')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.typeName" :placeholder="$t('supplierTypeManage.search.typeName')" clearable />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 供应商类型列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('supplierTypeManage.list.title') }}</span>
        <el-button type="primary" style="float: right" @click="handleAdd">{{ $t('common.add') }}</el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="typeCd" :label="$t('supplierTypeManage.table.typeCd')" align="center" />
        <el-table-column prop="typeName" :label="$t('supplierTypeManage.table.typeName')" align="center" />
        <el-table-column prop="createTime" :label="$t('supplierTypeManage.table.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('supplierTypeManage.table.remark')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">{{ $t('common.edit') }}</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 子组件 -->
    <add-supplier-type ref="addSupplierType" @success="handleSuccess" />
    <edit-supplier-type ref="editSupplierType" @success="handleSuccess" />
    <delete-supplier-type ref="deleteSupplierType" @success="handleSuccess" />
  </div>
</template>

<script>
import { listSupplierType } from '@/api/scm/supplierTypeManageApi'
import AddSupplierType from '@/components/scm/AddSupplierType'
import EditSupplierType from '@/components/scm/EditSupplierType'
import DeleteSupplierType from '@/components/scm/DeleteSupplierType'

export default {
  name: 'SupplierTypeManageList',
  components: {
    AddSupplierType,
    EditSupplierType,
    DeleteSupplierType
  },
  data() {
    return {
      loading: false,
      searchForm: {
        typeCd: '',
        typeName: ''
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
        const { data, records } = await listSupplierType(params)
        this.tableData = data
        this.page.total = records
      } catch (error) {
        this.$message.error(this.$t('supplierTypeManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addSupplierType.open()
    },
    handleEdit(row) {
      this.$refs.editSupplierType.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteSupplierType.open(row)
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
.supplier-type-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: 20px;
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