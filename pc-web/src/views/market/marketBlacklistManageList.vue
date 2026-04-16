<template>
  <div class="market-blacklist-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('marketBlacklist.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.personName" :placeholder="$t('marketBlacklist.search.personName')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.filter" :placeholder="$t('marketBlacklist.search.filter')" clearable />
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('marketBlacklist.list.title') }}</span>
        <el-button type="primary" style="float: right" @click="handleAdd">{{ $t('common.add') }}</el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="personName" :label="$t('marketBlacklist.table.personName')" align="center" />
        <el-table-column prop="filter" :label="$t('marketBlacklist.table.filter')" align="center" />
        <el-table-column prop="createTime" :label="$t('marketBlacklist.table.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('marketBlacklist.table.remark')" align="center" />
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

    <!-- 组件 -->
    <add-market-blacklist ref="addDialog" @success="handleSuccess" />
    <edit-market-blacklist ref="editDialog" @success="handleSuccess" />
    <delete-market-blacklist ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMarketBlacklist } from '@/api/market/marketBlacklistManageApi'
import AddMarketBlacklist from '@/components/market/AddMarketBlacklist'
import EditMarketBlacklist from '@/components/market/EditMarketBlacklist'
import DeleteMarketBlacklist from '@/components/market/DeleteMarketBlacklist'

export default {
  name: 'MarketBlacklistManageList',
  components: {
    AddMarketBlacklist,
    EditMarketBlacklist,
    DeleteMarketBlacklist
  },
  data() {
    return {
      loading: false,
      searchForm: {
        personName: '',
        filter: ''
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
        const { data, records } = await listMarketBlacklist(params)
        this.tableData = data
        this.page.total = records
      } catch (error) {
        this.$message.error(this.$t('marketBlacklist.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
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
.market-blacklist-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>