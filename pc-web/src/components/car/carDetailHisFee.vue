<template>
  <div>
    <div class="margin-top">
      <el-table
        :data="carDetailHisFeeInfo.feeDetails"
        style="width: 100%; margin-top: 10px"
        border
        stripe
      >
        <el-table-column prop="feeName" :label="$t('carDetailHisFee.feeItem')" align="center"></el-table-column>
        <el-table-column prop="payerObjName" :label="$t('carDetailHisFee.chargeObject')" align="center"></el-table-column>
        <el-table-column prop="cycles" :label="$t('carDetailHisFee.cycle')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailHisFee.receivableAmount')" align="center">
          <template slot-scope="scope">
            {{scope.row.receivableAmount}}/{{scope.row.receivedAmount}}<br>
            <div v-if="scope.row.acctAmount>0">
              {{$t('carDetailHisFee.accountDeduction')}}: {{scope.row.acctAmount}}<br>
            </div>
            <div v-for="(item,index) in scope.row.payFeeDetailDiscountDtoList" :key="index">
              {{item.discountName}}: {{Math.abs(item.discountPrice)}}<br>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="primeRateName" :label="$t('carDetailHisFee.paymentMethod')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailHisFee.paymentPeriod')" align="center">
          <template slot-scope="scope">
            {{dateFormat(scope.row.startTime)}}~<br>
            {{dateFormat(scope.row.endTime)}}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('carDetailHisFee.paymentTime')" align="center"></el-table-column>
        <el-table-column prop="cashierName" :label="$t('carDetailHisFee.cashier')" align="center">
          <template slot-scope="scope">
            {{scope.row.cashierName || '-'}}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('carDetailHisFee.status')" align="center"></el-table-column>
        <el-table-column prop="remark" :label="$t('carDetailHisFee.remark')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailHisFee.operation')" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.state=='1400' || scope.row.state== '1200' || scope.row.state== ''"
              type="text"
              size="small"
              @click="_toRefundFee(scope.row)"
            >
              {{$t('carDetailHisFee.detail')}}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { queryFeeDetail } from '@/api/owner/ownerDetailHisFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CarDetailHisFee',
  data() {
    return {
      carDetailHisFeeInfo: {
        feeDetails: [],
        carId: '',
        carNum: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.clearCarDetailHisFeeInfo()
      this.carDetailHisFeeInfo.carId = data.carId
      this.carDetailHisFeeInfo.carNum = data.carNum
      this._listFeeDetails(this.currentPage, this.pageSize)
    },
    switch(data) {
      this.carDetailHisFeeInfo.carId = data.carId
      this.carDetailHisFeeInfo.carNum = data.carNum
      this._listFeeDetails(this.currentPage, this.pageSize)
    },
    async _listFeeDetails(page, row) {
      try {
        const param = {
          page: page,
          row: row,
          communityId: this.communityId,
          payerObjId: this.carDetailHisFeeInfo.carId,
          payerObjType: '5555'
        }

        const { data: feeDetails, total } = await queryFeeDetail(param)
        this.carDetailHisFeeInfo.feeDetails = feeDetails
        this.total = total
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    clearCarDetailHisFeeInfo() {
      this.carDetailHisFeeInfo = {
        feeDetails: [],
        carId: '',
        carNum: ''
      }
    },
    _toRefundFee(detail) {
      this.$router.push(`/views/fee/propertyFee?feeId=${detail.feeId}`)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listFeeDetails(val, this.pageSize)
    },
    dateFormat(date) {
      return date
    }
  }
}
</script>
