<template>
  <div class="report-pay-fee-deposit-container animated fadeInRight">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('reportPayFeeDeposit.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-row :gutter="20">
            <el-col :span="4">
              <el-select v-model="conditions.floorId" :placeholder="$t('reportPayFeeDeposit.search.floorName')"
                @change="handleFloorChange" class="search-item" style="width:100%">
                <el-option :label="$t('reportPayFeeDeposit.search.floorName')" value=""></el-option>
                <el-option v-for="item in floors" :key="item.floorId" :label="item.floorName" :value="item.floorId">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.unitId" :placeholder="$t('reportPayFeeDeposit.search.unit')"
                class="search-item" style="width:100%">
                <el-option :label="$t('reportPayFeeDeposit.search.unitPlaceholder')" value=""></el-option>
                <el-option v-for="(unit, index) in roomUnits" :key="index" :label="unit.unitNum" :value="unit.unitId">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model="conditions.roomNum" :placeholder="$t('reportPayFeeDeposit.search.roomNum')"
                class="search-item">
              </el-input>
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.configId" :placeholder="$t('reportPayFeeDeposit.search.feeName')"
                class="search-item" style="width:100%">
                <el-option :label="$t('reportPayFeeDeposit.search.feeNamePlaceholder')" value=""></el-option>
                <el-option v-for="(item, index) in feeConfigs" :key="index" :label="item.feeName"
                  :value="item.configId">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.state" :placeholder="$t('reportPayFeeDeposit.search.state')"
                class="search-item" style="width:100%">
                <el-option :label="$t('reportPayFeeDeposit.search.statePlaceholder')" value=""></el-option>
                <el-option v-for="(item, index) in states" :key="index" :label="item.name" :value="item.statusCd">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="queryMethod">
                <i class="el-icon-search"></i>
                {{ $t('reportPayFeeDeposit.search.search') }}
              </el-button>
              <el-button @click="resetMethod" style="margin-left: 10px;">
                <i class="el-icon-refresh"></i>
                {{ $t('reportPayFeeDeposit.search.reset') }}
              </el-button>
            </el-col>
          </el-row>
          <el-row :gutter="20">

            <el-col :span="4">
              <el-select v-model="conditions.payerObjType" :placeholder="$t('reportPayFeeDeposit.search.payerObjType')"
                class="search-item" style="width:100%">
                <el-option :label="$t('reportPayFeeDeposit.search.payerObjTypePlaceholder')" value=""></el-option>
                <el-option v-for="(item, index) in payerObjTypes" :key="index" :label="item.name"
                  :value="item.statusCd">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="conditions.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('reportPayFeeDeposit.search.startTime')" style="width:100%">
              </el-date-picker>
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="conditions.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('reportPayFeeDeposit.search.endTime')" style="width:100%">
              </el-date-picker>
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.detailState" :placeholder="$t('reportPayFeeDeposit.search.detailState')"
                class="search-item" style="width:100%">
                <el-option :label="$t('reportPayFeeDeposit.search.detailStatePlaceholder')" value=""></el-option>
                <template v-for="(item, index) in detailStates">
                  <el-option :key="index" :label="item.name" :value="item.statusCd" v-if="item.statusCd != '1300'">
                  </el-option>
                </template>
              </el-select>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-card>

    <!-- 押金报表 -->
    <el-card class="table-card">
      <div slot="header" class="flex justify-between">
        <div>
          <span>{{ $t('reportPayFeeDeposit.table.title') }}</span>
          <el-tooltip class="item" effect="dark" :content="$t('reportPayFeeDeposit.table.tooltip')" placement="top">
            <i class="el-icon-info" style="margin-left: 10px; cursor: pointer;"></i>
          </el-tooltip>
        </div>
        <el-button type="primary" size="small" style="float: right;" @click="exportFee">
          <i class="el-icon-download"></i>
          {{ $t('reportPayFeeDeposit.table.export') }}
        </el-button>
      </div>
      <el-table :data="depositFees" border style="width: 100%" v-loading="loading">
        <el-table-column prop="feeId" :label="$t('reportPayFeeDeposit.table.feeId')" align="center">
        </el-table-column>
        <el-table-column prop="objName" :label="$t('reportPayFeeDeposit.table.roomNum')" align="center">
        </el-table-column>
        <el-table-column prop="ownerName" :label="$t('reportPayFeeDeposit.table.owner')" align="center">
        </el-table-column>
        <el-table-column prop="feeTypeCdName" :label="$t('reportPayFeeDeposit.table.feeType')" align="center">
        </el-table-column>
        <el-table-column prop="feeName" :label="$t('reportPayFeeDeposit.table.feeName')" align="center">
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('reportPayFeeDeposit.table.startTime')" align="center">
        </el-table-column>
        <el-table-column prop="deadlineTime" :label="$t('reportPayFeeDeposit.table.endTime')" align="center">
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('reportPayFeeDeposit.table.createTime')" align="center">
        </el-table-column>
        <el-table-column prop="payerObjTypeName" :label="$t('reportPayFeeDeposit.table.payerObjType')" align="center">
        </el-table-column>
        <el-table-column prop="payerObjId" :label="$t('reportPayFeeDeposit.table.payerObjId')" align="center">
        </el-table-column>
        <el-table-column prop="additionalAmount" :label="$t('reportPayFeeDeposit.table.amount')" align="center">
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('reportPayFeeDeposit.table.state')" align="center">
        </el-table-column>
        <el-table-column :label="$t('reportPayFeeDeposit.table.detailState')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.state == '2009001'">{{ scope.row.detailStateName }}</span>
            <span v-else>{{ $t('reportPayFeeDeposit.table.unpaid') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('reportPayFeeDeposit.table.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button size="mini" @click="openPayFeeDetail(scope.row)">
              {{ $t('reportPayFeeDeposit.table.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
        :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
        :total="total" style="margin-top: 20px;">
      </el-pagination>
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="1">
          <div>
            <b>{{ $t('reportPayFeeDeposit.table.subtotal') }}</b>
          </div>
          <div style="margin-top: 10px;">
            <b>{{ $t('reportPayFeeDeposit.table.total') }}</b>
          </div>
        </el-col>
        <el-col :span="3">
          <div>
            {{ $t('reportPayFeeDeposit.table.unpaid') }}: {{ sumTotal.unpaidfeeAmount }} {{
              $t('reportPayFeeDeposit.table.yuan') }}
          </div>
          <div style="margin-top: 10px;">
            {{ $t('reportPayFeeDeposit.table.unpaid') }}: {{ sumTotal.unpaidfeeAmounts }} {{
              $t('reportPayFeeDeposit.table.yuan') }}
          </div>
        </el-col>
        <el-col :span="3">
          <div>
            {{ $t('reportPayFeeDeposit.table.paid') }}: {{ sumTotal.paidfeeAmount }} {{
              $t('reportPayFeeDeposit.table.yuan')
            }}
          </div>
          <div style="margin-top: 10px;">
            {{ $t('reportPayFeeDeposit.table.paid') }}: {{ sumTotal.paidfeeAmounts }} {{
              $t('reportPayFeeDeposit.table.yuan') }}
          </div>
        </el-col>
        <el-col :span="3">
          <div>
            {{ $t('reportPayFeeDeposit.table.refunded') }}: {{ sumTotal.refundedAmount }} {{
              $t('reportPayFeeDeposit.table.yuan') }}
          </div>
          <div style="margin-top: 10px;">
            {{ $t('reportPayFeeDeposit.table.refunded') }}: {{ sumTotal.refundedAmounts }} {{
              $t('reportPayFeeDeposit.table.yuan') }}
          </div>
        </el-col>
        <el-col :span="3">
          <div>
            {{ $t('reportPayFeeDeposit.table.refundInProgress') }}: {{ sumTotal.refundInProgressAmount }} {{
              $t('reportPayFeeDeposit.table.yuan') }}
          </div>
          <div style="margin-top: 10px;">
            {{ $t('reportPayFeeDeposit.table.refundInProgress') }}: {{ sumTotal.refundInProgressAmounts }} {{
              $t('reportPayFeeDeposit.table.yuan') }}
          </div>
        </el-col>
        <el-col :span="3">
          <div>
            {{ $t('reportPayFeeDeposit.table.refundFailed') }}: {{ sumTotal.refundFailedAmount }} {{
              $t('reportPayFeeDeposit.table.yuan') }}
          </div>
          <div style="margin-top: 10px;">
            {{ $t('reportPayFeeDeposit.table.refundFailed') }}: {{ sumTotal.refundFailedAmounts }} {{
              $t('reportPayFeeDeposit.table.yuan') }}
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'
import { queryPayFeeDeposit, queryUnits, exportData } from '@/api/report/reportPayFeeDepositApi'
import {getFloors} from '@/api/room/roomApi'

export default {
  name: 'ReportPayFeeDepositList',
  data() {
    return {
      loading: false,
      communityId: '',
      conditions: {
        floorId: '',
        roomNum: '',
        unitId: '',
        feeId: '',
        state: '',
        payerObjType: '',
        startTime: '',
        endTime: '',
        configId: '',
        detailState: '',
        feeTypeCd: '888800010006', // 押金
        communityId: '',
        page: 1,
        row: 10
      },
      depositFees: [],
      feeConfigs: [],
      states: [],
      payerObjTypes: [],
      detailStates: [],
      floors: [],
      roomUnits: [],
      sumTotal: {
        unpaidfeeAmount: 0,
        unpaidfeeAmounts: 0,
        paidfeeAmount: 0,
        paidfeeAmounts: 0,
        refundedAmount: 0,
        refundedAmounts: 0,
        refundInProgressAmount: 0,
        refundInProgressAmounts: 0,
        refundFailedAmount: 0,
        refundFailedAmounts: 0
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      total: 0
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.conditions.communityId = this.communityId
    this.initData()
  },
  methods: {
    async initData() {
      try {
        // 获取字典数据
        await Promise.all([
          this.getDictData('pay_fee', 'state'),
          this.getDictData('pay_fee', 'payer_obj_type'),
          this.getDictData('pay_fee_detail', 'state'),
          this.loadFloors()
        ])
        this.listFees()
      } catch (error) {
        console.error('初始化数据失败:', error)
      }
    },
    async getDictData(dictType, state) {
      try {
        const data = await getDict(dictType, state)
        if (dictType === 'pay_fee' && state === 'state') {
          this.states = data
        } else if (dictType === 'pay_fee' && state === 'payer_obj_type') {
          this.payerObjTypes = data
        } else if (dictType === 'pay_fee_detail' && state === 'state') {
          this.detailStates = data
        }
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async listFees() {
      this.loading = true
      try {
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size
        }
        const res = await queryPayFeeDeposit(params)
        this.depositFees = res.data
        this.total = res.total
        this.sumTotal = res.sumTotal
        if (res.data.length > 0) {
          this.feeConfigs = res.data[0].feeConfigDtos || []
        }
      } catch (error) {
        console.error('获取押金报表失败:', error)
      } finally {
        this.loading = false
      }
    },
    async loadFloors() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const data = await getFloors(params)
        this.floors = data.apiFloorDataVoList
      } catch (error) {
        console.error('加载楼栋数据失败:', error)
      }
    },
    async loadUnits(floorId) {
      try {
        const params = {
          floorId,
          communityId: this.communityId,
          page: 1,
          row: 100
        }
        const data = await queryUnits(params)
        this.roomUnits = data || []
      } catch (error) {
        console.error('获取单元列表失败:', error)
      }
    },
    async handleFloorChange(floorId) {
      try {
        this.conditions.unitId = ''
        this.roomUnits = []

        if (!floorId) return

        await this.loadUnits(floorId)
      } catch (error) {
        console.error('楼栋变化处理失败:', error)
      }
    },
    queryMethod() {
      this.page.current = 1
      this.listFees()
    },
    resetMethod() {
      this.conditions = {
        floorId: '',
        roomNum: '',
        unitId: '',
        feeId: '',
        state: '',
        payerObjType: '',
        startTime: '',
        endTime: '',
        configId: '',
        detailState: '',
        feeTypeCd: '888800010006',
        communityId: this.communityId,
        page: 1,
        row: 10
      }
      this.roomUnits = []
      this.listFees()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listFees()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listFees()
    },
    async exportFee() {
      try {
        this.conditions.pagePath = 'reportPayFeeDeposit'
        const res = await exportData(this.conditions)
        this.$message.success(res.msg)
        if (res.code === 0) {
          this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
        }
      } catch (error) {
        console.error('导出失败:', error)
      }
    },
    openPayFeeDetail(fee) {
      this.$router.push(`/views/fee/propertyFee?feeId=${fee.feeId}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.report-pay-fee-deposit-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;

    .search-item {
      margin-bottom: 15px;
    }
  }

  .table-card {
    margin-bottom: 20px;
    text-align: left;
    font-size: 13px;
    color: #606266;
  }
}
</style>