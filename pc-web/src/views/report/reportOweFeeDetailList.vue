<template>
  <div class="report-owe-fee-detail-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="3">
        <select-community-floor ref="selectCommunityFloor" />
      </el-col>
      <el-col :span="21">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('reportOweFeeDetail.searchCondition') }}</span>
          </div>
          <el-form :inline="true" :model="reportOweFeeDetailInfo.conditions" class="search-form text-left">
            <el-form-item >
              <el-select v-model="reportOweFeeDetailInfo.conditions.feeTypeCd"
                :placeholder="$t('reportOweFeeDetail.selectFeeType')" style="width:100%">
                <el-option v-for="(item, index) in reportOweFeeDetailInfo.feeTypeCds" :key="index" :label="item.name"
                  :value="item.statusCd">
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item >
              <el-input v-model="reportOweFeeDetailInfo.conditions.objNameLike"
                :placeholder="$t('reportOweFeeDetail.inputRoomNum')">
              </el-input>
            </el-form-item>

            <el-form-item >
              <el-date-picker v-model="reportOweFeeDetailInfo.conditions.startTime" type="date"
                :placeholder="$t('reportOweFeeDetail.selectStartTime')">
              </el-date-picker>
            </el-form-item>

            <el-form-item >
              <el-date-picker v-model="reportOweFeeDetailInfo.conditions.endTime" type="date"
                :placeholder="$t('reportOweFeeDetail.selectEndTime')">
              </el-date-picker>
            </el-form-item>

            <el-form-item v-if="reportOweFeeDetailInfo.communitys.length > 1"
              >
              <el-select v-model="reportOweFeeDetailInfo.conditions.communityId" @change="_changCommunity"
                :placeholder="$t('reportOweFeeDetail.selectCommunity')" style="width:100%">
                <el-option v-for="(item, index) in reportOweFeeDetailInfo.communitys" :key="index" :label="item.name"
                  :value="item.communityId">
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="_queryMethod">{{ $t('reportOweFeeDetail.search') }}</el-button>
              <el-button @click="_resetMethod" style="margin-left: 10px;">{{ $t('reportOweFeeDetail.reset')
                }}</el-button>
            </el-form-item>
            <el-form-item >
              <el-input v-model="reportOweFeeDetailInfo.conditions.ownerNameLike"
                :placeholder="$t('reportOweFeeDetail.inputOwnerName')">
              </el-input>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card style="margin-top: 20px;">
          <div slot="header" class="flex justify-between">
            <div>
              <span>{{ $t('reportOweFeeDetail.oweFeeDetail') }}</span>
              <el-tooltip class="item" effect="dark" :content="$t('reportOweFeeDetail.tooltip')" placement="top">
                <i class="el-icon-info" style="margin-left: 10px; cursor: pointer;"></i>
              </el-tooltip>
              <span style="font-size: 12px; margin-left: 10px;">({{ $t('reportOweFeeDetail.updateDaily') }})</span>
            </div>
            <el-button type="primary" size="small" style="float: right;" @click="_exportExcel">
              <i class="el-icon-download"></i>
              {{ $t('reportOweFeeDetail.export') }}
            </el-button>
          </div>

          <el-table :data="reportOweFeeDetailInfo.fees" border style="width: 100%" v-loading="loading">
            <el-table-column prop="index" :label="$t('reportOweFeeDetail.feeNum')" width="80" align="center">
              <template slot-scope="scope">
                {{ scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column prop="objName" :label="$t('reportOweFeeDetail.roomNum')" align="center">
            </el-table-column>
            <el-table-column prop="ownerName" :label="$t('reportOweFeeDetail.owner')" align="center">
            </el-table-column>
            <el-table-column prop="ownerTel" :label="$t('reportOweFeeDetail.ownerTel')" align="center">
            </el-table-column>
            <el-table-column prop="builtUpArea" :label="$t('reportOweFeeDetail.area')" align="center">
            </el-table-column>
            <el-table-column prop="feeName" :label="$t('reportOweFeeDetail.feeItem')" align="center">
            </el-table-column>
            <el-table-column prop="startTime" :label="$t('reportOweFeeDetail.startTime')" align="center">
            </el-table-column>
            <el-table-column prop="endTime" :label="$t('reportOweFeeDetail.endTime')" align="center">
            </el-table-column>
            <el-table-column prop="oweDay" :label="$t('reportOweFeeDetail.oweDays')" align="center">
            </el-table-column>
            <el-table-column prop="oweAmount" :label="$t('reportOweFeeDetail.oweAmount')" align="center">
            </el-table-column>
          </el-table>

          <div class="summary">
            <div class="summary-item">
              <span class="label">{{ $t('reportOweFeeDetail.subtotal') }}</span>
              <span class="value">
                {{ $t('reportOweFeeDetail.owe') }}: {{ reportOweFeeDetailInfo.totalPreferentialAmount }} {{
                  $t('reportOweFeeDetail.yuan') }}
              </span>
            </div>
            <div class="summary-item">
              <span class="label">{{ $t('reportOweFeeDetail.total') }}</span>
              <span class="value">
                {{ $t('reportOweFeeDetail.owe') }}: {{ reportOweFeeDetailInfo.allOweAmount }} {{
                  $t('reportOweFeeDetail.yuan') }}
              </span>
            </div>
          </div>

          <div class="footer">
            <div class="text-left table-desc">
              <p>{{ $t('reportOweFeeDetail.feeStartTimeDesc') }}</p>
              <p>{{ $t('reportOweFeeDetail.oweDaysDesc1') }}</p>
              <p>{{ $t('reportOweFeeDetail.oweDaysDesc2') }}</p>
              <p>{{ $t('reportOweFeeDetail.oweAmountDesc') }}</p>
            </div>
            <div class="footer-right">
              <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
                layout="total, sizes, prev, pager, next, jumper" :total="page.total">
              </el-pagination>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import SelectCommunityFloor from '@/components/report/selectCommunityFloor'

import { queryOweFeeDetail, exportData } from '@/api/report/reportOweFeeDetailApi'
import { searchFloors } from '@/api/room/searchFloorApi'
import {
  listMyEnteredCommunitys
} from '@/api/report/dataReportApi'

export default {
  name: 'ReportOweFeeDetailList',
  components: {
    SelectCommunityFloor
  },
  data() {
    return {
      loading: false,
      reportOweFeeDetailInfo: {
        fees: [],
        floors: [],
        total: 0,
        records: 1,
        feeTypeCds: [],
        communitys: [],
        totalPreferentialAmount: 0.0,
        allOweAmount: 0.0,
        conditions: {
          floorId: '',
          floorName: '',
          roomNum: '',
          objNameLike: '',
          feeTypeCd: '',
          startTime: '',
          endTime: '',
          communityId: '',
          ownerNameLike: '',
          page: 1,
          row: 10
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
    setTimeout(() => {
      this.$refs.selectCommunityFloor.open({
        callBack: (floor) => {
          this.reportOweFeeDetailInfo.conditions.floorId = floor.floorId
            this._listFees()
        }
      })
    }, 500)
  },
  methods: {
    async initData() {
      this.reportOweFeeDetailInfo.conditions.communityId = getCommunityId()
      await this.getDictData()
      await this._listFloors()
      await this._listFees()
      await this._loadStaffCommunitys()
    },

    async getDictData() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.reportOweFeeDetailInfo.feeTypeCds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    async _listFees() {
      this.loading = true
      try {
        const params = {
          ...this.reportOweFeeDetailInfo.conditions,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await queryOweFeeDetail(params)
        this.reportOweFeeDetailInfo.fees = data
        this.page.total = total

        // 计算小计
        let _totalPreferentialAmount = 0.0
        data.forEach(item => {
          _totalPreferentialAmount += parseFloat(item.oweAmount)
        })
        this.reportOweFeeDetailInfo.totalPreferentialAmount = _totalPreferentialAmount.toFixed(2)

        if (data.length > 0) {
          this.reportOweFeeDetailInfo.allOweAmount = data[0].allOweAmount
        } else {
          this.reportOweFeeDetailInfo.allOweAmount = 0.0.toFixed(2)
        }
      } catch (error) {
        console.error('获取欠费明细失败:', error)
      } finally {
        this.loading = false
      }
    },

    async _listFloors() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.reportOweFeeDetailInfo.conditions.communityId
        }
        const { apiFloorDataVoList } = await searchFloors(params)
        this.reportOweFeeDetailInfo.floors = apiFloorDataVoList
      } catch (error) {
        console.error('获取楼层列表失败:', error)
      }
    },

    async _loadStaffCommunitys() {
      try {
        const params = {
          _uid: '123mlkdinkldldijdhuudjdjkkd',
          page: 1,
          row: 100
        }
        const data = await listMyEnteredCommunitys(params)
        this.reportOweFeeDetailInfo.communitys = data.communitys
      } catch (error) {
        console.error('获取小区列表失败:', error)
      }
    },

    _queryMethod() {
      this.page.current = 1
      this._listFees()
    },

    _resetMethod() {
      this.reportOweFeeDetailInfo.conditions = {
        floorId: '',
        floorName: '',
        roomNum: '',
        objNameLike: '',
        feeTypeCd: '',
        startTime: '',
        endTime: '',
        communityId: this.reportOweFeeDetailInfo.conditions.communityId,
        ownerNameLike: '',
        page: 1,
        row: 10
      }
      this._listFees()
    },

    swatchFloor(floorId) {
      this.reportOweFeeDetailInfo.conditions.floorId = floorId
      this._listFees()
    },

    _changCommunity() {
      this.reportOweFeeDetailInfo.conditions.floorId = ''
      this._listFloors()
      this._listFees()
    },

    async _exportExcel() {
      try {
        const params = {
          ...this.reportOweFeeDetailInfo.conditions,
          pagePath: 'reportOweFeeDetail'
        }
        const { code, msg } = await exportData(params)
        this.$message.success(msg)
        if (code === 0) {
          this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
        }
      } catch (error) {
        console.error('导出失败:', error)
      }
    },

    handleSizeChange(val) {
      this.page.size = val
      this._listFees()
    },

    handleCurrentChange(val) {
      this.page.current = val
      this._listFees()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-owe-fee-detail-container {
  padding: 20px;

  .floor-list-card {
    height: 100%;

    .floor-list {
      list-style: none;
      padding: 0;
      margin: 0;

      .floor-item {
        padding: 10px;
        margin-bottom: 5px;
        cursor: pointer;
        border-radius: 4px;
        text-align: center;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active {
          background-color: #409eff;
          color: white;
        }
      }
    }
  }

  .search-form {

    .el-form-item {
      margin-right: 20px;
    }
  }

  .summary {
    padding: 10px;
    border-radius: 4px;

    .summary-item {
      display: flex;
      align-items: center;
      margin-bottom: 5px;

      .label {
        font-weight: bold;
        margin-right: 10px;
        width: 60px;
      }
    }
  }

  .footer {
    display: flex;
    justify-content: space-between;

    .footer-left {
      width: 60%;
      font-size: 12px;
      color: #666;

      p {
        margin: 5px 0;
      }
    }

    .footer-right {
      width: 40%;
      text-align: right;
    }
  }
}
</style>