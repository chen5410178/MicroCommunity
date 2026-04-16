<template>
  <div class="coupon-market-container">
    <div class="search-wrapper">
      <div>
        <el-input v-model="searchForm.couponName" style="width: 50%" :placeholder="$t('couponMarket.search.placeholder')"
          @keyup.enter.native="handleSearch" />
        <el-button type="primary" @click="handleSearch">
          {{ $t('common.search') }}
        </el-button>
      </div>
      <el-row :gutter="20" class="margin-top-sm">
        <el-col :span="3"></el-col>
        <el-col :span="12">
          <el-link v-for="(item, index) in couponMarketInfo.suppliers" :key="index" type="primary"
            @click="handleQuerySupplierCoupon(item)" class="supplier-link">
            {{ item.supplierName }}
          </el-link>
        </el-col>
      </el-row>
    </div>

    <divider></divider>

    <div class="coupon-list-wrapper">
      <el-row :gutter="20">
        <el-col v-for="(item, index) in couponMarketInfo.coupons" :key="index" :span="6" class="coupon-item">
          <el-card shadow="hover">
            <el-image :src="couponMarketInfo.url" class="coupon-image" fit="cover" />
            <div class="coupon-title">
              {{ item.name }}
            </div>
            <el-row class="coupon-footer">
              <el-col :span="12">
                <span class="price-label">{{ $t('couponMarket.price') }}:</span>
                <span class="price-value">￥{{ item.valuePrice }}</span>
              </el-col>
              <el-col :span="12" class="text-right">
                <el-link type="primary">{{ $t('couponMarket.buy') }}</el-link>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>

      <el-pagination v-if="couponMarketInfo.records > 1" :current-page.sync="page.current" :page-size="page.size"
        :total="couponMarketInfo.records" layout="total, prev, pager, next" @current-change="handlePageChange"
        class="pagination-wrapper" />
    </div>
  </div>
</template>

<script>
import { listSupplier, listSupplierCoupon } from '@/api/scm/couponMarketApi'
import divider from '@/components/system/divider'

export default {
  name: 'CouponMarketList',
  data() {
    return {
      searchForm: {
        couponName: ''
      },
      couponMarketInfo: {
        coupons: [],
        suppliers: [],
        total: 0,
        records: 1,
        url: 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2Fv2-e262fc8062b7ef085a9f4de51a31f08f_1440w.jpg%3Fsource%3D172ae18b&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1671372327&t=5644f55f337250a4f5af2baae7f34e3e'
      },
      page: {
        current: 1,
        size: 15
      }
    }
  },
  components: {
    divider
  },
  created() {
    this.loadTopSupplier()
    this.loadCoupons()
  },
  methods: {
    async loadTopSupplier() {
      try {
        const params = {
          page: 1,
          row: 5
        }
        const { data } = await listSupplier(params)
        this.couponMarketInfo.suppliers = data
      } catch (error) {
        this.$message.error(this.$t('couponMarket.fetchSupplierError'))
      }
    },
    async loadCoupons() {
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          supplierId: this.currentSupplierId || ''
        }
        const { data, total, records } = await listSupplierCoupon(params)
        this.couponMarketInfo.coupons = data
        this.couponMarketInfo.total = total
        this.couponMarketInfo.records = records
      } catch (error) {
        this.$message.error(this.$t('couponMarket.fetchCouponError'))
      }
    },
    handleQuerySupplierCoupon(item) {
      this.currentSupplierId = item.supplierId
      this.page.current = 1
      this.loadCoupons()
    },
    handleSearch() {
      this.page.current = 1
      this.loadCoupons()
    },
    handlePageChange(currentPage) {
      this.page.current = currentPage
      this.loadCoupons()
    }
  }
}
</script>

<style lang="scss" scoped>
.coupon-market-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .supplier-wrapper {
    margin-bottom: 20px;

    .supplier-link {
      margin-right: 15px;
    }
  }

  .coupon-list-wrapper {
    .coupon-item {
      margin-bottom: 20px;

      .coupon-image {
        width: 100%;
        height: 150px;
        border-radius: 4px;
      }

      .coupon-title {
        padding: 10px 0;
        font-weight: bold;
      }

      .coupon-footer {
        padding-top: 10px;

        .price-label {
          color: #999;
        }

        .price-value {
          color: #ff0036;
          font-weight: bold;
        }
      }
    }

    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>