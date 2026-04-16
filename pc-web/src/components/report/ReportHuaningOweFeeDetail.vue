<template>
  <div class="report-owe-fee-detail-container">
    <el-card>
      <div slot="header" class="flex justify-between">
        <div>{{ $t('reportHuaning.search.title') }}</div>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-select v-model="conditions.feeTypeCd" style="width:100%" :placeholder="$t('reportHuaning.search.feeType')">
            <el-option v-for="(item, index) in feeTypeCds" :key="index" :label="item.name" :value="item.statusCd">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="conditions.floorNum" style="width:100%" :placeholder="$t('reportHuaning.search.floor')">
            <el-option v-for="(item, index) in floors" :key="index" :label="item.floorNum" :value="item.floorNum">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleQuery">
            <i class="el-icon-search"></i>
            <span>{{ $t('common.search') }}</span>
          </el-button>
          <el-button @click="handleReset" style="margin-left: 20px;">
            <i class="el-icon-refresh"></i>
            <span>{{ $t('common.reset') }}</span>
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="margin-top">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('reportHuaning.tab.oweFeeDetail') }}</span>
      </div>
      <el-table :data="fees" border style="width: 100%">
        <el-table-column prop="roomName" :label="$t('reportHuaningOweFeeDetail.table.roomNum')" align="center" />
        <el-table-column prop="builtUpArea" :label="$t('reportHuaningOweFeeDetail.table.area')" align="center" />
        <el-table-column prop="feeName" :label="$t('reportHuaningOweFeeDetail.table.fee')" align="center" />
        <el-table-column prop="endTime" :label="$t('reportHuaningOweFeeDetail.table.startTime')" align="center" />
        <el-table-column prop="deadlineTime" :label="$t('reportHuaningOweFeeDetail.table.endTime')" align="center" />
        <el-table-column prop="oweAmount" :label="$t('reportHuaningOweFeeDetail.table.totalOweAmount')" align="center" />
        <el-table-column prop="curOweAmount"
          :label="`${conditions.year}${$t('common.year')}1-${conditions.month}${$t('common.month')}${$t('reportHuaningOweFeeDetail.table.oweAmount')}`"
          align="center" />
        <el-table-column prop="preOweAmount"
          :label="`${conditions.year}${$t('common.year')}${$t('reportHuaningOweFeeDetail.table.preYearOweAmount')}`"
          align="center" />
      </el-table>

      <el-pagination :current-page.sync="pagination.current" :page-size="pagination.size" :total="pagination.total"
        layout="total, prev, pager, next, jumper" @current-change="handlePageChange" class="margin-top" />
    </el-card>
  </div>
</template>

<script>
import { queryHuaningOweFeeDetail } from '@/api/report/reportHuaningApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'
import { listFloors } from '@/api/report/reportHuaningApi'
import { getDateYYYYMMDD } from '@/utils/dateUtil'

export default {
  name: 'ReportHuaningOweFeeDetail',
  data() {
    return {
      fees: [],
      dateStr: getDateYYYYMMDD(),
      feeTypeCds: [],
      floors: [],
      conditions: {
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1,
        feeTypeCd: '',
        floorNum: ''
      },
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
    async initData() {
      await this.loadDictData()
      await this.loadFloorsData()
      await this.loadData()
    },
    async loadDictData() {
      try {
        const feeTypes = await getDict('pay_fee_config', 'fee_type_cd')
        this.feeTypeCds = feeTypes
      } catch (error) {
        console.error('获取费用类型字典失败:', error)
      }
    },
    async loadFloorsData() {
      try {
        const params = {
          communityId: getCommunityId(),
          row: 50,
          page: 1
        }
        const floorData = await listFloors(params)
        this.floors = floorData.apiFloorDataVoList || []
      } catch (error) {
        console.error('获取楼栋数据失败:', error)
      }
    },
    async loadData() {
      try {
        const params = {
          ...this.conditions,
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: getCommunityId()
        }

        const { data, total } = await queryHuaningOweFeeDetail(params)

        this.fees = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取欠费明细数据失败:', error)
      }
    },
    handleQuery() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.conditions = {
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1,
        feeTypeCd: '',
        floorNum: ''
      }
      this.handleQuery()
    },
    handlePageChange(currentPage) {
      this.pagination.current = currentPage
      this.loadData()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-owe-fee-detail-container {
  .margin-top {
    margin-top: 20px;
  }
}
</style>