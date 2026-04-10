<template>
  <div class="list-owe-fee-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('listOweFee.search.title') }}</span>
        <el-button type="text" style="float: right; padding: 3px 0" @click="toggleMoreCondition">
          {{ listOweFeeInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="listOweFeeInfo.conditions.floorName" :placeholder="$t('listOweFee.search.floorName')"
            class="search-item">
            <el-button slot="append" type="primary" @click="openChooseFloor">
              <i class="el-icon-search"></i>
              {{ $t('common.select') }}
            </el-button>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-select v-model="listOweFeeInfo.conditions.unitId" :placeholder="$t('listOweFee.search.unit')"
            style="width:100%">
            <el-option :label="$t('listOweFee.search.unit')" value=""></el-option>
            <el-option v-for="(unit, index) in listOweFeeInfo.roomUnits" :key="index"
              :label="unit.unitNum + $t('listOweFee.search.unit')" :value="unit.unitId"></el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model.trim="listOweFeeInfo.conditions.roomNum"
            :placeholder="$t('listOweFee.search.roomNum')"></el-input>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="queryOweFee">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button type="info" @click="resetOweFee">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <!-- 更多查询条件 -->
      <el-row v-show="listOweFeeInfo.moreCondition" :gutter="20">
        <el-col :span="6">
          <el-select v-model="listOweFeeInfo.conditions.roomSubType" :placeholder="$t('listOweFee.search.roomType')"
            style="width:100%">
            <el-option :label="$t('listOweFee.search.roomType')" value=""></el-option>
            <el-option v-for="(item, index) in listOweFeeInfo.roomSubTypes" :key="index" :label="item.name"
              :value="item.statusCd"></el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model.trim="listOweFeeInfo.conditions.ownerName"
            :placeholder="$t('listOweFee.search.ownerName')"></el-input>
        </el-col>
        <el-col :span="6">
          <el-select v-model="listOweFeeInfo.conditions.payObjType" :placeholder="$t('listOweFee.search.payObjType')"
            style="width:100%">
            <el-option :label="$t('listOweFee.search.payObjType')" value=""></el-option>
            <el-option v-for="(item, index) in listOweFeeInfo.payObjTypes" :key="index" :label="item.name"
              :value="item.statusCd"></el-option>
          </el-select>
        </el-col>
      </el-row>

      <el-row v-show="listOweFeeInfo.moreCondition" :gutter="20" class="margin-top">
        <el-col v-if="listOweFeeInfo.conditions.payObjType === '3333'" :span="6">
          <el-input v-model.trim="listOweFeeInfo.conditions.num"
            :placeholder="$t('listOweFee.search.roomNumFormat')"></el-input>
        </el-col>
        <el-col v-if="listOweFeeInfo.conditions.payObjType === '6666'" :span="6">
          <el-input v-model.trim="listOweFeeInfo.conditions.num"
            :placeholder="$t('listOweFee.search.carportNum')"></el-input>
        </el-col>
        <el-col :span="6">
          <el-select v-model="selectedFeeConfigs" multiple :placeholder="$t('listOweFee.search.feeConfig')"
            style="width:100%">
            <el-option v-for="(item, index) in listOweFeeInfo.feeConfigs" :key="index" :label="item.feeName"
              :value="item.configId"></el-option>
          </el-select>
        </el-col>
      </el-row>
    </el-card>

    <!-- 欠费信息表格 -->
    <el-card class="table-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('listOweFee.table.title') }}</span>
        <el-button type="primary" size="small" @click="exportFee">
          <i class="el-icon-download"></i>
          {{ $t('common.export') }}
        </el-button>
      </div>
      <el-table :data="listOweFeeInfo.fees" border style="width: 100%" v-loading="loading">
        <el-table-column prop="index" :label="$t('listOweFee.table.index')" width="80" align="center">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="payerObjName" :label="$t('listOweFee.table.payerObjName')"
          align="center"></el-table-column>
        <el-table-column prop="ownerName" :label="$t('listOweFee.table.ownerName')" align="center"></el-table-column>
        <el-table-column prop="ownerTel" :label="$t('listOweFee.table.ownerTel')" align="center"></el-table-column>
        <el-table-column prop="startTime" :label="$t('listOweFee.table.endTime')" align="center"></el-table-column>
        <el-table-column prop="endTime" :label="$t('listOweFee.table.deadlineTime')"
          align="center"></el-table-column>
        <el-table-column v-for="(item, index) in listOweFeeInfo.feeConfigNames" :key="index" :label="item.configName"
          align="center">
          <template slot-scope="scope">
            {{ getFeeOweAmount(item, scope.row) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('listOweFee.table.total')" align="center">
          <template slot-scope="scope">
            {{ getAllFeeOweAmount(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" :label="$t('listOweFee.table.updateTime')" align="center"></el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
        :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
        :total="page.total" style="margin-top: 20px;"></el-pagination>

      <!-- 小计和合计 -->
      <div class="summary-wrapper">
        <div class="summary-item">
          <span class="summary-title">{{ $t('listOweFee.summary.subtotal') }}</span>
          <div v-for="(item, index) in listOweFeeInfo.feeConfigNames" :key="index" class="summary-content">
            {{ item.configName }}: {{ getFeeOweAllAmount(item) }} {{ $t('common.yuan') }}
          </div>
          <div class="summary-content">
            {{ $t('listOweFee.summary.total') }}: {{ getFeeOweAllAmounts() }} {{ $t('common.yuan') }}
          </div>
        </div>
      </div>
    </el-card>

    <!-- 选择楼栋组件 -->
    <search-floor ref="searchFloor" @chooseFloor="handleChooseFloor"></search-floor>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { queryReportOweFee, listFeeConfigs, exportData, queryUnits } from '@/api/fee/listOweFeeApi'
import SearchFloor from '@/components/fee/searchFloor'

export default {
  name: 'ListOweFee',
  components: {
    SearchFloor
  },
  data() {
    return {
      loading: false,
      listOweFeeInfo: {
        fees: [],
        moreCondition: false,
        roomUnits: [],
        conditions: {
          configIds: '',
          payObjType: '',
          num: '',
          billType: '00123',
          floorId: '',
          floorName: '',
          roomNum: '',
          unitId: '',
          roomSubType: '',
          ownerName: '',
          communityId: ''
        },
        feeConfigs: [],
        feeConfigNames: [],
        payObjTypes: [],
        roomSubTypes: []
      },
      selectedFeeConfigs: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.listOweFeeInfo.conditions.communityId = getCommunityId()
    this.initData()
  },
  watch: {
    selectedFeeConfigs(newVal) {
      this.listOweFeeInfo.feeConfigNames = newVal.map(configId => {
        const config = this.listOweFeeInfo.feeConfigs.find(item => item.configId === configId)
        return {
          configId,
          configName: config ? config.feeName : ''
        }
      })
    }
  },
  methods: {
    async initData() {
      try {
        this.loading = true
        await Promise.all([
          this.getPayObjTypes(),
          this.getRoomSubTypes(),
          this.loadListOweFeeInfo(),
          this.listFeeConfigs()
        ])
      } catch (error) {
        console.error('初始化数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    async getPayObjTypes() {
      try {
        const data = await getDict('report_owe_fee', 'payer_obj_type')
        this.listOweFeeInfo.payObjTypes = data
      } catch (error) {
        console.error('获取付费对象类型失败:', error)
      }
    },
    async getRoomSubTypes() {
      try {
        const data = await getDict('building_room', 'room_sub_type')
        this.listOweFeeInfo.roomSubTypes = data
      } catch (error) {
        console.error('获取房屋类型失败:', error)
      }
    },
    async loadListOweFeeInfo() {
      try {
        const params = {
          ...this.listOweFeeInfo.conditions,
          page: this.page.current,
          row: this.page.size,
          configIds: this.listOweFeeInfo.feeConfigNames.map(item => item.configId).join(',')
        }
        const { data, total } = await queryReportOweFee(params)
        this.listOweFeeInfo.fees = data
        this.page.total = total
      } catch (error) {
        console.error('获取欠费信息失败:', error)
      }
    },
    async listFeeConfigs() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.listOweFeeInfo.conditions.communityId
        }
        const { data } = await listFeeConfigs(params)
        this.listOweFeeInfo.feeConfigs = data
      } catch (error) {
        console.error('获取收费项失败:', error)
      }
    },
    toggleMoreCondition() {
      this.listOweFeeInfo.moreCondition = !this.listOweFeeInfo.moreCondition
    },
    queryOweFee() {
      this.page.current = 1
      this.loadListOweFeeInfo()
    },
    resetOweFee() {
      this.listOweFeeInfo.conditions = {
        configIds: '',
        payObjType: '',
        num: '',
        billType: '00123',
        floorId: '',
        floorName: '',
        roomNum: '',
        unitId: '',
        roomSubType: '',
        ownerName: '',
        communityId: this.listOweFeeInfo.conditions.communityId
      }
      this.selectedFeeConfigs = []
      this.loadListOweFeeInfo()
    },
    getFeeOweAmount(item, fee) {
      if (!fee.items) return 0
      const found = fee.items.find(tmp => tmp.configId === item.configId)
      return found ? found.amountOwed : 0
    },
    getFeeOweAllAmount(item) {
      let amountOwed = 0.0
      this.listOweFeeInfo.fees.forEach(feeItem => {
        if (!feeItem.items) return
        feeItem.items.forEach(tmp => {
          if (tmp.configId === item.configId) {
            amountOwed += parseFloat(tmp.amountOwed)
          }
        })
      })
      return amountOwed.toFixed(2)
    },
    getAllFeeOweAmount(fee) {
      if (this.listOweFeeInfo.feeConfigNames.length < 1) {
        return fee.amountOwed
      }
      let amountOwed = 0.0
      this.listOweFeeInfo.feeConfigNames.forEach(feeItem => {
        if (!fee.items) return
        fee.items.forEach(item => {
          if (feeItem.configId === item.configId) {
            amountOwed += parseFloat(item.amountOwed)
          }
        })
      })
      return amountOwed.toFixed(2)
    },
    getFeeOweAllAmounts() {
      let amountOwed = 0.0
      this.listOweFeeInfo.fees.forEach(feeItem => {
        const rowAmount = this.getAllFeeOweAmount(feeItem)
        amountOwed += parseFloat(rowAmount)
      })
      return amountOwed.toFixed(2)
    },
    async exportFee() {
      try {
        const params = {
          ...this.listOweFeeInfo.conditions,
          pagePath: 'reportListOweFee',
          configIds: this.listOweFeeInfo.feeConfigNames.map(item => item.configId).join(',')
        }
        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error(this.$t('listOweFee.exportError'))
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadListOweFeeInfo()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadListOweFeeInfo()
    },
    openChooseFloor() {
      this.$refs.searchFloor.open()
    },
    handleChooseFloor(floor) {
      this.listOweFeeInfo.conditions.floorId = floor.floorId
      this.listOweFeeInfo.conditions.floorName = floor.floorName
      this.loadUnits(floor.floorId)
    },
    async loadUnits(floorId) {
      try {
        const params = {
          floorId,
          communityId: this.listOweFeeInfo.conditions.communityId
        }
        const { data } = await queryUnits(params)
        this.listOweFeeInfo.roomUnits = data
      } catch (error) {
        console.error('获取单元信息失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.list-owe-fee-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;

    .search-item {
      margin-bottom: 15px;
    }
  }

  .table-card {
    margin-bottom: 20px;
  }

  .summary-wrapper {
    margin-top: 20px;
    padding: 15px;
    background-color: #f5f7fa;
    border-radius: 4px;

    .summary-item {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
    }

    .summary-title {
      font-weight: bold;
      margin-right: 20px;
    }

    .summary-content {
      margin-right: 30px;
      margin-bottom: 10px;
    }
  }
}
</style>