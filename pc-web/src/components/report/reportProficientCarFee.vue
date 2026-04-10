<template>
  <div class="report-proficient-car-fee">
    <div class="table-container">
      <el-table :data="reportProficientCarFeeInfo.fees" border style="width: 100%" >
        <!-- 固定列：基本信息 -->
        <el-table-column prop="ownerName" :label="$t('reportProficientCarFee.name')" align="center" fixed="left" width="120">
        </el-table-column>
        <el-table-column prop="objName" :label="$t('reportProficientCarFee.carNumber')" align="center" fixed="left" width="120">
        </el-table-column>
        <el-table-column prop="ownerLink" :label="$t('reportProficientCarFee.phone')" align="center" fixed="left" width="120">
        </el-table-column>
        <el-table-column prop="feeTypeCdName" :label="$t('reportProficientCarFee.feeType')" align="center" fixed="left" width="120">
        </el-table-column>
        <el-table-column prop="feeName" :label="$t('reportProficientCarFee.feeName')" align="center" fixed="left" width="120">
        </el-table-column>
        
        <!-- 动态列：年度费用数据 -->
        <el-table-column v-for="(item, index) in reportProficientCarFeeInfo.listColumns" :key="index"
          :label="item + $t('reportProficientCarFee.year')" align="center" >
          <template slot-scope="scope">
            <el-link type="primary" @click="_showCarFeeDetail(scope.row, item)">
              {{ _getProficientCarFeeValue(scope.row.reportFeeYearCollectionDetailDtos, item) }}
            </el-link>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-row :gutter="20" class="margin-top">
      <el-col :span="12">
        <div class="tip-text">
          {{ $t('reportProficientCarFee.tip') }}
        </div>
      </el-col>
      <el-col :span="12">
        <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
          :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
        </el-pagination>
      </el-col>
    </el-row>
    <view-fee-detail ref="viewFeeDetail"></view-fee-detail>
  </div>
</template>

<script>
import { queryReportFeeYear } from '@/api/report/reportProficientApi'
import ViewFeeDetail from '@/components/report/viewFeeDetail' 

export default {
  name: 'ReportProficientCarFee',
  components: {
    ViewFeeDetail
  },
  props: {
    conditions: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      reportProficientCarFeeInfo: {
        fees: [],
        listColumns: [2023]
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
  },
  methods: {
    open(params) {
      this.conditions = params
      this.listReportProficientCarFee()
    },
    handleSwitch(params) {
      console.log(params)
      this.clearReportProficientCarFeeInfo()
      this.listReportProficientCarFee()
    },
    async listReportProficientCarFee() {
      try {
        const params = {
          ...this.conditions,
          page: this.pagination.current,
          row: this.pagination.size,
          objType: '6666'
        }
        const { data, records } = await queryReportFeeYear(params)

        this.reportProficientCarFeeInfo.fees = data
        this.pagination.total = records

        if (data && data.length > 0) {
          this.reportProficientCarFeeInfo.listColumns = []
          let maxColumns = data[0].reportFeeYearCollectionDetailDtos

          data.forEach(item => {
            if (item.reportFeeYearCollectionDetailDtos.length > maxColumns.length) {
              maxColumns = item.reportFeeYearCollectionDetailDtos
            }
          })

          maxColumns.forEach(item => {
            this.reportProficientCarFeeInfo.listColumns.push(item.collectionYear)
          })
        }
      } catch (error) {
        console.error('获取车辆费台账失败:', error)
      }
    },
    _getProficientCarFeeValue(reportFeeYearCollectionDetailDtos, year) {
      const item = reportFeeYearCollectionDetailDtos.find(d => d.collectionYear === year)
      return item ? item.receivedAmount : '0.00'
    },
    clearReportProficientCarFeeInfo() {
      this.reportProficientCarFeeInfo = {
        fees: [],
        listColumns: [2023]
      }
    },
    _showCarFeeDetail(fee, year) {
      this.$refs.viewFeeDetail.open({
        roomName: fee.objName,
        feeId: fee.feeId,
        configId: fee.configId,
        payerObjId: fee.objId,
        curYear: year
      })
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.listReportProficientCarFee()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.listReportProficientCarFee()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-proficient-car-fee {
  .table-container {
    overflow-x: auto;
    
    // 确保表格容器有足够的宽度来显示滚动条
    .el-table {
      min-width: 100%;
      
      // 设置表格的最小宽度，确保横向滚动生效
      &::v-deep .el-table__body-wrapper {
        overflow-x: auto;
      }
    }
  }

  .margin-top {
    margin-top: 20px;
  }

  .tip-text {
    padding: 10px;
    color: #999;
    font-size: 12px;
  }
}
</style>