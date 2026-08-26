<template>
  <div class="property-fee-container">


    <!-- 主费用信息组件 -->
    <view-main-fee ref="viewMainFee" :fee-name="$t('propertyFee.feeName')" fee-type-cd="888800010001"
      pay-name="propertyPay" />

    <el-card class="box-card" style="margin-top: 20px;">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('propertyFee.paymentHistory') }}</span>
      </div>

      <el-row :gutter="20">
        <el-col :span="6">
          <el-date-picker v-model="feeDetailInfo.startTime" type="date" :placeholder="$t('propertyFee.selectStartTime')"
            style="width: 100%;" />
        </el-col>
        <el-col :span="6">
          <el-date-picker v-model="feeDetailInfo.endTime" type="date" :placeholder="$t('propertyFee.selectEndTime')"
            style="width: 100%;" />
        </el-col>
        <el-col :span="12">
          <el-button type="primary" @click="queryFeeDetailMethod()">
            <i class="el-icon-search"></i>
            {{ $t('propertyFee.searchNow') }}
          </el-button>
          <el-button type="info" @click="resetFeeDetailMethod()" style="margin-left: 20px;">
            <i class="el-icon-refresh"></i>
            {{ $t('propertyFee.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <el-table :data="feeDetailInfo.feeDetails" style="width: 100%; margin-top: 20px;" border stripe>
        <el-table-column prop="detailId" :label="$t('propertyFee.paymentId')" align="center" />
        <el-table-column prop="cycles" :label="$t('propertyFee.cycle')" align="center" />
        <el-table-column prop="primeRateName" :label="$t('propertyFee.paymentMethod')" align="center" />
        <el-table-column :label="$t('propertyFee.receivableAmount')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.state == '1500'">{{ scope.row.payerObjName }}{{ $t('propertyFee.viewInFee') }}</span>
            <span v-else>{{ scope.row.receivableAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('propertyFee.receivedAmount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.receivedAmount }}<br>
            <div v-if="scope.row.acctAmount > 0">
              {{ $t('propertyFee.accountDeduction') }}: {{ scope.row.acctAmount }}<br>
            </div>
            <div v-for="(item, index) in scope.row.payFeeDetailDiscountDtoList" :key="index">
              {{ item.discountName }}: {{ Math.abs(item.discountPrice) }}<br>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('propertyFee.paymentTime')" align="center" />
        <el-table-column :label="$t('propertyFee.startTime')" align="center">
          <template slot-scope="scope">
            {{ dateFormat(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('propertyFee.endTime')" align="center">
          <template slot-scope="scope">
            {{ dateFormat(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="cashierName" :label="$t('propertyFee.cashier')" align="center" />
        <el-table-column prop="stateName" :label="$t('propertyFee.status')" align="center" />
        <el-table-column prop="remark" :label="$t('propertyFee.remark')" align="center" />
        <el-table-column :label="$t('propertyFee.operation')" align="center">
          <template slot-scope="scope">
            <el-button-group>
              <el-button v-if="scope.row.state == '1400' || scope.row.state == '1200' || scope.row.state == ''"
                size="mini" @click="_openRefundModel(scope.row)">
                {{ $t('propertyFee.applyRefund') }}
              </el-button>
              <el-button v-if="scope.row.state != '1500'" size="mini" @click="_openFeeDetailDiscountModal(scope.row)">
                {{ $t('propertyFee.discount') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" style="margin-top: 20px;" />
    </el-card>

    <!-- 子组件 -->
    <return-pay-fee ref="returnPayFee" @success="queryFeeDetailMethod" />
    <view-fee-detail-discount ref="viewFeeDetailDiscount" />

  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryFeeDetail } from '@/api/fee/propertyFeeApi'
import ViewMainFee from '@/components/fee/viewMainFee'
import ReturnPayFee from '@/components/fee/returnPayFee'
import ViewFeeDetailDiscount from '@/components/fee/viewFeeDetailDiscount'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'PropertyFeeList',
  components: {
    ViewMainFee,
    ReturnPayFee,
    ViewFeeDetailDiscount
  },
  data() {
    return {
      feeDetailInfo: {
        feeDetails: [],
        total: 0,
        records: 1,
        feeId: '',
        startTime: '',
        endTime: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.feeDetailInfo.feeId = this.$route.query.feeId
    this.queryFeeDetailMethod()
  },
  methods: {
    dateFormat(date) {
      if (!date) return ''
      return dateFormat(date)
    },
    async listFeeDetail(page = 1, row = 10) {
      try {
        const params = {
          page,
          row,
          communityId: this.communityId,
          feeId: this.feeDetailInfo.feeId,
          startTime: this.feeDetailInfo.startTime,
          endTime: this.feeDetailInfo.endTime
        }

        const { data: feeDetails, total, records } = await queryFeeDetail(params)
        this.feeDetailInfo.feeDetails = feeDetails
        this.feeDetailInfo.total = total
        this.feeDetailInfo.records = records
        this.page.total = records
      } catch (error) {
        console.error('获取费用明细失败:', error)
      }
    },
    queryFeeDetailMethod() {
      this.page.current = 1
      this.listFeeDetail()
    },
    resetFeeDetailMethod() {
      this.feeDetailInfo.startTime = ''
      this.feeDetailInfo.endTime = ''
      this.queryFeeDetailMethod()
    },
    _openRefundModel(feeDetail) {
      this.$refs.returnPayFee.open(feeDetail)
    },
    _openFeeDetailDiscountModal(detail) {
      this.$refs.viewFeeDetailDiscount.open(detail)
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listFeeDetail(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listFeeDetail(val, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.property-fee-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .clearfix {
    font-size: 18px;
    font-weight: bold;
  }
}
</style>