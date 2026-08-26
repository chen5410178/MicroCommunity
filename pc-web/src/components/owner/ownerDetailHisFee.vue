<template>
  <div>
    <div class="margin-top">
      <el-table
        :data="ownerDetailHisFeeInfo.feeDetails"
        style="width: 100%; margin-top: 10px"
        border
        stripe
      >
        <el-table-column prop="feeName" :label="$t('ownerDetailHisFee.feeItem')" align="center"></el-table-column>
        <el-table-column prop="payerObjName" :label="$t('ownerDetailHisFee.chargeObject')" align="center"></el-table-column>
        <el-table-column prop="cycles" :label="$t('ownerDetailHisFee.cycle')" align="center"></el-table-column>
        <el-table-column :label="$t('ownerDetailHisFee.receivableAmount')" align="center">
          <template slot-scope="scope">
            {{scope.row.receivableAmount}}/{{scope.row.receivedAmount}}<br>
            <div v-if="scope.row.acctAmount>0">
              {{$t('ownerDetailHisFee.accountDeduction')}}: {{scope.row.acctAmount}}<br>
            </div>
            <div v-for="(item,index) in scope.row.payFeeDetailDiscountDtoList" :key="index">
              {{item.discountName}}: {{Math.abs(item.discountPrice)}}<br>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="primeRateName" :label="$t('ownerDetailHisFee.paymentMethod')" align="center"></el-table-column>
        <el-table-column :label="$t('ownerDetailHisFee.paymentPeriod')" align="center">
          <template slot-scope="scope">
            {{dateFormat(scope.row.startTime)}}~<br>
            {{dateFormat(scope.row.endTime)}}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('ownerDetailHisFee.paymentTime')" align="center"></el-table-column>
        <el-table-column prop="cashierName" :label="$t('ownerDetailHisFee.cashier')" align="center">
          <template slot-scope="scope">
            {{scope.row.cashierName || '-'}}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('ownerDetailHisFee.status')" align="center"></el-table-column>
        <el-table-column prop="remark" :label="$t('ownerDetailHisFee.remark')" align="center"></el-table-column>
        <el-table-column :label="$t('ownerDetailHisFee.operation')" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.state=='1400' || scope.row.state== '1200' || scope.row.state== ''"
              type="text"
              size="small"
              @click="_toRefundFee(scope.row)"
            >
              {{$t('ownerDetailHisFee.detail')}}
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
  name: 'OwnerDetailHisFee',
  data() {
    return {
      ownerDetailHisFeeInfo: {
        feeDetails: [],
        ownerId: ''
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
    open(ownerId) {
      this.clearOwnerDetailHisFeeInfo()
      this.ownerDetailHisFeeInfo.ownerId = ownerId
      this._listOwnerDetailFeeDetails(this.currentPage, this.pageSize)
    },
    async _listOwnerDetailFeeDetails(page, row) {
      try {
        const param = {
          page: page,
          row: row,
          communityId: this.communityId,
          ownerId: this.ownerDetailHisFeeInfo.ownerId
        }

        const { data: feeDetails, total } = await queryFeeDetail(param)
        this.ownerDetailHisFeeInfo.feeDetails = feeDetails
        this.total = total
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    clearOwnerDetailHisFeeInfo() {
      this.ownerDetailHisFeeInfo = {
        feeDetails: [],
        ownerId: ''
      }
    },
    _toRefundFee(detail) {
      this.$router.push(`/views/fee/propertyFee?feeId=${detail.feeId}`)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listOwnerDetailFeeDetails(val, this.pageSize)
    },
    dateFormat(date) {
      // Implement your date formatting logic here
      return date
    }
  }
}
</script>