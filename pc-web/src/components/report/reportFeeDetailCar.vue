<template>
  <div class="report-fee-detail-car">
    <div class="operation-bar">
      <el-button type="primary" size="small" @click="exportReportFeeDetailCarExcel">
        <i class="el-icon-download"></i>
        {{ $t('common.export') }}
      </el-button>
    </div>

    <el-table :data="reportFeeDetailCarInfo.fees" border style="width: 100%" v-loading="loading">
      <el-table-column prop="carNum" :label="$t('reportFeeDetailCar.car')" align="center">
      </el-table-column>
      <el-table-column prop="ownerName" :label="$t('reportFeeDetailCar.owner')" align="center">
        <template slot-scope="scope">
          {{ scope.row.ownerName }}({{ scope.row.link }})
        </template>
      </el-table-column>
      <el-table-column prop="oweFee" :label="$t('reportFeeDetailCar.oweFee')" align="center">
        <template slot-scope="scope">
          {{ scope.row.oweFee || '0' }}
        </template>
      </el-table-column>
      <el-table-column prop="receivedFee" :label="$t('reportFeeDetailCar.receivedFee')" align="center">
        <template slot-scope="scope">
          {{ scope.row.receivedFee || '0' }}
        </template>
      </el-table-column>
      <template v-for="(item, index) in reportFeeDetailCarInfo.feeTypeCds">
        <el-table-column :key="index + 'owe'" :label="item.name + $t('reportFeeDetailCar.owe')" align="center">
          <template slot-scope="scope">
            {{ scope.row['oweFee' + item.statusCd] || '0' }}
          </template>
        </el-table-column>
        <el-table-column :key="index + 'received'" :label="item.name + $t('reportFeeDetailCar.received')" align="center">
          <template slot-scope="scope">
            {{ scope.row['receivedFee' + item.statusCd] || '0' }}
          </template>
        </el-table-column>
      </template>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
    </el-pagination>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { queryReportFeeDetailCar, exportReportFeeDetailCar } from '@/api/report/reportFeeDetailApi'

export default {
  name: 'ReportFeeDetailCar',
  data() {
    return {
      reportFeeDetailCarInfo: {
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
        this.reportFeeDetailCarInfo.feeTypeCds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async open(conditions) {
      this.reportFeeDetailCarInfo.conditions = conditions
      this.listReportFeeDetailCars()
    },
    async listReportFeeDetailCars() {
      try {
        this.loading = true
        const params = {
          ...this.reportFeeDetailCarInfo.conditions,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await queryReportFeeDetailCar(params)
        this.reportFeeDetailCarInfo.fees = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取车辆费用明细失败:', error)
      } finally {
        this.loading = false
      }
    },
    async exportReportFeeDetailCarExcel() {
      try {
        const params = {
          ...this.reportFeeDetailCarInfo.conditions,
          pagePath: 'reportFeeDetailCar'
        }
        await exportReportFeeDetailCar(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')

      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error(this.$t('common.exportFailed'))
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.listReportFeeDetailCars()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.listReportFeeDetailCars()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-fee-detail-car {
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