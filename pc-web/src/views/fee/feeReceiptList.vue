<template>
  <div class="fee-receipt-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('feeReceipt.search.title') }}</span>
        <el-button type="text" style="float: right; padding: 3px 0" @click="_moreCondition">
          {{ feeReceiptManageInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="feeReceiptManageInfo.conditions.receiptId" :placeholder="$t('feeReceipt.search.receiptId')"
            clearable />
        </el-col>
        <el-col :span="8">
          <el-input v-model="feeReceiptManageInfo.conditions.roomId" :placeholder="$t('feeReceipt.search.roomId')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="feeReceiptManageInfo.conditions.objType" :placeholder="$t('feeReceipt.search.objType')"
            style="width:100%">
            <el-option :label="$t('feeReceipt.search.all')" value="" />
            <el-option :label="$t('feeReceipt.search.houseFee')" value="3333" />
            <el-option :label="$t('feeReceipt.search.parkingFee')" value="6666" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="_queryFeeReceiptMethod">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="_resetFeeReceiptMethod">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <el-row v-show="feeReceiptManageInfo.moreCondition" :gutter="20" style="margin-top:20px">
        <el-col :span="6">
          <el-date-picker v-model="feeReceiptManageInfo.conditions.qstartTime" type="date"
            :placeholder="$t('feeReceipt.search.startTime')" style="width:100%" />
        </el-col>
        <el-col :span="6">
          <el-date-picker v-model="feeReceiptManageInfo.conditions.qendTime" type="date"
            :placeholder="$t('feeReceipt.search.endTime')" style="width:100%" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 收据信息 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('feeReceipt.list.title') }}</span>
      </div>

      <el-table v-loading="loading" :data="feeReceiptManageInfo.feeReceipts" border style="width: 100%">
        <el-table-column prop="receiptId" :label="$t('feeReceipt.table.receiptId')" align="center" />
        <el-table-column prop="receiptCode" :label="$t('feeReceipt.table.receiptCode')" align="center">
          <template slot-scope="scope">
            {{ scope.row.receiptCode || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="objType" :label="$t('feeReceipt.table.objType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.objType === '3333' ? $t('feeReceipt.search.houseFee') : $t('feeReceipt.search.parkingFee') }}
          </template>
        </el-table-column>
        <el-table-column prop="feeName" :label="$t('feeReceipt.table.feeName')" align="center" />
        <el-table-column prop="roomName" :label="$t('feeReceipt.table.roomName')" align="center" />
        <el-table-column prop="payObjName" :label="$t('feeReceipt.table.payObjName')" align="center" />
        <el-table-column prop="carNum" :label="$t('feeReceipt.table.carNum')" align="center" />
        <el-table-column prop="amount" :label="$t('feeReceipt.table.amount')" align="center" />
        <el-table-column :label="$t('feeReceipt.table.feeMonth')" align="center">
          <template slot-scope="scope">
            {{ scope.row.feeMonth || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('feeReceipt.table.cycle')" align="center">
          <template slot-scope="scope">
            {{ scope.row.startTime || '-' }} ~ {{ scope.row.endTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('feeReceipt.table.payWay')" align="center">
          <template slot-scope="scope">
            {{ scope.row.primeRate || scope.row.payWay || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('feeReceipt.table.deductionAmount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.deductionAmount || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('feeReceipt.table.lateAmount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.lateAmount || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('feeReceipt.table.acctAmount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.acctAmount || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('feeReceipt.table.degree')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.startDegree || scope.row.endDegree">
              {{ scope.row.startDegree || '-' }} ~ {{ scope.row.endDegree || '-' }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('feeReceipt.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="_printFeeReceipt(scope.row)">
              {{ $t('feeReceipt.button.reprint') }}
            </el-button>
            <el-button size="mini" type="success" @click="_printFeeSmallReceipt(scope.row)">
              {{ $t('feeReceipt.button.reprintSmall') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>
  </div>
</template>

<script>
import { queryFeeReceipt, listFeePrintPage } from '@/api/fee/feeReceiptApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeReceiptList',
  data() {
    return {
      loading: false,
      feeReceiptManageInfo: {
        feeReceipts: [],
        moreCondition: false,
        printUrl: '/#/pages/property/printPayFee',
        conditions: {
          objType: '',
          storeName: '',
          objId: '',
          month: '',
          qstartTime: '',
          qendTime: '',
          type: '',
          roomId: '',
          communityId: getCommunityId(),
          receiptId: '',
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
    this._listFeeReceipts()
    this._listFeePrintPages()
  },
  methods: {
    async _listFeeReceipts() {
      try {
        this.loading = true
        const params = {
          ...this.feeReceiptManageInfo.conditions,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await queryFeeReceipt(params)
        this.feeReceiptManageInfo.feeReceipts = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('feeReceipt.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async _listFeePrintPages() {
      try {
        const params = {
          page: 1,
          row: 1,
          state: 'T',
          communityId: getCommunityId()
        }
        const { data } = await listFeePrintPage(params)
        if (data && data.length > 0) {
          this.feeReceiptManageInfo.printUrl = data[0].url
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _queryFeeReceiptMethod() {
      this.page.current = 1
      this._listFeeReceipts()
    },
    _resetFeeReceiptMethod() {
      this.feeReceiptManageInfo.conditions = {
        ...this.feeReceiptManageInfo.conditions,
        objType: '',
        roomId: '',
        receiptId: '',
        qstartTime: '',
        qendTime: ''
      }
      this._listFeeReceipts()
    },
    _printFeeReceipt(receipt) {
      window.open(`${this.feeReceiptManageInfo.printUrl}?receiptId=${receipt.receiptId}`)
    },
    _printFeeSmallReceipt(receipt) {
      window.open(`/#/pages/property/printSmallPayFee?receiptIds=${receipt.receiptId}`)
    },
    _moreCondition() {
      this.feeReceiptManageInfo.moreCondition = !this.feeReceiptManageInfo.moreCondition
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listFeeReceipts()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listFeeReceipts()
    }
  }
}
</script>

<style lang="scss" scoped>
.fee-receipt-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: 20px;
    }
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>