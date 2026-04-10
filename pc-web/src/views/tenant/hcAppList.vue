<template>
  <div class="hcApp-container">
    <!-- 查询条件 -->
    <div class="search-wrapper">
      <div class="search-title">{{ $t('hcApp.search.title') }}</div>
      <div class="search-content">
        <el-input
          v-model="searchForm.appId"
          :placeholder="$t('hcApp.search.appId')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />
        <el-input
          v-model="searchForm.name"
          :placeholder="$t('hcApp.search.name')"
          class="search-item"
          clearable
          @keyup.enter.native="handleSearch"
        />
        <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
      </div>
    </div>

    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('hcApp.list.title') }}</div>
        <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="appId"
          :label="$t('hcApp.table.appId')"
        />
        <el-table-column
          prop="name"
          :label="$t('hcApp.table.name')"
        />
        <el-table-column
          prop="secure"
          :label="$t('hcApp.table.secure')"
        />
        <el-table-column
          prop="appToken"
          :label="$t('hcApp.table.appToken')"
        />
        <!-- <el-table-column
          prop="tenantId"
          :label="$t('hcApp.table.tenantId')"
        /> -->
        <el-table-column
          prop="validHours"
          :label="$t('hcApp.table.validHours')"
        />
        <el-table-column
          prop="remark"
          :label="$t('hcApp.table.remark')"
        />
        <el-table-column
          prop="createTime"
          :label="$t('hcApp.table.createTime')"
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
    <AddHcApp
      :visible.sync="addVisible"
      @success="handleSuccess"
    />

    <EditHcApp
      :visible.sync="editVisible"
      :form-data="currentRow"
      @success="handleSuccess"
    />

    <DelHcApp
      :visible.sync="delVisible"
      :app-id="currentRow.appId"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getHcAppList } from '@/api/tenant/hcAppApi'
import AddHcApp from '@/components/tenant/AddHcApp'
import EditHcApp from '@/components/tenant/EditHcApp'
import DelHcApp from '@/components/tenant/DelHcApp'

export default {
  name: 'hcAppList',
  components: {
    AddHcApp,
    EditHcApp,
    DelHcApp
  },
  data() {
    return {
      loading: false,
      searchForm: {
        appId: '',
        name: '',
        tenantId: '',
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
          appId: this.searchForm.appId,
          name: this.searchForm.name,
          secure: this.searchForm.secure,
          appToken: this.searchForm.appToken,
          tenantId: this.searchForm.tenantId,
          validHours: this.searchForm.validHours,
          remark: this.searchForm.remark,
          createTime: this.searchForm.createTime,
        }
        const { data } = await getHcAppList(params)
        this.tableData = data.data
        this.page.total = data.total
      } catch (error) {
        this.$message.error(this.$t('hcApp.fetchError'))
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
.hcApp-container {
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