<template>
  <div>
    <div class="margin-top">
      <el-table :data="carDetailCouponInfo.carDetailCoupons" border style="width: 100%">
        <el-table-column prop="carNum" :label="$t('carDetailCoupon.car')" align="center"></el-table-column>
        <el-table-column prop="couponName" :label="$t('carDetailCoupon.coupon')" align="center"></el-table-column>
        <el-table-column prop="shopName" :label="$t('carDetailCoupon.shop')" align="center"></el-table-column>
        <el-table-column prop="paName" :label="$t('carDetailCoupon.parkingLot')" align="center"></el-table-column>
        <el-table-column prop="giveWayName" :label="$t('carDetailCoupon.giveWay')" align="center"></el-table-column>
        <el-table-column prop="typeCdName" :label="$t('carDetailCoupon.couponType')" align="center"></el-table-column>
        <el-table-column prop="value" :label="$t('carDetailCoupon.value')" align="center"></el-table-column>
        <el-table-column prop="startTime" :label="$t('carDetailCoupon.startTime')" align="center"></el-table-column>
        <el-table-column prop="endTime" :label="$t('carDetailCoupon.endTime')" align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('carDetailCoupon.giveTime')" align="center"></el-table-column>
        <el-table-column prop="stateName" :label="$t('carDetailCoupon.status')" align="center"></el-table-column>
        <el-table-column prop="remark" :label="$t('carDetailCoupon.remark')" align="center"></el-table-column>
      </el-table>

      <el-row>
        <el-col :span="16" :offset="8">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listParkingCouponCar } from '@/api/car/carDetailCouponApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CarDetailCoupon',
  data() {
    return {
      carDetailCouponInfo: {
        carDetailCoupons: [],
        carId: '',
        memberId: '',
        carNum: '',
        paId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.carDetailCouponInfo.carId = data.carId
      this.carDetailCouponInfo.carNum = data.carNum
      this.carDetailCouponInfo.paId = data.paId
      this.carDetailCouponInfo.memberId = data.memberId
      this._loadCarDetailCouponData(this.currentPage, this.pageSize)
    },
    switch(data) {
      this.carDetailCouponInfo.carId = data.carId
      this.carDetailCouponInfo.carNum = data.carNum
      this.carDetailCouponInfo.paId = data.paId
      this.carDetailCouponInfo.memberId = data.memberId
      this._loadCarDetailCouponData(this.currentPage, this.pageSize)
    },
    _loadCarDetailCouponData(page, row) {
      const params = {
        communityId: getCommunityId(),
        carNum: this.carDetailCouponInfo.carNum,
        iotApiCode: 'listParkingCouponCarBmoImpl',
        page,
        row
      }

      listParkingCouponCar(params).then(response => {
        this.carDetailCouponInfo.carDetailCoupons = response.data
        this.total = response.total
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadCarDetailCouponData(val, this.pageSize)
    }
  }
}
</script>