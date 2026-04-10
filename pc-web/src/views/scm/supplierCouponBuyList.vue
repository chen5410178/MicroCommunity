<template>
  <div class="supplier-coupon-buy-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('supplierCouponBuy.searchTitle') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.buyId" :placeholder="$t('supplierCouponBuy.placeholder.buyId')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.couponId" :placeholder="$t('supplierCouponBuy.placeholder.couponId')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.name" :placeholder="$t('supplierCouponBuy.placeholder.name')" clearable />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 15px">
        <el-col :span="6">
          <el-input v-model="searchForm.supplierName" :placeholder="$t('supplierCouponBuy.placeholder.supplierName')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.storeName" :placeholder="$t('supplierCouponBuy.placeholder.storeName')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.objName" :placeholder="$t('supplierCouponBuy.placeholder.objName')" clearable />
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 15px">
        <el-col :span="6">
          <el-input v-model="searchForm.createUserTel" :placeholder="$t('supplierCouponBuy.placeholder.createUserTel')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.createUserName" :placeholder="$t('supplierCouponBuy.placeholder.createUserName')"
            clearable />
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('supplierCouponBuy.listTitle') }}</span>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="buyId" :label="$t('supplierCouponBuy.table.buyId')" align="center" />
        <el-table-column prop="couponName" :label="$t('supplierCouponBuy.table.couponName')" align="center" />
        <el-table-column prop="name" :label="$t('supplierCouponBuy.table.name')" align="center" />
        <el-table-column prop="supplierName" :label="$t('supplierCouponBuy.table.supplierName')" align="center" />
        <el-table-column prop="quantity" :label="$t('supplierCouponBuy.table.quantity')" align="center" />
        <el-table-column prop="valuePrice" :label="$t('supplierCouponBuy.table.valuePrice')" align="center" />
        <el-table-column prop="storeName" :label="$t('supplierCouponBuy.table.storeName')" align="center" />
        <el-table-column prop="objName" :label="$t('supplierCouponBuy.table.objName')" align="center" />
        <el-table-column prop="createUserTel" :label="$t('supplierCouponBuy.table.createUserTel')" align="center" />
        <el-table-column prop="createUserName" :label="$t('supplierCouponBuy.table.createUserName')" align="center" />
        <el-table-column prop="remark" :label="$t('supplierCouponBuy.table.remark')" align="center" />
      </el-table>

      <!-- 分页 -->
      <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        layout="total, sizes, prev, pager, next, jumper" :total="page.total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>
  </div>
</template>

<script>
import { getSupplierCouponBuyList } from '@/api/scm/supplierCouponBuyApi'

export default {
  name: 'SupplierCouponBuyList',
  data() {
    return {
      loading: false,
      searchForm: {
        buyId: '',
        couponId: '',
        name: '',
        supplierName: '',
        storeName: '',
        objName: '',
        createUserTel: '',
        createUserName: ''
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
          ...this.searchForm,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await getSupplierCouponBuyList(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('supplierCouponBuy.fetchError'))
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
    }
  }
}
</script>

<style lang="scss" scoped>
.supplier-coupon-buy-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: 15px;

      &:last-child {
        margin-bottom: 0;
      }
    }
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>