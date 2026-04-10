<template>
  <div class="report-deadline-fee">
    <div class="text-right">
      <el-button 
        type="primary" 
        size="small" 
        @click="_exportExcel"
      >
        <i class="el-icon-download"></i>
        {{ $t('reportDeadlineFee.export') }}
      </el-button>
    </div>

    <el-table
      :data="reportDeadlineFeeInfo.fees"
      border
      style="width: 100%"
      class="margin-top"
    >
      <el-table-column
        prop="index"
        :label="$t('reportDeadlineFee.feeId')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column
        prop="objName"
        :label="$t('reportDeadlineFee.objName')"
        align="center"
      />
      <el-table-column
        prop="feeName"
        :label="$t('reportDeadlineFee.feeName')"
        align="center"
      />
      <el-table-column
        prop="deadlineTime"
        :label="$t('reportDeadlineFee.deadlineTime')"
        align="center"
      />
      <el-table-column
        prop="oweDay"
        :label="$t('reportDeadlineFee.oweDay')"
        align="center"
      />
    </el-table>

    <el-row class="margin-top">
      <el-col :span="18">
        <div class="tip-text">
          {{ $t('reportDeadlineFee.tip') }}
        </div>
      </el-col>
      <el-col :span="6" class="text-right">
        <el-pagination
          :current-page.sync="pagination.current"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryDeadlineFee, exportData } from '@/api/report/feeRemindApi'

export default {
  name: 'ReportDeadlineFee',
  data() {
    return {
      reportDeadlineFeeInfo: {
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
      this.reportDeadlineFeeInfo.conditions = { ...conditions }
      await this._listDeadlineFees(this.pagination.current, this.pagination.size)
    },
    async _listDeadlineFees(page, size) {
      try {
        const params = {
          ...this.reportDeadlineFeeInfo.conditions,
          page,
          row: size
        }
        const { data, total } = await queryDeadlineFee(params)
        this.reportDeadlineFeeInfo.fees = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to load deadline fees:', error)
      }
    },
    async _exportExcel() {
      try {
        const params = {
          ...this.reportDeadlineFeeInfo.conditions,
          communityId: getCommunityId(),
          pagePath: 'reportDeadlineFee'
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
      this._listDeadlineFees(this.pagination.current, val)
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this._listDeadlineFees(val, this.pagination.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.report-deadline-fee {
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