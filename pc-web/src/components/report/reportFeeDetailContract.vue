<template>
  <div class="report-fee-detail-contract">
    <el-card class="search-card">
      <div slot="header" class="text-left">
        <span>查询条件</span>
      </div>
      <el-form :model="conditions" label-width="0">
        <el-row :gutter="20">
          <el-col :span="4">
            <el-form-item>
              <el-date-picker v-model="conditions.startDate" type="date" value-format="yyyy-MM-dd" placeholder="开始日期" style="width:100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-date-picker v-model="conditions.endDate" type="date" value-format="yyyy-MM-dd" placeholder="结束日期" style="width:100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-input v-model.trim="conditions.contractName" placeholder="合同名称">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-input v-model.trim="conditions.ownerName" placeholder="业主姓名">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-input v-model.trim="conditions.link" placeholder="联系电话">
              </el-input>
            </el-form-item>
          </el-col>
           <el-col :span="4">
            <el-form-item>
              <el-button type="primary" @click="queryMethod">查询</el-button>
              <el-button @click="resetMethod">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="content-card">
      <div slot="header" class="clearfix flex justify-between">
        <span>合同费用明细</span>
        <el-button type="primary" size="small" class="float-right" @click="exportReportFeeDetailContractExcel">
          <i class="el-icon-download"></i>
          {{ $t('common.export') }}
        </el-button>
      </div>

      <el-table :data="reportFeeDetailContractInfo.fees" border style="width: 100%" v-loading="loading">
        <el-table-column prop="contractName" :label="$t('reportFeeDetailContract.contract')" align="center">
        </el-table-column>
        <el-table-column prop="ownerName" :label="$t('reportFeeDetailContract.owner')" align="center">
          <template slot-scope="scope">
            {{ scope.row.ownerName }}({{ scope.row.link }})
          </template>
        </el-table-column>
        <el-table-column prop="oweFee" :label="$t('reportFeeDetailContract.oweFee')" align="center">
          <template slot-scope="scope">
            {{ scope.row.oweFee || '0' }}
          </template>
        </el-table-column>
        <el-table-column prop="receivedFee" :label="$t('reportFeeDetailContract.receivedFee')" align="center">
          <template slot-scope="scope">
            {{ scope.row.receivedFee || '0' }}
          </template>
        </el-table-column>
        <template v-for="(item,index) in reportFeeDetailContractInfo.feeTypeCds">
          <el-table-column :key="index+'owe'" :label="item.name + $t('reportFeeDetailContract.owe')" align="center">
            <template slot-scope="scope">
              {{ scope.row['oweFee'+item.statusCd] || '0' }}
            </template>
          </el-table-column>
          <el-table-column :key="index+'received'" :label="item.name + $t('reportFeeDetailContract.received')" align="center">
            <template slot-scope="scope">
              {{ scope.row['receivedFee'+item.statusCd] || '0' }}
            </template>
          </el-table-column>
        </template>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import { getDict, getCommunityId } from '@/api/community/communityApi'
import { queryReportFeeDetailContract, exportReportFeeDetailContract } from '@/api/report/reportFeeDetailApi'

export default {
  name: 'ReportFeeDetailContract',
  data() {
    return {
      reportFeeDetailContractInfo: {
        fees: [],
        feeTypeCds: []
      },
      conditions: {
        startDate: '',
        endDate: '',
        contractName: '',
        ownerName: '',
        link: '',
        communityId: ''
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
    this.initData()
  },
  methods: {
    initData() {
      this.initDate()
      this.conditions.communityId = getCommunityId()
      this.getDictData()
      this.listReportFeeDetailContracts()
    },
    initDate() {
      const now = new Date()
      const year = now.getFullYear()
      let month = now.getMonth() + 1
      month = month < 10 ? `0${month}` : month
      this.conditions.startDate = `${year}-${month}-01`
      const nextMonth = new Date(year, month, 1)
      const nextYear = nextMonth.getFullYear()
      let nextMonthNum = nextMonth.getMonth() + 1
      nextMonthNum = nextMonthNum < 10 ? `0${nextMonthNum}` : nextMonthNum
      this.conditions.endDate = `${nextYear}-${nextMonthNum}-01`
    },
    async getDictData() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd_show')
        this.reportFeeDetailContractInfo.feeTypeCds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async listReportFeeDetailContracts() {
      try {
        this.loading = true
        const params = {
          ...this.conditions,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await queryReportFeeDetailContract(params)
        this.reportFeeDetailContractInfo.fees = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取合同费用明细失败:', error)
      } finally {
        this.loading = false
      }
    },
    async exportReportFeeDetailContractExcel() {
      try {
        const params = {
          ...this.conditions,
          pagePath: 'reportFeeDetailContract'
        }
        await exportReportFeeDetailContract(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error(this.$t('common.exportFailed'))
      }
    },
    queryMethod() {
      this.pagination.current = 1
      this.listReportFeeDetailContracts()
    },
    resetMethod() {
      this.conditions = {
        startDate: '',
        endDate: '',
        contractName: '',
        ownerName: '',
        link: '',
        communityId: getCommunityId()
      }
      this.initDate()
      this.queryMethod()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.listReportFeeDetailContracts()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.listReportFeeDetailContracts()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-fee-detail-contract {
  .search-card,
  .content-card {
    margin-bottom: 20px;
  }

  .float-right {
    float: right;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>