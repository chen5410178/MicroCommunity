<template>
  <div class="report-fee-detail-owner">
    <div class="operation-bar">
      <el-button 
        type="primary" 
        size="small"
        @click="exportReportFeeDetailOwnerExcel">
        <i class="el-icon-download"></i>
        {{ $t('common.export') }}
      </el-button>
    </div>

    <el-table
      :data="reportFeeDetailOwnerInfo.fees"
      border
      style="width: 100%"
      v-loading="loading">
      <el-table-column 
        prop="ownerName" 
        :label="$t('reportFeeDetailOwner.owner')"
        align="center">
        <template slot-scope="scope">
          {{ scope.row.ownerName }}({{ scope.row.link }})
        </template>
      </el-table-column>
      <el-table-column 
        prop="objName" 
        :label="$t('reportFeeDetailOwner.room')"
        align="center">
        <template slot-scope="scope">
          <div style="max-width: 200px;">{{ scope.row.objName }}</div>
        </template>
      </el-table-column>
      <el-table-column 
        prop="oweFee" 
        :label="$t('reportFeeDetailOwner.oweFee')"
        align="center">
        <template slot-scope="scope">
          {{ scope.row.oweFee || '0' }}
        </template>
      </el-table-column>
      <el-table-column 
        prop="receivedFee" 
        :label="$t('reportFeeDetailOwner.receivedFee')"
        align="center">
        <template slot-scope="scope">
          {{ scope.row.receivedFee || '0' }}
        </template>
      </el-table-column>
      <template v-for="(item,index) in reportFeeDetailOwnerInfo.feeTypeCds">
        <el-table-column 
          :key="index+'owe'"
          :label="item.name + $t('reportFeeDetailOwner.owe')"
          align="center">
          <template slot-scope="scope">
            {{ scope.row['oweFee'+item.statusCd] || '0' }}
          </template>
        </el-table-column>
        <el-table-column 
          :key="index+'received'"
          :label="item.name + $t('reportFeeDetailOwner.received')"
          align="center">
          <template slot-scope="scope">
            {{ scope.row['receivedFee'+item.statusCd] || '0' }}
          </template>
        </el-table-column>
      </template>
    </el-table>

    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.current"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total">
    </el-pagination>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { queryReportFeeDetailOwner, exportReportFeeDetailOwner } from '@/api/report/reportFeeDetailApi'

export default {
  name: 'ReportFeeDetailOwner',
  data() {
    return {
      reportFeeDetailOwnerInfo: {
        fees: [],
        feeTypeCds: [],
        conditions: {}
      },
      loading: false,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getDictData()
  },
  methods: {
    async getDictData() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd_show')
        this.reportFeeDetailOwnerInfo.feeTypeCds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async open(conditions) {
      this.reportFeeDetailOwnerInfo.conditions = conditions
      this.listReportFeeDetailOwners()
    },
    async listReportFeeDetailOwners() {
      try {
        this.loading = true
        const params = {
          ...this.reportFeeDetailOwnerInfo.conditions,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await queryReportFeeDetailOwner(params)
        this.reportFeeDetailOwnerInfo.fees = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取业主费用明细失败:', error)
      } finally {
        this.loading = false
      }
    },
    async exportReportFeeDetailOwnerExcel() {
      try {
        const params = {
          ...this.reportFeeDetailOwnerInfo.conditions,
          pagePath: 'reportFeeDetailOwner'
        }
        await exportReportFeeDetailOwner(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')

      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error(this.$t('common.exportFailed'))
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.listReportFeeDetailOwners()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.listReportFeeDetailOwners()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-fee-detail-owner {
  .operation-bar {
    margin-bottom: 20px;
    text-align: right;
  }
  
  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>