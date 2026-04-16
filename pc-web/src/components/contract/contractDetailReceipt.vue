<template>
  <div>
    <div class="flex justify-end">
      <div :span="12" class="padding-right-xs padding-left-xl"></div>
      <div :span="12" class="text-right padding-right-lg">
        <el-button type="primary" size="small" style="margin-left:10px" @click="_printFeeReceipt()">
          {{ $t('contractDetailReceipt.print') }}
        </el-button>
        <el-button type="primary" size="small" style="margin-left:10px" @click="_printFeeSmallReceipt()">
          {{ $t('contractDetailReceipt.printSmall') }}
        </el-button>
        <el-button type="primary" size="small" style="margin-left:10px" @click="_printApplyFeeReceipt()">
          {{ $t('contractDetailReceipt.printApply') }}
        </el-button>
      </div>
    </div>

    <div class="margin-top">
      <el-table :data="contractDetailReceiptInfo.feeReceipts" border style="width: 100%">
        <el-table-column width="50" align="center">
          <template slot-scope="scope">
            <el-checkbox v-model="contractDetailReceiptInfo.selectReceipts" :label="scope.row.receiptId"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="objName" :label="$t('contractDetailReceipt.feeType')" align="center"></el-table-column>
        <el-table-column prop="payObjName" :label="$t('contractDetailReceipt.owner')" align="center"></el-table-column>
        <el-table-column prop="feeName" :label="$t('contractDetailReceipt.feeItem')" align="center"></el-table-column>
        <el-table-column :label="$t('contractDetailReceipt.feePeriod')" align="center" width="200">
          <template slot-scope="scope">
            {{ dateFormat(scope.row.startTime) }}~<br />
            {{ dateFormat(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" :label="$t('contractDetailReceipt.totalAmount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.amount }}{{ $t('common.yuan') }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('contractDetailReceipt.payTime')" align="center"
          width="160"></el-table-column>
        <el-table-column prop="receiptId" :label="$t('contractDetailReceipt.receiptId')" align="center"
          width="120"></el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="24" class="text-right">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryFeeReceipt, listFeePrintPage } from '@/api/contract/contractDetailReceiptApi'
import { getCommunityId } from '@/api/community/communityApi'
import {dateFormat} from '@/utils/dateUtil'

export default {
  name: 'ContractDetailReceipt',
  data() {
    return {
      contractDetailReceiptInfo: {
        feeReceipts: [],
        payObjId: '',
        selectReceipts: [],
        printUrl: '/#/pages/property/printPayFee'
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._listFeePrintPages()
  },
  watch: {
    'contractDetailReceiptInfo.selectReceipts': {
      handler(newVal) {
        if (newVal.length === this.contractDetailReceiptInfo.feeReceipts.length) {
          this.contractDetailReceiptInfo.quan = true
        } else {
          this.contractDetailReceiptInfo.quan = false
        }
      },
      deep: true
    }
  },
  methods: {
    dateFormat,
    open(data) {
      this.contractDetailReceiptInfo.payObjId = data.ownerId
      this._listContractDetailReceipt(this.currentPage, this.pageSize)
    },
    switch(params) {
      if (!params.ownerId) return
      this.contractDetailReceiptInfo.payObjId = params.ownerId
      this._listContractDetailReceipt(this.currentPage, this.pageSize)
    },
    _listContractDetailReceipt(page, rows) {
      this.contractDetailReceiptInfo.selectReceipts = []
      const param = {
        page: page,
        row: rows,
        payObjId: this.contractDetailReceiptInfo.payObjId,
        communityId: this.communityId
      }

      queryFeeReceipt(param)
        .then(response => {
          this.contractDetailReceiptInfo.feeReceipts = response.data
          this.total = response.total
          this.currentPage = page
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this._listContractDetailReceipt(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listContractDetailReceipt(this.currentPage, this.pageSize)
    },
    _printFeeReceipt() {
      if (this.contractDetailReceiptInfo.selectReceipts.length < 1) {
        this.$message.warning(this.$t('contractDetailReceipt.selectPrintReceipt'))
        return
      }
      const receiptIds = this.contractDetailReceiptInfo.selectReceipts.join(',')
      window.open(`${this.contractDetailReceiptInfo.printUrl}?receiptIds=${receiptIds}&apply=N`)
    },
    _printApplyFeeReceipt() {
      if (this.contractDetailReceiptInfo.selectReceipts.length < 1) {
        this.$message.warning(this.$t('contractDetailReceipt.selectPrint'))
        return
      }
      const receiptIds = this.contractDetailReceiptInfo.selectReceipts.join(',')
      window.open(`/#/pages/property/printPayFee?receiptIds=${receiptIds}&apply=Y`)
    },
    _printFeeSmallReceipt() {
      if (this.contractDetailReceiptInfo.selectReceipts.length < 1) {
        this.$message.warning(this.$t('contractDetailReceipt.selectPrintReceipt'))
        return
      }
      const receiptIds = this.contractDetailReceiptInfo.selectReceipts.join(',')
      window.open(`/#/pages/property/printSmallPayFee?receiptIds=${receiptIds}`)
    },
    _listFeePrintPages() {
      const param = {
        page: 1,
        row: 1,
        state: 'T',
        communityId: this.communityId
      }

      listFeePrintPage(param)
        .then(response => {
          const feePrintPages = response.data
          if (feePrintPages && feePrintPages.length > 0) {
            this.contractDetailReceiptInfo.printUrl = feePrintPages[0].url
          }
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.margin-top-lg {
  margin-top: 30px;
}

.padding-right-xs {
  padding-right: 5px;
}

.padding-left-xl {
  padding-left: 30px;
}

.padding-right-lg {
  padding-right: 30px;
}
</style>