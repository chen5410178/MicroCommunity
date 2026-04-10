<template>
  <div class="market-goods-container">
    <el-card>
      <div class="search-wrapper">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input v-model="searchForm.name" :placeholder="$t('marketGoods.name')" clearable
              @keyup.enter.native="handleSearch" />
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="handleSearch">
              {{ $t('marketGoods.search') }}
            </el-button>
            <el-button type="primary" @click="handleAdd">
              {{ $t('marketGoods.add') }}
            </el-button>
          </el-col>
        </el-row>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="name" :label="$t('marketGoods.name')" align="center" />
        <el-table-column :label="$t('marketGoods.goodsCount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.goodsCount }}
            <el-button type="text" @click="handleViewGoods(scope.row)">
              ({{ $t('marketGoods.viewGoods') }})
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('marketGoods.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('marketGoods.remark')" align="center" />
        <el-table-column :label="$t('marketGoods.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('marketGoods.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('marketGoods.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <add-market-goods ref="addMarketGoods" @success="handleSuccess" />
    <edit-market-goods ref="editMarketGoods" @success="handleSuccess" />
    <delete-market-goods ref="deleteMarketGoods" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMarketGoods } from '@/api/market/marketWayApi'
import AddMarketGoods from './AddMarketGoods'
import EditMarketGoods from './EditMarketGoods'
import DeleteMarketGoods from './DeleteMarketGoods'

export default {
  name: 'MarketWayGoods',
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
    AddMarketGoods,
    EditMarketGoods,
    DeleteMarketGoods
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
        const { data, total } = await listMarketGoods(params)
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
      this.$refs.addMarketGoods.open()
    },
    handleEdit(row) {
      this.$refs.editMarketGoods.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteMarketGoods.open(row)
    },
    handleViewGoods(row) {
      this.$router.push(`/views/market/marketGoodsItemManage?goodsId=${row.goodsId}`)
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
.market-goods-container {
  .search-wrapper {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>