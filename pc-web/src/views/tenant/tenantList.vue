<template>
  <div class="tenant-container">
    <!-- 查询条件 -->
    <div class="search-wrapper">
      <div class="search-title">{{ $t('tenant.search.title') }}</div>
      <div class="search-content">
        <el-input
          v-model="searchForm.name"
          :placeholder="$t('tenant.search.name')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />
        <el-input
          v-model="searchForm.address"
          :placeholder="$t('tenant.search.address')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />
        <el-input
          v-model="searchForm.tel"
          :placeholder="$t('tenant.search.tel')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />
        <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
      </div>
    </div>

    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('tenant.list.title') }}</div>
        <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="tenantId"
          :label="$t('tenant.table.tenantId')"
        />
        <el-table-column
          prop="name"
          :label="$t('tenant.table.name')"

        />
        <el-table-column
          prop="address"
          :label="$t('tenant.table.address')"

        />
        <el-table-column
          prop="tel"
          :label="$t('tenant.table.tel')"

        />
        <el-table-column
          prop="createTime"
          :label="$t('tenant.table.createTime')"

        />
        <el-table-column
          :label="$t('common.operation')"
          width="200"
          fixed="right"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleEdit(scope.row)"
            >{{ $t('common.edit') }}</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)"
            >{{ $t('common.delete') }}</el-button>
            <el-button
              size="mini"
              class="margin-top"
              plain
              @click="handleResetPassword(scope.row)"
            >{{ $t('tenant.table.resetPassword') }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <Addtenant
      :visible.sync="addVisible"
      @success="handleSuccess"
    />

    <Edittenant
      :visible.sync="editVisible"
      :form-data="currentRow"
      @success="handleSuccess"
    />

    <Deltenant
      :visible.sync="delVisible"
      :tenant-id="currentRow.tenantId"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getTenantList,resetTenantPassword } from '@/api/tenant/tenantApi'
import Addtenant from '@/components/tenant/Addtenant'
import Edittenant from '@/components/tenant/Edittenant'
import Deltenant from '@/components/tenant/Deltenant'

export default {
  name: 'tenantList',
  components: {
    Addtenant,
    Edittenant,
    Deltenant
  },
  data() {
    return {
      loading: false,
      searchForm: {
        tenantId: '',
        name: '',
        address: '',
        tel: '',
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
          tenantId: this.searchForm.tenantId,
          name: this.searchForm.name,
          address: this.searchForm.address,
          tel: this.searchForm.tel,
          createTime: this.searchForm.createTime,
        }
        const { data } = await getTenantList(params)
        this.tableData = data.data
        this.page.total = data.total
      } catch (error) {
        this.$message.error(this.$t('tenant.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleResetPassword(row) {
      this.currentRow = { ...row }
      resetTenantPassword(this.currentRow.tenantId).then(res => {
        this.$message.success(res.msg)
      }).catch(error => {
        this.$message.error(error.message)
      })
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
.tenant-container {
  padding: 0;
  margin: 0;
  .margin-top {
    margin-top: 10px;
  }

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