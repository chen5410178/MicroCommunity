<template>
  <div class="owner-detail-coupon">
    <div class="margin-top">
      <el-table :data="aOwnerDetailCouponInfo.coupons" border style="width: 100%">
        <el-table-column prop="couponId" :label="$t('aOwnerDetailCoupon.id')" align="center" />
        <el-table-column prop="couponName" :label="$t('aOwnerDetailCoupon.couponName')" align="center" />
        <el-table-column prop="value" :label="$t('aOwnerDetailCoupon.faceValue')" align="center" />
        <el-table-column prop="validityDay" :label="$t('aOwnerDetailCoupon.validity')" align="center" />
        <el-table-column prop="userName" :label="$t('aOwnerDetailCoupon.userName')" align="center" />
        <el-table-column prop="tel" :label="$t('aOwnerDetailCoupon.phone')" align="center" />
        <el-table-column prop="toTypeName" :label="$t('aOwnerDetailCoupon.purpose')" align="center" />
        <el-table-column prop="stock" :label="$t('aOwnerDetailCoupon.quantity')" align="center">
          <template #default="{ row }">
            {{ row.stock }}{{ $t('aOwnerDetailCoupon.sheet') }}
          </template>
        </el-table-column>
        <el-table-column prop="state" :label="$t('aOwnerDetailCoupon.status')" align="center">
          <template #default="{ row }">
            {{ row.state === '1001' ? $t('aOwnerDetailCoupon.unused') : $t('aOwnerDetailCoupon.used') }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('aOwnerDetailCoupon.effectiveTime')" align="center" />
      </el-table>

      <el-row class="margin-top">
        <el-col :span="16"></el-col>
        <el-col :span="8">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next" :total="total" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listAdminCouponPropertyUser } from '@/api/aCommunity/aOwnerDetailCouponApi'
export default {
  name: 'AOwnerDetailCoupon',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      aOwnerDetailCouponInfo: {
        coupons: [],
        ownerId: '',
        link: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.aOwnerDetailCouponInfo.ownerId = data.ownerId
      this.aOwnerDetailCouponInfo.link = data.link
      this._loadAOwnerDetailCouponData(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadAOwnerDetailCouponData(val, this.DEFAULT_ROWS)
    },
    async _loadAOwnerDetailCouponData(page, row) {
      const param = {
        page,
        row,
        tel: this.aOwnerDetailCouponInfo.link
      }

      try {
        const response = await listAdminCouponPropertyUser(param)
        this.aOwnerDetailCouponInfo.coupons = response.data
        this.total = response.total
        this.currentPage = page
      } catch (error) {
        console.error('请求失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
</style>