<template>
  <div>
    <div class="margin-top">
      <el-table
        :data="feeDetailDiscountInfo.applyRoomDiscounts"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="roomName"
          :label="$t('feeDetailDiscount.roomName')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="discountId"
          :label="$t('feeDetailDiscount.discountId')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="discountName"
          :label="$t('feeDetailDiscount.discountName')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="applyTypeName"
          :label="$t('feeDetailDiscount.applyType')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="createUserName"
          :label="$t('feeDetailDiscount.applicant')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="createUserTel"
          :label="$t('feeDetailDiscount.applicantPhone')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="startTime"
          :label="$t('feeDetailDiscount.startTime')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="endTime"
          :label="$t('feeDetailDiscount.endTime')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="stateName"
          :label="$t('feeDetailDiscount.status')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="createTime"
          :label="$t('feeDetailDiscount.createTime')"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="inUse"
          :label="$t('feeDetailDiscount.useStatus')"
          align="center"
        >
          <template #default="{row}">
            {{row.inUse === '0' ? $t('feeDetailDiscount.notUsed') : $t('feeDetailDiscount.used')}}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('feeDetailDiscount.returnType')"
          align="center"
        >
          <template #default="{row}">
            <span v-if="row.discountId">
              {{row.returnWay === '1002' ? $t('feeDetailDiscount.accountBalance') : $t('feeDetailDiscount.discount')}}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('feeDetailDiscount.returnAmount')"
          align="center"
        >
          <template #default="{row}">
            {{row.returnAmount ? row.returnAmount : '-'}}
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="4"></el-col>
        <el-col :span="20">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total"
          ></el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryApplyRoomDiscount } from '@/api/fee/feeDetailDiscountApi'
import { getCommunityId } from '@/api/community/communityApi' 

export default {
  name: 'FeeDetailDiscount',
  data() {
    return {
      feeDetailDiscountInfo: {
        applyRoomDiscounts: [],
        feeId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(params) {
      this.feeDetailDiscountInfo.feeId = params.feeId
      this._loadFeeDetailDiscountData(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadFeeDetailDiscountData(this.currentPage, this.pageSize)
    },
    async _loadFeeDetailDiscountData(page, row) {
      try {
        const params = {
          communityId: getCommunityId(),
          feeId: this.feeDetailDiscountInfo.feeId,
          page,
          row
        }
        
        const res = await queryApplyRoomDiscount(params)
        this.feeDetailDiscountInfo.applyRoomDiscounts = res.data
        this.total = res.total
      } catch (error) {
        console.error('Failed to load discount data:', error)
      }
    }
  }
}
</script>