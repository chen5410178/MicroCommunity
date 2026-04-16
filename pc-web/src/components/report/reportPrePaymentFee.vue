<template>
  <div class="report-pre-payment-fee">
    <div class="text-right">
      <el-button type="primary" size="small" @click="_exportExcel">
        <i class="el-icon-download"></i>
        {{ $t('reportPrePaymentFee.export') }}
      </el-button>
    </div>

    <el-table :data="reportPrePaymentFeeInfo.fees" border style="width: 100%" class="margin-top">
      <el-table-column prop="index" :label="$t('reportPrePaymentFee.feeId')" align="center">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="objName" :label="$t('reportPrePaymentFee.objName')" align="center" />
      <el-table-column prop="feeName" :label="$t('reportPrePaymentFee.feeName')" align="center" />
      <el-table-column prop="endTime" :label="$t('reportPrePaymentFee.startTime')" align="center" />
      <el-table-column prop="oweDay" :label="$t('reportPrePaymentFee.oweDay')" align="center" />
    </el-table>

    <el-row class="margin-top">
      <el-col :span="18">
        <div class="tip-text">
          {{ $t('reportPrePaymentFee.tip') }}
        </div>
      </el-col>
      <el-col :span="6" class="text-right">
        <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
          :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryPrePayment, exportData } from '@/api/report/feeRemindApi'

export default {
  name: 'ReportPrePaymentFee',
  data() {
    return {
      reportPrePaymentFeeInfo: {
        fees: [],
        conditions: {}
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    async loadData(conditions) {
      this.reportPrePaymentFeeInfo.conditions = { ...conditions }
      await this._listPreFees(this.pagination.current, this.pagination.size)
    },
    async _listPreFees(page, size) {
      try {
        const params = {
          ...this.reportPrePaymentFeeInfo.conditions,
          page,
          row: size
        }
        const { data, total } = await queryPrePayment(params)
        this.reportPrePaymentFeeInfo.fees = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to load pre payment fees:', error)
      }
    },
    async _exportExcel() {
      try {
        const params = {
          ...this.reportPrePaymentFeeInfo.conditions,
          communityId: getCommunityId(),
          pagePath: 'reportPrePaymentFee'
        }
        const { code, msg } = await exportData(params)
        this.$message.success(msg)
        if (code === 0) {
          this.$router.push('/pages/property/downloadTempFile?tab=downloadCenter')
        }
      } catch (error) {
        console.error('Failed to export:', error)
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this._listPreFees(this.pagination.current, val)
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this._listPreFees(val, this.pagination.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.report-pre-payment-fee {
  .margin-top {
    margin-top: 15px;
  }

  .text-right {
    text-align: right;
  }

  .tip-text {
    color: #999;
    font-size: 12px;
    padding: 5px 0;
  }
}
</style>