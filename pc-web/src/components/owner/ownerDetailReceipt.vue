<template>
  <div>
    <el-row class="margin-top-lg">
      <el-col :span="12"></el-col>
      <el-col :span="12" class="text-right">
        <el-button type="primary" size="small" @click="_printFeeReceipt" style="margin-left:10px">
          {{$t('ownerDetailReceipt.print')}}
        </el-button>
        <el-button type="primary" size="small" @click="_printFeeSmallReceipt" style="margin-left:10px">
          {{$t('ownerDetailReceipt.printSmall')}}
        </el-button>
        <el-button type="primary" size="small" @click="_printApplyFeeReceipt" style="margin-left:10px">
          {{$t('ownerDetailReceipt.printApply')}}
        </el-button>
      </el-col>
    </el-row>
    <div class="margin-top">
      <el-table
        :data="ownerDetailReceiptInfo.feeReceipts"
        style="width: 100%; margin-top: 10px"
        border
        stripe
      >
        <el-table-column width="50" align="center">
          <template slot-scope="scope">
            <el-checkbox 
              v-model="scope.row.checked"
              @change="handleCheckChange(scope.row)"
            ></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="objName" :label="$t('ownerDetailReceipt.feeType')" align="center"></el-table-column>
        <el-table-column prop="payObjName" :label="$t('ownerDetailReceipt.owner')" align="center"></el-table-column>
        <el-table-column prop="feeName" :label="$t('ownerDetailReceipt.feeItem')" align="center"></el-table-column>
        <el-table-column :label="$t('ownerDetailReceipt.chargePeriod')" align="center">
          <template slot-scope="scope">
            {{dateFormat(scope.row.startTime)}}~<br>
            {{dateFormat(scope.row.endTime)}}
          </template>
        </el-table-column>
        <el-table-column prop="amount" :label="$t('ownerDetailReceipt.totalAmount')" align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('ownerDetailReceipt.paymentTime')" align="center"></el-table-column>
        <el-table-column prop="receiptId" :label="$t('ownerDetailReceipt.receiptId')" align="center"></el-table-column>
      </el-table>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { queryFeeReceipt, listFeePrintPage } from '@/api/owner/ownerDetailReceiptApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDetailReceipt',
  data() {
    return {
      ownerDetailReceiptInfo: {
        feeReceipts: [],
        payObjId: '',
        total: 0,
        records: 0,
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
  },
  methods: {
    open(ownerId) {
      this.ownerDetailReceiptInfo.payObjId = ownerId
      this._listFeePrintPages()
      this._listOwnerDetailReceipt(this.currentPage, this.pageSize)
    },
    _listOwnerDetailReceipt(page, rows) {
      this.ownerDetailReceiptInfo.selectReceipts = []
      const param = {
        page: page,
        row: rows,
        payObjId: this.ownerDetailReceiptInfo.payObjId,
        communityId: this.communityId
      }

      queryFeeReceipt(param).then(response => {
        this.ownerDetailReceiptInfo.feeReceipts = response.data.data.map(item => {
          return { ...item, checked: false }
        })
        this.total = response.data.total
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _listFeePrintPages() {
      const param = {
        page: 1,
        row: 1,
        state: 'T',
        communityId: this.communityId
      }

      listFeePrintPage(param).then(response => {
        const feePrintPages = response.data.data
        if (feePrintPages && feePrintPages.length > 0) {
          this.ownerDetailReceiptInfo.printUrl = feePrintPages[0].url
        }
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _printFeeReceipt() {
      const selected = this.ownerDetailReceiptInfo.feeReceipts.filter(item => item.checked)
      if (selected.length < 1) {
        this.$message.warning(this.$t('ownerDetailReceipt.selectPrint'))
        return
      }
      const receiptIds = selected.map(item => item.receiptId).join(',')
      window.open(`${this.ownerDetailReceiptInfo.printUrl}?receiptIds=${receiptIds}&apply=N`)
    },
    _printApplyFeeReceipt() {
      const selected = this.ownerDetailReceiptInfo.feeReceipts.filter(item => item.checked)
      if (selected.length < 1) {
        this.$message.warning(this.$t('ownerDetailReceipt.selectPrint'))
        return
      }
      const receiptIds = selected.map(item => item.receiptId).join(',')
      window.open(`/#/pages/property/printPayFee?receiptIds=${receiptIds}&apply=Y`)
    },
    _printFeeSmallReceipt() {
      const selected = this.ownerDetailReceiptInfo.feeReceipts.filter(item => item.checked)
      if (selected.length < 1) {
        this.$message.warning(this.$t('ownerDetailReceipt.selectPrint'))
        return
      }
      const receiptIds = selected.map(item => item.receiptId).join(',')
      window.open(`/#/pages/property/printSmallPayFee?receiptIds=${receiptIds}`)
    },
    handleCheckChange(row) {
      if (row.checked) {
        this.ownerDetailReceiptInfo.selectReceipts.push(row.receiptId)
      } else {
        const index = this.ownerDetailReceiptInfo.selectReceipts.indexOf(row.receiptId)
        if (index > -1) {
          this.ownerDetailReceiptInfo.selectReceipts.splice(index, 1)
        }
      }
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listOwnerDetailReceipt(val, this.pageSize)
    },
    dateFormat(date) {
      // Implement your date formatting logic here
      return date
    }
  }
}
</script>