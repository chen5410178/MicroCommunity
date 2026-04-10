<template>
  <div>
    <div class="margin-top">
      <el-table :data="carDetailCouponOrderInfo.orders" border style="width: 100%">
        <el-table-column prop="orderId" :label="$t('carDetailCouponOrder.orderId')" align="center"></el-table-column>
        <el-table-column prop="carNum" :label="$t('carDetailCouponOrder.carNum')" align="center"></el-table-column>
        <el-table-column prop="couponName" :label="$t('carDetailCouponOrder.coupon')" align="center"></el-table-column>
        <el-table-column prop="shopName" :label="$t('carDetailCouponOrder.shop')" align="center"></el-table-column>
        <el-table-column prop="paName" :label="$t('carDetailCouponOrder.parkingLot')" align="center"></el-table-column>
        <el-table-column prop="machineName" :label="$t('carDetailCouponOrder.machine')"
          align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('carDetailCouponOrder.verifyTime')"
          align="center"></el-table-column>
        <el-table-column prop="remark" :label="$t('carDetailCouponOrder.remark')" align="center"></el-table-column>
      </el-table>

      <el-row>
        <el-col :span="16" :offset="8">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listParkingCouponCarOrder } from '@/api/car/carDetailCouponOrderApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CarDetailCouponOrder',
  data() {
    return {
      carDetailCouponOrderInfo: {
        orders: [],
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
      this.carDetailCouponOrderInfo.carId = data.carId
      this.carDetailCouponOrderInfo.carNum = data.carNum
      this.carDetailCouponOrderInfo.paId = data.paId
      this.carDetailCouponOrderInfo.memberId = data.memberId
      this._loadCarDetailCouponOrderData(this.currentPage, this.pageSize)
    },
    switch(data) {
      this.carDetailCouponOrderInfo.carId = data.carId
      this.carDetailCouponOrderInfo.carNum = data.carNum
      this.carDetailCouponOrderInfo.paId = data.paId
      this.carDetailCouponOrderInfo.memberId = data.memberId
      this._loadCarDetailCouponOrderData(this.currentPage, this.pageSize)
    },
    _loadCarDetailCouponOrderData(page, row) {
      const params = {
        communityId: getCommunityId(),
        carNum: this.carDetailCouponOrderInfo.carNum,
        iotApiCode: 'listParkingCouponCarOrderBmoImpl',
        page,
        row
      }

      listParkingCouponCarOrder(params).then(response => {
        this.carDetailCouponOrderInfo.orders = response.data
        this.total = response.total
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadCarDetailCouponOrderData(val, this.pageSize)
    }
  }
}
</script>