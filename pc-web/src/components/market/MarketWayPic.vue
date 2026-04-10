<template>
  <div class="market-pic-container">
    <el-card>
      <div class="search-wrapper">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input v-model="searchForm.name" :placeholder="$t('marketPic.name')" clearable
              @keyup.enter.native="handleSearch" />
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="handleSearch">
              {{ $t('marketPic.search') }}
            </el-button>
            <el-button type="primary" @click="handleAdd">
              {{ $t('marketPic.add') }}
            </el-button>
          </el-col>
        </el-row>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column :label="$t('marketPic.image')" align="center" width="120">
          <template slot-scope="scope">
            <el-image v-if="scope.row.picUrl" style="width: 60px; height: 60px; cursor: pointer; border-radius: 4px;"
              :src="scope.row.picUrl" :preview-src-list="[scope.row.picUrl]" fit="cover" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('marketPic.name')" align="center" />
        <el-table-column prop="picLink" :label="$t('marketPic.url')" align="center" />
        <el-table-column prop="createTime" :label="$t('marketPic.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('marketPic.remark')" align="center" />
        <el-table-column :label="$t('marketPic.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('marketPic.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('marketPic.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <add-market-pic ref="addMarketPic" @success="handleSuccess" />
    <edit-market-pic ref="editMarketPic" @success="handleSuccess" />
    <delete-market-pic ref="deleteMarketPic" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMarketPic } from '@/api/market/marketWayApi'
import AddMarketPic from './AddMarketPic'
import EditMarketPic from './EditMarketPic'
import DeleteMarketPic from './DeleteMarketPic'

export default {
  name: 'MarketWayPic',
  data() {
    return {
      loading: false,
      searchForm: {
        name: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  components: {
    AddMarketPic,
    EditMarketPic,
    DeleteMarketPic
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
          ...this.searchForm
        }
        const { data, total } = await listMarketPic(params)
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
    handleAdd() {
      this.$refs.addMarketPic.open()
    },
    handleEdit(row) {
      this.$refs.editMarketPic.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteMarketPic.open(row)
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
    },
    refreshList() {
      this.pagination.current = 1
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.market-pic-container {
  .search-wrapper {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>