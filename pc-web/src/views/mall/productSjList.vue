<template>
  <div class="product-sj-container ">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('productSj.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="searchForm.prodName"
            :placeholder="$t('productSj.search.prodName')"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 商品列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('productSj.list.title') }}</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="listProductsToDay">
            <i class="el-icon-plus"></i>
            {{ $t('productSj.list.today') }}
          </el-button>
          <el-button type="primary" size="small" style="margin-left: 10px" @click="listProductsAll">
            {{ $t('productSj.list.all') }}
          </el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="productId" :label="$t('productSj.table.productId')" align="center" />
        <el-table-column :label="$t('productSj.table.coverPhoto')" align="center">
          <template slot-scope="scope">
            <el-image
              style="width: 60px; height: 60px; border-radius: 5px;"
              :src="scope.row.coverPhoto"
              fit="cover"
            />
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" :label="$t('productSj.table.categoryName')" align="center" />
        <el-table-column prop="prodName" :label="$t('productSj.table.prodName')" align="center" />
        <el-table-column prop="sales" :label="$t('productSj.table.sales')" align="center" />
        <el-table-column prop="stock" :label="$t('productSj.table.stock')" align="center" />
        <el-table-column :label="$t('productSj.table.price')" align="center">
          <template slot-scope="scope">
            {{ scope.row.defaultSpecValue.price }}
          </template>
        </el-table-column>
        <el-table-column prop="sort" :label="$t('productSj.table.sort')" align="center" />
        <el-table-column prop="stateName" :label="$t('productSj.table.state')" align="center" />
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
    </el-card>

    <delete-product ref="deleteProduct" @success="handleSuccess" />
  </div>
</template>

<script>
import { getAdminProductList } from '@/api/mall/productSjApi'
import DeleteProduct from '@/components/mall/DeleteProduct'

export default {
  name: 'ProductSjList',
  components: {
    DeleteProduct
  },
  data() {
    return {
      loading: false,
      searchForm: {
        prodName: '',
        state: '2002',
        keyword: '',
        barCode: '',
        createTime: '',
        mallApiCode: 'queryAdminProductBmoImpl'
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
        const { data, total } = await getAdminProductList(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('productSj.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    listProductsToDay() {
      this.searchForm.createTime = this.$dayjs().format('YYYY-MM-DD HH:mm:ss')
      this.handleSearch()
    },
    listProductsAll() {
      this.searchForm.createTime = ''
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    },
    handleSuccess() {
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.product-sj-container {
    padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>