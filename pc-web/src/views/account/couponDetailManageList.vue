<template>
  <div class="coupon-detail-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('couponDetailManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input v-model="searchForm.poolId" :placeholder="$t('couponDetailManage.search.poolId')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="4">
          <el-input v-model="searchForm.shopId" :placeholder="$t('couponDetailManage.search.shopId')" clearable
            @keyup.enter.native="handleSearch" />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix text-left">
        <span>{{ $t('couponDetailManage.list.title') }}</span>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="detailId" :label="$t('couponDetailManage.table.detailId')" align="center" />
        <el-table-column prop="shopName" :label="$t('couponDetailManage.table.shopName')" align="center" />
        <el-table-column prop="couponName" :label="$t('couponDetailManage.table.couponName')" align="center" />
        <el-table-column prop="actualPrice" :label="$t('couponDetailManage.table.actualPrice')" align="center" />
        <el-table-column prop="buyPrice" :label="$t('couponDetailManage.table.buyPrice')" align="center" />
        <el-table-column prop="amount" :label="$t('couponDetailManage.table.amount')" align="center" />
        <el-table-column prop="buyCount" :label="$t('couponDetailManage.table.buyCount')" align="center" />
        <el-table-column prop="validityDay" :label="$t('couponDetailManage.table.validityDay')" align="center" />
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

  </div>
</template>

<script>
import {
  listCouponDetail,
} from '@/api/account/couponDetailManageApi'

export default {
  name: 'CouponDetailManageList',
  components: {
  },
  data() {
    return {
      loading: false,
      searchForm: {
        poolId: '',
        shopId: ''
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
        const { data, total } = await listCouponDetail(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('couponDetailManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
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
    handleSuccess() {
      this.getList()
    },
    handleAdd() {
      this.$refs.addCouponDetail.open()
    },
    handleEdit(row) {
      this.$refs.editCouponDetail.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteCouponDetail.open(row)
    }
  }
}
</script>

<style lang="scss" scoped>
.coupon-detail-container {
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