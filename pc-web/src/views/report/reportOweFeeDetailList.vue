<template>
  <div class="report-owe-fee-detail-container animated fadeInRight">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('reportOweFeeDetail.searchCondition') }}</span>
          </div>
          <el-form :inline="true" :model="reportOweFeeDetailInfo.conditions" class="search-form text-left">
            <el-form-item>
              <el-input v-model="reportOweFeeDetailInfo.conditions.objName"
                :placeholder="$t('reportOweFeeDetail.inputRoomNum')" clearable>
              </el-input>
            </el-form-item>

            <el-form-item>
              <el-input v-model="reportOweFeeDetailInfo.conditions.ownerName"
                :placeholder="$t('reportOweFeeDetail.inputOwnerName')" clearable>
              </el-input>
            </el-form-item>

            <el-form-item>
              <el-input v-model="reportOweFeeDetailInfo.conditions.link"
                :placeholder="$t('reportOweFeeDetail.inputPhone')" clearable>
              </el-input>
            </el-form-item>

            <el-form-item>
              <el-date-picker v-model="reportOweFeeDetailInfo.conditions.startDate" type="date"
                :placeholder="$t('reportOweFeeDetail.selectStartTime')" value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>

            <el-form-item>
              <el-date-picker v-model="reportOweFeeDetailInfo.conditions.endDate" type="date"
                :placeholder="$t('reportOweFeeDetail.selectEndTime')" value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="_qureyDataReportOweDetailStatistics">
                <i class="el-icon-search"></i>
                {{ $t('reportOweFeeDetail.search') }}
              </el-button>
              <el-button @click="_resetDataReportOweDetailStatistics" style="margin-left: 10px;">
                <i class="el-icon-refresh"></i>
                {{ $t('reportOweFeeDetail.reset') }}
              </el-button>
            </el-form-item>

            <el-form-item style="float: right;">
              <el-button type="primary" @click="_exportReportOweDetailExcel">
                <i class="el-icon-download"></i>
                {{ $t('reportOweFeeDetail.export') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card style="margin-top: 20px;">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('reportOweFeeDetail.oweFeeDetail') }}</span>
          </div>

          <el-table :data="reportOweFeeDetailInfo.fees" border style="width: 100%" v-loading="loading">
            <el-table-column prop="roomName" :label="$t('reportOweFeeDetail.room')" align="center" />
            <el-table-column prop="ownerName" :label="$t('reportOweFeeDetail.owner')" align="center" />
            <el-table-column prop="oweFee" :label="$t('reportOweFeeDetail.oweAmount')" align="center" />
            <el-table-column v-for="(item, index) in reportOweFeeDetailInfo.feeTypeCds" :key="index"
              :label="item.name" align="center">
              <template slot-scope="scope">
                <div v-if="scope.row['oweFee' + item.statusCd]">
                  <div v-for="(feeItem, fIndex) in scope.row['oweFee' + item.statusCd]" :key="fIndex">
                    {{ feeItem.feeName }}({{ feeItem.endTime }}~{{ feeItem.deadlineTime }})={{ feeItem.amountOwed }}
                  </div>
                </div>
                <div v-else>
                  0
                </div>
              </template>
            </el-table-column>
          </el-table>

          <el-row class="margin-top">
            <el-col :span="24" class="text-right">
              <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
                layout="total, sizes, prev, pager, next, jumper" :total="page.total">
              </el-pagination>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { queryOweDetailStatistics, exportData } from '@/api/report/dataReportApi'

export default {
  name: 'ReportOweFeeDetailList',
  components: {},
  data() {
    return {
      loading: false,
      reportOweFeeDetailInfo: {
        fees: [],
        feeTypeCds: [],
        conditions: {
          objName: '',
          ownerName: '',
          link: '',
          startDate: '',
          endDate: '',
          communityId: ''
        }
      },
      page: {
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
    async initData() {
      this.reportOweFeeDetailInfo.conditions.communityId = getCommunityId()
      await this.getDictData()
      await this._loadDataReportOweDetailStatisticsData()
    },

    async getDictData() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd_show')
        this.reportOweFeeDetailInfo.feeTypeCds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    async _loadDataReportOweDetailStatisticsData() {
      this.loading = true
      try {
        const params = {
          communityId: this.reportOweFeeDetailInfo.conditions.communityId,
          startDate: this.reportOweFeeDetailInfo.conditions.startDate,
          endDate: this.reportOweFeeDetailInfo.conditions.endDate,
          objName: this.reportOweFeeDetailInfo.conditions.objName,
          ownerName: this.reportOweFeeDetailInfo.conditions.ownerName,
          link: this.reportOweFeeDetailInfo.conditions.link,
          page: this.page.current,
          row: this.page.size
        }
        const { data, records } = await queryOweDetailStatistics(params)
        this.reportOweFeeDetailInfo.fees = data
        this.page.total = records
      } catch (error) {
        console.error('获取欠费明细失败:', error)
      } finally {
        this.loading = false
      }
    },

    _qureyDataReportOweDetailStatistics() {
      this.page.current = 1
      this._loadDataReportOweDetailStatisticsData()
    },

    _resetDataReportOweDetailStatistics() {
      this.reportOweFeeDetailInfo.conditions.objName = ''
      this.reportOweFeeDetailInfo.conditions.ownerName = ''
      this.reportOweFeeDetailInfo.conditions.link = ''
      this._qureyDataReportOweDetailStatistics()
    },

    async _exportReportOweDetailExcel() {
      try {
        const params = {
          communityId: this.reportOweFeeDetailInfo.conditions.communityId,
          startDate: this.reportOweFeeDetailInfo.conditions.startDate,
          endDate: this.reportOweFeeDetailInfo.conditions.endDate,
          objName: this.reportOweFeeDetailInfo.conditions.objName,
          ownerName: this.reportOweFeeDetailInfo.conditions.ownerName,
          link: this.reportOweFeeDetailInfo.conditions.link,
          pagePath: 'dataReportOweDetailStatistics'
        }
        const res = await exportData(params)
        this.$message.success(res.msg)
        if (res.code === 0) {
          this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
        }
      } catch (error) {
        console.error('导出失败:', error)
      }
    },

    handleSizeChange(val) {
      this.page.size = val
      this._loadDataReportOweDetailStatisticsData()
    },

    handleCurrentChange(val) {
      this.page.current = val
      this._loadDataReportOweDetailStatisticsData()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-owe-fee-detail-container {
  padding: 20px;

  .search-form {
    .el-form-item {
      margin-right: 20px;
    }
  }

  .margin-top {
    margin-top: 20px;
  }

  .text-right {
    text-align: right;
  }
}
</style>