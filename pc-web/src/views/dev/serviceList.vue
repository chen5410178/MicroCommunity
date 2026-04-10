<template>
  <div class="service-container">
    <!-- 查询条件 -->
    <div class="search-wrapper">
      <div class="search-title">{{ $t('service.search.title') }}</div>
      <div class="search-content">
        <el-input v-model="searchForm.serviceId" :placeholder="$t('service.search.serviceId')" class="search-item"
          clearable @keyup.enter.native="handleSearch" />
        <el-input v-model="searchForm.serviceCode" :placeholder="$t('service.search.serviceCode')" class="search-item"
          clearable @keyup.enter.native="handleSearch" />
        <el-input v-model="searchForm.name" :placeholder="$t('service.search.name')" class="search-item" clearable
          @keyup.enter.native="handleSearch" />
        <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
      </div>
    </div>

    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('service.list.title') }}</div>
        <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="serviceId" :label="$t('service.table.serviceId')" />
        <el-table-column prop="serviceCode" :label="$t('service.table.serviceCode')" />
        <el-table-column prop="name" :label="$t('service.table.name')" />
        <el-table-column prop="url" :label="$t('service.table.url')" />
        <el-table-column prop="method" :label="$t('service.table.method')" />
        <el-table-column prop="timeout" :label="$t('service.table.timeout')" />
        <el-table-column prop="retryCount" :label="$t('service.table.retryCount')" />
        <el-table-column prop="createTime" :label="$t('common.createTime')" />
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
    <AddService :visible.sync="addVisible" @success="handleSuccess" />

    <EditService :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />

    <DelService :visible.sync="delVisible" :service-id="currentRow.serviceId" @success="handleSuccess" />
  </div>
</template>

<script>
import { getServiceList } from '@/api/dev/serviceApi'
import AddService from '@/components/dev/AddService'
import EditService from '@/components/dev/EditService'
import DelService from '@/components/dev/DelService'

export default {
  name: 'serviceList',
  components: {
    AddService,
    EditService,
    DelService
  },
  data() {
    return {
      loading: false,
      searchForm: {
        serviceId: '',
        serviceCode: '',
        name: '',
        url: '',
        method: '',
        timeout: '',
        retryCount: '',
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
          serviceId: this.searchForm.serviceId,
          serviceCode: this.searchForm.serviceCode,
          name: this.searchForm.name,
        }
        const { services, total } = await getServiceList(params)
        this.tableData = services
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('service.fetchError'))
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
.service-container {
  padding: 0;
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