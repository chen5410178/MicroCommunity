<template>
  <div v-if="payFeeCouponInfo.feeCoupons.length > 0">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="ibox">
          <div slot="header" class="ibox-title">
            <h5>
              <span>{{ $t('payFeeCoupon.giftCoupons') }}</span>
            </h5>
            <div class="ibox-tools" style="top:10px;">
            </div>
          </div>
          <div class="ibox-content">
            <el-table :data="payFeeCouponInfo.feeCoupons" border style="width:100%">
              <el-table-column :label="$t('payFeeCoupon.giftRule')" prop="ruleName"></el-table-column>
              <el-table-column :label="$t('payFeeCoupon.couponName')" prop="couponName"></el-table-column>
              <el-table-column :label="$t('payFeeCoupon.quantity')" prop="quantity">
                <template slot-scope="scope">
                  {{ scope.row.quantity }}张
                </template>
              </el-table-column>
              <el-table-column :label="$t('payFeeCoupon.purpose')" prop="toTypeName"></el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { computePayFeeCoupon } from '@/api/fee/payFeeOrderApi'

export default {
  name: 'PayFeeCoupon',
  data() {
    return {
      communityId: '',
      payFeeCouponInfo: {
        feeCoupons: [],
        feeId: '',
        communityId: '',
        cycles: 1,
        endTime: ''
      }
    }
  },
  created() {
    this.payFeeCouponInfo.communityId = getCommunityId()
  },
  methods: {
    // 父组件调用：计算赠券
    computeFeeCoupon(params) {
      this.payFeeCouponInfo.feeId = params.feeId
      this.payFeeCouponInfo.cycles = params.cycles || 1
      this.payFeeCouponInfo.endTime = params.endTime
      this._listFeeCoupons(1, 20)
    },
    async _listFeeCoupons(page, row) {
      const params = {
        page,
        row,
        feeId: this.payFeeCouponInfo.feeId,
        communityId: this.communityId,
        cycles: this.payFeeCouponInfo.cycles,
        endTime: this.payFeeCouponInfo.endTime
      }
      try {
        const { data } = await computePayFeeCoupon(params)
        this.payFeeCouponInfo.feeCoupons = data
      } catch (error) {
        console.error('请求失败处理')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.ibox {
  margin-bottom: 20px;
}
</style>