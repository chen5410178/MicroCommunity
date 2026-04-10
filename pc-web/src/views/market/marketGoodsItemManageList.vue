<template>
  <div class="market-goods-item-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('marketGoodsItem.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.prodName" :placeholder="$t('marketGoodsItem.search.prodName')" clearable />
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('marketGoodsItem.list.title') }}</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleAdd">
            {{ $t('common.add') }}
          </el-button>
          <el-button type="default" size="small" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="picUrl" :label="$t('marketGoodsItem.table.picUrl')" align="center">
          <template slot-scope="scope">
            <el-image style="width: 60px; height: 60px; cursor: pointer; border-radius: 4px;"
              :src="scope.row.picUrl || '/img/noPhoto.jpg'" :preview-src-list="[scope.row.picUrl || '/img/noPhoto.jpg']"
              fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="prodName" :label="$t('marketGoodsItem.table.prodName')" align="center" />
        <el-table-column prop="prodDesc" :label="$t('marketGoodsItem.table.prodDesc')" align="center" />
        <el-table-column prop="picLink" :label="$t('marketGoodsItem.table.picLink')" align="center" />
        <el-table-column prop="price" :label="$t('marketGoodsItem.table.price')" align="center" />
        <el-table-column prop="shopName" :label="$t('marketGoodsItem.table.shopName')" align="center" />
        <el-table-column prop="sort" :label="$t('marketGoodsItem.table.sort')" align="center" />
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

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 子组件 -->
    <add-market-goods-item ref="addDialog" @success="handleSuccess" />
    <edit-market-goods-item ref="editDialog" @success="handleSuccess" />
    <delete-market-goods-item ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMarketGoodsItem } from '@/api/market/marketGoodsItemManageApi'
import AddMarketGoodsItem from '@/components/market/AddMarketGoodsItem'
import EditMarketGoodsItem from '@/components/market/EditMarketGoodsItem'
import DeleteMarketGoodsItem from '@/components/market/DeleteMarketGoodsItem'

export default {
  name: 'MarketGoodsItemManageList',
  components: {
    AddMarketGoodsItem,
    EditMarketGoodsItem,
    DeleteMarketGoodsItem
  },
  data() {
    return {
      loading: false,
      searchForm: {
        goodsId: this.$route.query.goodsId || '',
        prodName: '',
        shopName: ''
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
        const { data, total } = await listMarketGoodsItem(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('marketGoodsItem.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.$refs.addDialog.open({
        goodsId: this.searchForm.goodsId
      })
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
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.market-goods-item-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .search-title {
      font-size: 16px;
      font-weight: bold;
    }
  }

  .list-wrapper {
    .list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>