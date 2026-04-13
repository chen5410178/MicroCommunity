<template>
  <div class="report-owe-fee-container">
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
        <span>{{ $t('reportHuaning.tab.oweFee') }}</span>
      </div>
      <el-table :data="fees" border style="width: 100%">
        <el-table-column prop="floorNum" :label="$t('reportHuaningOweFee.table.floorNum')" align="center" />
        <el-table-column prop="oweAmount"
          :label="$t('reportHuaningOweFee.table.totalOweAmount', { date: dateStr })"
          align="center" />
        <el-table-column prop="curOweAmount" :label="_getCurYear() + $t('reportHuaningOweFee.table.oweAmount')"
          align="center" />
        <el-table-column prop="preOweAmount" :label="_getPreCurYear()" align="center" />
      </el-table>

      <el-pagination :current-page.sync="pagination.current" :page-size="pagination.size" :total="pagination.total"
        layout="total, prev, pager, next, jumper" @current-change="handlePageChange" class="margin-top" />
    </el-card>
  </div>
</template>

<script>
import { queryHuaningOweFee } from '@/api/report/reportHuaningApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'
import { listFloors } from '@/api/report/reportHuaningApi'
import { getDateYYYYMMDD } from '@/utils/dateUtil'

export default {
  name: 'ReportHuaningOweFee',
  data() {
    return {
      fees: [],
      dateStr: getDateYYYYMMDD(),
      feeTypeCds: [],
      floors: [],
      conditions: {
        configId: '',
        feeTypeCd: '',
        floorNum: '',
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1
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
          communityId: getCommunityId(),
          objType: '3333'
        }

        const { data, total } = await queryHuaningOweFee(params)

        this.fees = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取欠费数据失败:', error)
      }
    },
    handleQuery() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.conditions = {
        configId: '',
        feeTypeCd: '',
        floorNum: '',
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1
      }
      this.handleQuery()
    },
    handlePageChange(currentPage) {
      this.pagination.current = currentPage
      this.loadData()
    },
    _getCurYear() {
      const date = new Date()
      const year = date.getFullYear()
      const month = date.getMonth() + 1
      return `${year}${this.$t('common.year')}1-${month}${this.$t('common.month')}`
    },
    _getPreCurYear() {
      const date = new Date()
      const year = date.getFullYear()
      return `${year}${this.$t('reportHuaningOweFee.table.preYearOweAmount')}`
    },
    _showFeeDetail(fee, item) {
      if (this.$refs.viewFeeDetail) {
        this.$refs.viewFeeDetail.open({
          roomName: fee.objName,
          feeId: fee.feeId,
          configId: fee.configId,
          payerObjId: fee.objId,
          curYear: item.collectionYear
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.report-owe-fee-container {
  .margin-top {
    margin-top: 20px;
  }
}
</style>