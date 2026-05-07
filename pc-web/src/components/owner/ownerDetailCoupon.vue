<template>
  <div class="margin-top">
    <el-table :data="ownerDetailCouponInfo.coupons" style="width: 100%">
      <el-table-column prop="couponId" :label="$t('ownerDetailCoupon.id')" align="center"></el-table-column>
      <el-table-column prop="couponName" :label="$t('ownerDetailCoupon.couponName')" align="center"></el-table-column>
      <el-table-column prop="value" :label="$t('ownerDetailCoupon.value')" align="center"></el-table-column>
      <el-table-column prop="validityDay" :label="$t('ownerDetailCoupon.validity')" align="center"></el-table-column>
      <el-table-column prop="userName" :label="$t('ownerDetailCoupon.userName')" align="center"></el-table-column>
      <el-table-column prop="tel" :label="$t('ownerDetailCoupon.phone')" align="center"></el-table-column>
      <el-table-column prop="toTypeName" :label="$t('ownerDetailCoupon.purpose')" align="center"></el-table-column>
      <el-table-column prop="stock" :label="$t('ownerDetailCoupon.quantity')" align="center">
        <template slot-scope="scope">{{scope.row.stock}}张</template>
      </el-table-column>
      <el-table-column prop="state" :label="$t('ownerDetailCoupon.status')" align="center">
        <template slot-scope="scope">{{scope.row.state == '1001'?$t('ownerDetailCoupon.unused'):$t('ownerDetailCoupon.used')}}</template>
      </el-table-column>
      <el-table-column prop="startTime" :label="$t('ownerDetailCoupon.effectiveTime')" align="center"></el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="pagination.currentPage"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      layout="total, prev, pager, next, jumper">
    </el-pagination>
  </div>
</template>

<script>
import { listCouponPropertyUser } from '@/api/scm/couponPropertyUserManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDetailCoupon',
  data() {
    return {
      ownerDetailCouponInfo: {
        coupons: [],
        ownerId: '',
        link: '',
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(ownerId, ownerName, link) {
      this.ownerDetailCouponInfo.ownerId = ownerId
      this.ownerDetailCouponInfo.ownerName = ownerName
      this.ownerDetailCouponInfo.link = link
      this._loadOwnerDetailCouponData(1, this.pagination.pageSize)
    },
    _loadOwnerDetailCouponData(page, row) {
      const params = {
        page: page,
        row: row,
        communityId: getCommunityId(),
        tel: this.ownerDetailCouponInfo.link
      }

      listCouponPropertyUser(params).then(response => {
        this.ownerDetailCouponInfo.coupons = response.data
        this.pagination.total = response.total
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    handleCurrentChange(val) {
      this._loadOwnerDetailCouponData(val, this.pagination.pageSize)
    }
  },
  created() {
    this.$on('switch', (data) => {
      this.ownerDetailCouponInfo.ownerId = data.ownerId
      this.ownerDetailCouponInfo.link = data.link
      this._loadOwnerDetailCouponData(1, this.pagination.pageSize)
    })
  }
}
</script>

<style scoped>
/* 样式同上一个组件 */
</style>