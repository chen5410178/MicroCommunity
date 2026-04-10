<template>
  <div>
    <div class="flex justify-between">
      <div></div>
      <div >
        <el-button type="primary" size="small" @click="_printFeeAccountReceipt" style="margin-left:10px">
          {{ $t('ownerDetailAccountReceipt.print') }}
        </el-button>
        <el-button type="primary" size="small" @click="_printFeeSmallAccountReceipt" style="margin-left:10px">
          {{ $t('ownerDetailAccountReceipt.printSmall') }}
        </el-button>
      </div>
    </div>
    <div class="margin-top">
      <el-table :data="ownerDetailAccountReceiptInfo.feeReceipts" style="width: 100%; margin-top: 10px" border stripe>
        <el-table-column width="50" align="center">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked" @change="handleCheckChange(scope.row)"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="acctName" :label="$t('ownerDetailAccountReceipt.accountName')"
          align="center"></el-table-column>
        <el-table-column prop="acctTypeName" :label="$t('ownerDetailAccountReceipt.accountType')"
          align="center"></el-table-column>
        <el-table-column prop="ownerName" :label="$t('ownerDetailAccountReceipt.owner')"
          align="center"></el-table-column>
        <el-table-column prop="receivedAmount" :label="$t('ownerDetailAccountReceipt.prestoreAmount')"
          align="center"></el-table-column>
        <el-table-column prop="primeRateName" :label="$t('ownerDetailAccountReceipt.prestoreMethod')"
          align="center"></el-table-column>
        <el-table-column prop="amount" :label="$t('ownerDetailAccountReceipt.totalAmount')"
          align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('ownerDetailAccountReceipt.prestoreTime')"
          align="center"></el-table-column>
        <el-table-column prop="arId" :label="$t('ownerDetailAccountReceipt.receiptId')"
          align="center"></el-table-column>
      </el-table>
      <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
        layout="total, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { listAccountReceipt } from '@/api/owner/ownerDetailAccountReceiptApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDetailAccountReceipt',
  data() {
    return {
      ownerDetailAccountReceiptInfo: {
        feeReceipts: [],
        ownerId: '',
        total: 0,
        records: 0,
        selectReceipts: []
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
      this.ownerDetailAccountReceiptInfo.ownerId = ownerId
      this._listOwnerDetailAccountReceipt(this.currentPage, this.pageSize)
    },
    _listOwnerDetailAccountReceipt(page, rows) {
      this.ownerDetailAccountReceiptInfo.selectReceipts = []
      const param = {
        page: page,
        row: rows,
        ownerId: this.ownerDetailAccountReceiptInfo.ownerId,
        communityId: this.communityId
      }

      listAccountReceipt(param).then(response => {
        this.ownerDetailAccountReceiptInfo.feeReceipts = response.data.map(item => {
          return { ...item, checked: false }
        })
        this.total = response.data.total
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _printFeeAccountReceipt() {
      const selected = this.ownerDetailAccountReceiptInfo.feeReceipts.filter(item => item.checked)
      if (selected.length < 1) {
        this.$message.warning(this.$t('ownerDetailAccountReceipt.selectPrint'))
        return
      }
      const arIds = selected.map(item => item.arId).join(',')
      window.open(`/#/pages/property/printAccountReceipt?arIds=${arIds}&apply=N`)
    },
    _printFeeSmallAccountReceipt() {
      const selected = this.ownerDetailAccountReceiptInfo.feeReceipts.filter(item => item.checked)
      if (selected.length < 1) {
        this.$message.warning(this.$t('ownerDetailAccountReceipt.selectPrint'))
        return
      }
      const arIds = selected.map(item => item.arId).join(',')
      window.open(`/#/pages/property/printSmallAccountReceipt?arIds=${arIds}`)
    },
    handleCheckChange(row) {
      if (row.checked) {
        this.ownerDetailAccountReceiptInfo.selectReceipts.push(row.arId)
      } else {
        const index = this.ownerDetailAccountReceiptInfo.selectReceipts.indexOf(row.arId)
        if (index > -1) {
          this.ownerDetailAccountReceiptInfo.selectReceipts.splice(index, 1)
        }
      }
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listOwnerDetailAccountReceipt(val, this.pageSize)
    }
  }
}
</script>