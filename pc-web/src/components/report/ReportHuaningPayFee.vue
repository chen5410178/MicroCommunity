<template>
  <div class="report-pay-fee-container">
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
        <span>{{ $t('reportHuaning.tab.payFee') }}</span>
      </div>
      <el-table :data="fees" border style="width: 100%">
        <el-table-column prop="floorNum" :label="$t('reportHuaningPayFee.table.floorNum')" align="center" />
        <el-table-column prop="receivableAmount" :label="$t('reportHuaningPayFee.table.monthlyReceivable')"
          align="center" />
        <el-table-column prop="receivedAmount"
          :label="`${conditions.month}${$t('reportHuaningPayFee.table.monthReceived')}`"
          align="center" />
        <el-table-column prop="receivedAmount1" :label="`${conditions.month}${$t('reportHuaningPayFee.table.monthReceivedPart1', {
          year: conditions.year,
          month: conditions.month - 1
        })}`" align="center" />
        <el-table-column prop="receivedAmount2" :label="`${conditions.month}${$t('reportHuaningPayFee.table.monthReceivedPart2', {
          year: conditions.year,
          month: conditions.month
        })}`" align="center" />
        <el-table-column prop="receivedAmount3" :label="`${conditions.month}${$t('reportHuaningPayFee.table.monthReceivedPart3', {
          nextMonth: _getNextMonth()
        })}`" align="center" />
        <el-table-column prop="receivedAmount4" :label="`${conditions.month}${$t('reportHuaningPayFee.table.monthReceivedPart4', {
          year: conditions.year
        })}`" align="center" />
      </el-table>

      <el-pagination :current-page.sync="pagination.current" :page-size="pagination.size" :total="pagination.total"
        layout="total, prev, pager, next, jumper" @current-change="handlePageChange" class="margin-top" />
    </el-card>
  </div>
</template>

<script>
import { queryHuaningPayFee } from '@/api/report/reportHuaningApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'
import { listFloors } from '@/api/report/reportHuaningApi'
import { getDateYYYYMMDD } from '@/utils/dateUtil'

export default {
  name: 'ReportHuaningPayFee',
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

        const { data, total } = await queryHuaningPayFee(params)

        this.fees = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取缴费数据失败:', error)
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
    },
    _getNextMonth() {
      const date = new Date()
      date.setMonth(date.getMonth() + 1)
      return `${date.getFullYear()}${this.$t('common.year')}${date.getMonth() + 1}${this.$t('common.month')}`
    }
  }
}
</script>

<style lang="scss" scoped>
.report-pay-fee-container {
  .margin-top {
    margin-top: 20px;
  }
}
</style>