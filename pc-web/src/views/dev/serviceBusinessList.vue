<template>
  <div class="serviceBusiness-container">
    <!-- 查询条件 -->
    <div class="search-wrapper">
      <div class="search-title">{{ $t('serviceBusiness.search.title') }}</div>
      <div class="search-content">
        <el-input v-model="searchForm.businessTypeCd" :placeholder="$t('serviceBusiness.search.businessTypeCd')"
          class="search-item" clearable @keyup.enter.native="handleSearch" />


        <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
      </div>
    </div>

    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('serviceBusiness.list.title') }}</div>
        <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="businessTypeCd" :label="$t('serviceBusiness.table.businessTypeCd')" />
        <!-- <el-table-column prop="invokeType" :label="$t('serviceBusiness.table.invokeType')" /> -->
        <el-table-column prop="url" :label="$t('serviceBusiness.table.url')" />
        <el-table-column prop="messageTopic" :label="$t('serviceBusiness.table.messageTopic')" />
        <el-table-column prop="timeout" :label="$t('serviceBusiness.table.timeout')" />
        <el-table-column prop="retryCount" :label="$t('serviceBusiness.table.retryCount')" />

        <el-table-column :label="$t('common.operation')" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">{{ $t('common.edit') }}</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>
    <AddServiceBusiness :visible.sync="addVisible" @success="handleSuccess" />

    <EditServiceBusiness :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />

    <DelServiceBusiness :visible.sync="delVisible" :service-business-id="currentRow.serviceBusinessId"
      @success="handleSuccess" />
  </div>
</template>

<script>
import { getServiceBusinessList } from '@/api/dev/serviceBusinessApi'
import AddServiceBusiness from '@/components/dev/AddServiceBusiness'
import EditServiceBusiness from '@/components/dev/EditServiceBusiness'
import DelServiceBusiness from '@/components/dev/DelServiceBusiness'

export default {
  name: 'serviceBusinessList',
  components: {
    AddServiceBusiness,
    EditServiceBusiness,
    DelServiceBusiness
  },
  data() {
    return {
      loading: false,
      searchForm: {
        businessTypeCd: '',
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      addVisible: false,
      editVisible: false,
      delVisible: false,
      currentRow: {}
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
          businessTypeCd: this.searchForm.businessTypeCd,
        }
        const { serviceImpls, total } = await getServiceBusinessList(params)
        this.tableData = serviceImpls
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('serviceBusiness.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.addVisible = true
    },
    handleEdit(row) {
      this.currentRow = { ...row }
      this.editVisible = true
    },
    handleDelete(row) {
      this.currentRow = { ...row }
      this.delVisible = true
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
.serviceBusiness-container {
  padding: 15px;
  margin: 0;

  .search-wrapper {
    background-color: #fff;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 4px;

    .search-title {
      font-size: 16px;
      font-weight: bold;
      margin-bottom: 20px;
      text-align: left;
      color: #333;
    }

    .search-content {
      display: flex;
      align-items: center;
      gap: 20px;

      .search-item {
        width: 200px;
      }
    }
  }

  .list-wrapper {
    background-color: #fff;
    padding: 20px;
    border-radius: 4px;

    .list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      .list-title {
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }
    }

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>