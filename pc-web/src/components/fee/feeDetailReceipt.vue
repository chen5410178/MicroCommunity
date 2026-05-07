<template>
  <div>
    <div class="flex justify-between">
      <div ></div>
      <div  class="text-right">
        <el-button type="primary" size="small" @click="_printFeeReceipt">{{ $t('feeDetailReceipt.print') }}</el-button>
        <el-button type="primary" size="small" @click="_printFeeSmallReceipt">{{ $t('feeDetailReceipt.printSmall')
          }}</el-button>
        <el-button type="primary" size="small" @click="_printApplyFeeReceipt">{{ $t('feeDetailReceipt.printApply')
          }}</el-button>
      </div>
    </div>

    <div class="margin-top">
      <el-table :data="feeDetailReceiptInfo.feeReceipts" border style="width: 100%">
        <el-table-column width="60" align="center">
          <template #header>
            <el-checkbox v-model="feeDetailReceiptInfo.quan" @change="checkAllReceipt"></el-checkbox>
          </template>
          <template #default="{ row }">
            <el-checkbox v-model="feeDetailReceiptInfo.selectReceipts" :label="row.receiptId"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="objName" :label="$t('feeDetailReceipt.feeType')" align="center"></el-table-column>
        <el-table-column prop="payObjName" :label="$t('feeDetailReceipt.owner')" align="center"></el-table-column>
        <el-table-column prop="feeName" :label="$t('feeDetailReceipt.feeItem')" align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailReceipt.feePeriod')" align="center">
          <template #default="{ row }">
            {{ $dayjs(row.startTime).format('YYYY-MM-DD') }}~<br />
            {{ $dayjs(row.endTime).format('YYYY-MM-DD') }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" :label="$t('feeDetailReceipt.totalAmount')" align="center">
          <template #default="{ row }">
            {{ row.amount }}{{ $t('feeDetailReceipt.yuan') }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('feeDetailReceipt.paymentTime')" align="center"></el-table-column>
        <el-table-column prop="receiptId" :label="$t('feeDetailReceipt.receiptId')" align="center"></el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="24" class="float-right">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next, jumper" :total="total"></el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryFeeReceipt } from '@/api/fee/feeDetailReceiptApi'
import {listFeePrintPage} from '@/api/fee/feeReceiptApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeDetailReceipt',
  data() {
    return {
      feeDetailReceiptInfo: {
        feeReceipts: [],
        feeId: '',
        total: 0,
        records: 0,
        selectReceipts: [],
        quan: false,
        printUrl: '/#/pages/property/printPayFee'
      },
      currentPage: 1,
      pageSize: 10
    }
  },
  watch: {
    'feeDetailReceiptInfo.selectReceipts': {
      handler(newVal) {
        this.feeDetailReceiptInfo.quan = newVal.length === this.feeDetailReceiptInfo.feeReceipts.length
      },
      deep: true
    }
  },
  methods: {
    open(params) {
      if (!params.feeId) return
      this.feeDetailReceiptInfo = Object.assign(this.feeDetailReceiptInfo, params)
      this._listFeePrintPages()
      this._listFeeDetailReceipt(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listFeeDetailReceipt(this.currentPage, this.pageSize)
    },
    async _listFeeDetailReceipt(page, rows) {
      try {
        this.feeDetailReceiptInfo.selectReceipts = []
        this.feeDetailReceiptInfo.quan = false

        const params = {
          page,
          row: rows,
          feeId: this.feeDetailReceiptInfo.feeId,
          communityId: getCommunityId()
        }

        const res = await queryFeeReceipt(params)
        this.feeDetailReceiptInfo.total = res.total
        this.feeDetailReceiptInfo.records = res.records
        this.feeDetailReceiptInfo.feeReceipts = res.data
      } catch (error) {
        console.error('Failed to load receipt data:', error)
      }
    },
    _printFeeReceipt() {
      if (this.feeDetailReceiptInfo.selectReceipts.length < 1) {
        this.$message.warning(this.$t('feeDetailReceipt.selectPrintReceipt'))
        return
      }

      const receiptIds = this.feeDetailReceiptInfo.selectReceipts.join(',')
      window.open(`${this.feeDetailReceiptInfo.printUrl}?receiptIds=${receiptIds}&apply=N`)
    },
    _printApplyFeeReceipt() {
      if (this.feeDetailReceiptInfo.selectReceipts.length < 1) {
        this.$message.warning(this.$t('feeDetailReceipt.selectPrintApply'))
        return
      }

      const receiptIds = this.feeDetailReceiptInfo.selectReceipts.join(',')
      window.open(`/#/pages/property/printPayFee?receiptIds=${receiptIds}&apply=Y`)
    },
    _printFeeSmallReceipt() {
      if (this.feeDetailReceiptInfo.selectReceipts.length < 1) {
        this.$message.warning(this.$t('feeDetailReceipt.selectPrintReceipt'))
        return
      }

      const receiptIds = this.feeDetailReceiptInfo.selectReceipts.join(',')
      window.open(`/#/pages/property/printSmallPayFee?receiptIds=${receiptIds}`)
    },
    checkAllReceipt(e) {
      if (e) {
        this.feeDetailReceiptInfo.selectReceipts = this.feeDetailReceiptInfo.feeReceipts.map(item => item.receiptId)
      } else {
        this.feeDetailReceiptInfo.selectReceipts = []
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

        const res = await listFeePrintPage(params)
        if (res.data && res.data.length > 0) {
          this.feeDetailReceiptInfo.printUrl = res.data[0].url
        }
      } catch (error) {
        console.error('Failed to load print pages:', error)
      }
    }
  }
}
</script>