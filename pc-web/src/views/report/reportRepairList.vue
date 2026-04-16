<template>
  <div class="animated fadeInRight report-repair-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('reportRepair.queryCondition') }}</span>
            <div style="float: right;">
              <el-button type="text" @click="_moreCondition()">
                {{ reportRepairInfo.moreCondition ? $t('reportRepair.hide') : $t('reportRepair.more') }}
              </el-button>
            </div>
          </div>
          <div class="card-body">
            <el-row :gutter="20">
              <el-col :span="4">
                <el-date-picker v-model="reportRepairInfo.conditions.beginStartTime" type="date"
                  :placeholder="$t('reportRepair.beginStartTimePlaceholder')" value-format="yyyy-MM-dd" style="width: 100%;" />
              </el-col>
              <el-col :span="4">
                <el-date-picker v-model="reportRepairInfo.conditions.beginEndTime" type="date"
                  :placeholder="$t('reportRepair.beginEndTimePlaceholder')" value-format="yyyy-MM-dd" style="width: 100%;"
                  @change="validateBeginTime" />
              </el-col>
              <el-col :span="4">
                <el-date-picker v-model="reportRepairInfo.conditions.finishStartTime" type="date"
                  :placeholder="$t('reportRepair.finishStartTimePlaceholder')" value-format="yyyy-MM-dd" style="width: 100%;" />
              </el-col>
              <el-col :span="4">
                <el-date-picker v-model="reportRepairInfo.conditions.finishEndTime" type="date"
                  :placeholder="$t('reportRepair.finishEndTimePlaceholder')" value-format="yyyy-MM-dd" style="width: 100%;"
                  @change="validateFinishTime" />
              </el-col>
              <el-col :span="4" v-if="reportRepairInfo.communitys.length > 1">
                <el-select v-model="reportRepairInfo.conditions.communityId"
                  :placeholder="$t('reportRepair.communityPlaceholder')" style="width: 100%;" @change="_changCommunity">
                  <el-option v-for="item in reportRepairInfo.communitys" :key="item.communityId" :label="item.name"
                    :value="item.communityId" />
                </el-select>
              </el-col>
              <el-col :span="4">
                <el-button type="primary" @click="_queryMethod">
                  <i class="el-icon-search"></i>
                  {{ $t('reportRepair.search') }}
                </el-button>
                <el-button @click="_resetMethod" style="margin-left: 10px;">
                  <i class="el-icon-refresh"></i>
                  {{ $t('reportRepair.reset') }}
                </el-button>
              </el-col>
            </el-row>

            <el-row :gutter="20" v-show="reportRepairInfo.moreCondition" class="margin-top">
              <el-col :span="4">
                <el-select v-model="reportRepairInfo.conditions.staffId"
                  :placeholder="$t('reportRepair.staffPlaceholder')" style="width: 100%;">
                  <el-option v-for="item in reportRepairInfo.repairUsers" :key="item.staffId" :label="item.staffName"
                    :value="item.staffId" />
                </el-select>
              </el-col>
              <el-col :span="4">
                <el-select v-model="reportRepairInfo.conditions.state" :placeholder="$t('reportRepair.statePlaceholder')"
                  style="width: 100%;">
                  <template v-for="item in reportRepairInfo.states">
                  <el-option :key="item.statusCd" :label="item.name"
                    :value="item.statusCd" v-if="item.statusCd != '10005'" />
                  </template>
                </el-select>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" >
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <div>
              <span>{{ $t('reportRepair.reportTitle') }}</span>
              <el-tooltip :content="$t('reportRepair.tooltip')" placement="top">
                <i class="el-icon-info" style="cursor: pointer; margin-left: 10px;"></i>
              </el-tooltip>
            </div>
            <div style="float: right;">
              <el-button type="primary" size="small" @click="_exportFee">
                <i class="el-icon-download"></i>
                {{ $t('reportRepair.export') }}
              </el-button>
            </div>
          </div>
          <div class="card-body">
            <div class="table-container" v-loading="loading">
              <table class="custom-table">
                <thead>
                  <tr>
                    <th style="width: 80px; text-align: center; padding: 12px 8px;">{{ $t('reportRepair.repairId') }}</th>
                    <th style="text-align: center; padding: 12px 8px;">{{ $t('reportRepair.staffId') }}</th>
                    <th style="text-align: center; padding: 12px 8px;">{{ $t('reportRepair.staffName') }}</th>
                    <th style="text-align: center; padding: 12px 8px;">{{ $t('reportRepair.dealing') }}</th>
                    <th style="text-align: center; padding: 12px 8px;">{{ $t('reportRepair.dispatch') }}</th>
                    <th style="text-align: center; padding: 12px 8px;">{{ $t('reportRepair.transfer') }}</th>
                    <th style="text-align: center; padding: 12px 8px;">{{ $t('reportRepair.chargeback') }}</th>
                    <th style="text-align: center; padding: 12px 8px;">{{ $t('reportRepair.returnVisit') }}</th>
                    <th style="text-align: center; padding: 12px 8px;">{{ $t('reportRepair.finished') }}</th>
                    <th style="text-align: center; padding: 12px 8px;">{{ $t('reportRepair.score') }}</th>
                  </tr>
                </thead>
                <tbody>
                  <!-- 数据行 -->
                  <tr v-for="(item, index) in reportRepairInfo.repairs" :key="index" class="data-row">
                    <td style="text-align: center; padding: 12px 8px;">{{ index + 1 }}</td>
                    <td style="text-align: center; padding: 12px 8px;">{{ item.staffId }}</td>
                    <td style="text-align: center; padding: 12px 8px;">{{ item.staffName }}</td>
                    <td style="text-align: center; padding: 12px 8px;">{{ item.dealAmount }}</td>
                    <td style="text-align: center; padding: 12px 8px;">{{ item.dispatchAmount }}</td>
                    <td style="text-align: center; padding: 12px 8px;">{{ item.transferOrderAmount }}</td>
                    <td style="text-align: center; padding: 12px 8px;">{{ item.chargebackAmount }}</td>
                    <td style="text-align: center; padding: 12px 8px;">{{ item.returnAmount }}</td>
                    <td style="text-align: center; padding: 12px 8px;">{{ item.statementAmount }}</td>
                    <td style="text-align: center; padding: 12px 8px;">{{ item.score || '-' }}</td>
                  </tr>
                  
                  <!-- 统计汇总行 -->
                  <tr class="statistics-row">
                    <td style="text-align: center; padding: 12px 8px; font-size: 18px; color: red;">{{ $t('reportRepair.statistics') }}</td>
                    <td style="text-align: center; padding: 12px 8px; font-size: 18px; color: red;">---</td>
                    <td style="text-align: center; padding: 12px 8px; font-size: 18px; color: red;">---</td>
                    <td style="text-align: center; padding: 12px 8px; font-size: 18px; color: red;">{{ reportRepairInfo.conditions.dealNumber }}</td>
                    <td style="text-align: center; padding: 12px 8px; font-size: 18px; color: red;">{{ reportRepairInfo.conditions.dispatchNumber }}</td>
                    <td style="text-align: center; padding: 12px 8px; font-size: 18px; color: red;">{{ reportRepairInfo.conditions.transferOrderNumber }}</td>
                    <td style="text-align: center; padding: 12px 8px; font-size: 18px; color: red;">{{ reportRepairInfo.conditions.chargebackNumber }}</td>
                    <td style="text-align: center; padding: 12px 8px; font-size: 18px; color: red;">{{ reportRepairInfo.conditions.returnNumber }}</td>
                    <td style="text-align: center; padding: 12px 8px; font-size: 18px; color: red;">{{ reportRepairInfo.conditions.statementNumber }}</td>
                    <td style="text-align: center; padding: 12px 8px; font-size: 18px; color: red;">---</td>
                  </tr>
                </tbody>
              </table>
            </div>

            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
              layout="total, sizes, prev, pager, next, jumper" :total="page.total" style="margin-top: 20px;" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryRepair, exportData, listMyEnteredCommunitys } from '@/api/report/reportRepairApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ReportRepairList',
  data() {
    return {
      loading: false,
      reportRepairInfo: {
        repairs: [],
        communitys: [],
        total: 0,
        records: 1,
        moreCondition: false,
        states: [],
        repairUsers: [],
        conditions: {
          staffId: '',
          staffName: '',
          state: '',
          stateName: '',
          amount: '',
          beginStartTime: '',
          beginEndTime: '',
          finishStartTime: '',
          finishEndTime: '',
          dealNumber: '',
          dispatchNumber: '',
          transferOrderNumber: '',
          chargebackNumber: '',
          statementNumber: '',
          returnNumber: '',
          score: '',
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
    this._initMethod()
  },
  methods: {
    async _initMethod() {
      this.reportRepairInfo.conditions.communityId = getCommunityId()
      await this._loadStaffCommunitys()
      await this._listRepairs(this.page.current, this.page.size)

      try {
        const states = await getDict('r_repair_user', 'state')
        this.reportRepairInfo.states = states
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    validateBeginTime() {
      if (this.reportRepairInfo.conditions.beginStartTime && this.reportRepairInfo.conditions.beginEndTime) {
        const start = new Date(this.reportRepairInfo.conditions.beginStartTime).getTime()
        const end = new Date(this.reportRepairInfo.conditions.beginEndTime).getTime()
        if (start > end) {
          this.$message.error(this.$t('reportRepair.timeError'))
          this.reportRepairInfo.conditions.beginEndTime = ''
        }
      }
    },
    validateFinishTime() {
      if (this.reportRepairInfo.conditions.finishStartTime && this.reportRepairInfo.conditions.finishEndTime) {
        const start = new Date(this.reportRepairInfo.conditions.finishStartTime).getTime()
        const end = new Date(this.reportRepairInfo.conditions.finishEndTime).getTime()
        if (start > end) {
          this.$message.error(this.$t('reportRepair.timeError'))
          this.reportRepairInfo.conditions.finishEndTime = ''
        }
      }
    },
    async _listRepairs(page, size) {
      this.loading = true
      try {
        const params = {
          page,
          row: size,
          ...this.reportRepairInfo.conditions
        }

        const res = await queryRepair(params)
        this.reportRepairInfo.repairs = res.data
        this.reportRepairInfo.repairUsers = res.sumTotal
        this.reportRepairInfo.conditions.dealNumber = res.rep.dealNumber
        this.reportRepairInfo.conditions.dispatchNumber = res.rep.dispatchNumber
        this.reportRepairInfo.conditions.transferOrderNumber = res.rep.transferOrderNumber
        this.reportRepairInfo.conditions.chargebackNumber = res.rep.chargebackNumber
        this.reportRepairInfo.conditions.statementNumber = res.rep.statementNumber
        this.reportRepairInfo.conditions.returnNumber = res.rep.returnNumber
        this.page.total = res.total
      } catch (error) {
        console.error('查询报修汇总失败:', error)
      } finally {
        this.loading = false
      }
    },
    _queryMethod() {
      this.page.current = 1
      this._listRepairs(this.page.current, this.page.size)
    },
    _resetMethod() {
      this.reportRepairInfo.conditions = {
        ...this.reportRepairInfo.conditions,
        staffId: '',
        staffName: '',
        beginStartTime: '',
        beginEndTime: '',
        finishStartTime: '',
        finishEndTime: '',
        state: '',
        stateName: ''
      }
      this._listRepairs(this.page.current, this.page.size)
    },
    _moreCondition() {
      this.reportRepairInfo.moreCondition = !this.reportRepairInfo.moreCondition
    },
    async _exportFee() {
      try {
        const params = {
          pagePath: 'reportRepairDetail',
          ...this.reportRepairInfo.conditions
        }

        const res = await exportData(params )
        this.$message.success(res.msg)
        if (res.code === 0) {
          this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
        }
      } catch (error) {
        console.error('导出失败:', error)
      }
    },
    async _loadStaffCommunitys() {
      try {
        const params = {
          _uid: '123mlkdinkldldijdhuudjdjkkd',
          page: 1,
          row: 100
        }

        const res = await listMyEnteredCommunitys(params)
        this.reportRepairInfo.communitys = res.communitys
      } catch (error) {
        console.error('获取小区列表失败:', error)
      }
    },
    _changCommunity() {
      this._listRepairs(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listRepairs(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listRepairs(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.report-repair-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    
  }

  .table-container {
   
    overflow-x: auto;
  }

  .custom-table {
    width: 100%;
    border-collapse: collapse;
    border: 1px solid #EBEEF5;
    
    th, td {
      border: 1px solid #EBEEF5;
      background-color: #fff;
    }
    
    th {
      background-color: #fafafa;
      font-weight: 500;
      color: #606266;
    }
    
    .data-row {
      &:hover {
        background-color: #f5f7fa;
      }
      
      &:nth-child(even) {
        background-color: #fafafa;
      }
    }
    
    .statistics-row {
      background-color: #fff !important;
      
      td {
        font-size: 18px;
        color: red;
        font-weight: bold;
      }
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>