<template>
  <div class="margin-top">
    <el-table :data="aFeeDetailDiscountInfo.applyRoomDiscounts" border style="width: 100%">
      <el-table-column prop="roomName" :label="$t('aFeeDetailDiscount.roomName')" align="center"></el-table-column>
      <el-table-column prop="discountId" :label="$t('aFeeDetailDiscount.discountId')" align="center"></el-table-column>
      <el-table-column prop="discountName" :label="$t('aFeeDetailDiscount.discountName')" align="center"></el-table-column>
      <el-table-column prop="applyTypeName" :label="$t('aFeeDetailDiscount.applyType')" align="center"></el-table-column>
      <el-table-column prop="createUserName" :label="$t('aFeeDetailDiscount.applicant')" align="center"></el-table-column>
      <el-table-column prop="createUserTel" :label="$t('aFeeDetailDiscount.applicantPhone')" align="center"></el-table-column>
      <el-table-column prop="startTime" :label="$t('aFeeDetailDiscount.startTime')" align="center"></el-table-column>
      <el-table-column prop="endTime" :label="$t('aFeeDetailDiscount.endTime')" align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('aFeeDetailDiscount.status')" align="center"></el-table-column>
      <el-table-column prop="createTime" :label="$t('aFeeDetailDiscount.createTime')" align="center"></el-table-column>
      <el-table-column :label="$t('aFeeDetailDiscount.useStatus')" align="center">
        <template #default="{row}">{{row.inUse === '0' ? $t('aFeeDetailDiscount.notUsed') : $t('aFeeDetailDiscount.used')}}</template>
      </el-table-column>
      <el-table-column :label="$t('aFeeDetailDiscount.returnType')" align="center">
        <template #default="{row}">
          <span v-if="row.discountId">
            {{row.returnWay === '1002' ? $t('aFeeDetailDiscount.accountBalance') : $t('aFeeDetailDiscount.discount')}}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('aFeeDetailDiscount.returnAmount')" align="center">
        <template #default="{row}">{{row.returnAmount ? row.returnAmount : '-'}}</template>
      </el-table-column>
    </el-table>

    <el-row class="margin-top">
      <el-col :span="12"></el-col>
      <el-col :span="12">
        <el-pagination
          @current-change="handlePageChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryAdminApplyRoomDiscount } from '@/api/aCommunity/aFeeDetailDiscountApi'

export default {
  name: 'AFeeDetailDiscount',
  data() {
    return {
      aFeeDetailDiscountInfo: {
        applyRoomDiscounts: [],
        feeId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.aFeeDetailDiscountInfo.feeId = data.feeId
      this._loadAFeeDetailDiscountData()
    },
    handleSwitch(data) {
      this.aFeeDetailDiscountInfo.feeId = data.feeId
      this._loadAFeeDetailDiscountData()
    },
    handlePageChange(page) {
      this.currentPage = page
      this._loadAFeeDetailDiscountData()
    },
    async _loadAFeeDetailDiscountData() {
      try {
        const params = {
          feeId: this.aFeeDetailDiscountInfo.feeId,
          page: this.currentPage,
          row: this.pageSize
        }
        
        const res = await queryAdminApplyRoomDiscount(params)
        this.aFeeDetailDiscountInfo.applyRoomDiscounts = res.data
        this.total = res.total
      } catch (error) {
        console.error('加载费用折扣数据失败:', error)
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