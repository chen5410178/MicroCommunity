<template>
  <div class="print-pay-fee-container">
    <div class="text-center">
      <div style="color:#000;font-size:36px">
        {{ printPayFeeInfo.communityName }}
        <span v-if="printPayFeeInfo.apply === 'Y'">{{ $t('printPayFeeBangTai.applyForm') }}</span>
        <span v-if="printPayFeeInfo.apply === 'N'">{{ $t('printPayFeeBangTai.receiptForm') }}</span>
      </div>
      <span style="color:#000;font-size:20px">
        {{ $t('printPayFeeBangTai.receiptNum') }}：{{ printPayFeeInfo.receiptNum }}
      </span>
    </div>
    <div style="color:#000;font-size:20px;margin-left:20px">
      <div class="float-left">
        <span>{{ $t('printPayFeeBangTai.owner') }}</span>：{{ printPayFeeInfo.payObjName }}
      </div>
      <div class="float-right text-right">
        <span>{{ $t('printPayFeeBangTai.paymentTime') }}</span>：{{ printPayFeeInfo.feeTime }}
      </div>
    </div>

    <table class="table vc-table-border" style="color:#000;font-size:20px;width:100%;border-collapse:collapse;">
      <thead>
        <tr>
          <th class="text-center" width="80px">{{ $t('printPayFeeBangTai.serialNum') }}</th>
          <th class="text-center">{{ $t('printPayFeeBangTai.feeItem') }}</th>
          <th class="text-center">{{ $t('printPayFeeBangTai.houseCar') }}</th>
          <th class="text-center">{{ $t('printPayFeeBangTai.feeRange') }}</th>
          <th class="text-center">{{ $t('printPayFeeBangTai.cycle') }}</th>
          <th class="text-center">{{ $t('printPayFeeBangTai.unitPrice') }}</th>
          <th class="text-center">{{ $t('printPayFeeBangTai.areaUsage') }}</th>
          <th class="text-center">{{ $t('printPayFeeBangTai.paymentMethod') }}</th>
          <th class="text-center">{{ $t('printPayFeeBangTai.amount') }}</th>
          <th class="text-center">{{ $t('printPayFeeBangTai.discountAmount') }}</th>
          <th class="text-center">{{ $t('printPayFeeBangTai.remark') }}</th>
        </tr>
      </thead>
      <tbody style="color:#000;font-size:20px">
        <tr v-for="(item, index) in printPayFeeInfo.fees" :key="index">
          <th scope="row" class="text-center">{{ index + 1 }}</th>
          <td class="text-center">{{ item.feeName }}</td>
          <td class="text-center">{{ item.objName }}</td>
          <td class="text-center">
            <div v-if="item.preDegrees">
              {{ formatDate(item.startTime) }}{{ $t('printPayFeeBangTai.to') }}{{ formatDate(item.endTime) }}<br>
              {{ item.preDegrees }} {{ $t('printPayFeeBangTai.to') }} {{ item.curDegrees }}
            </div>
            <div v-else-if="item.feeTypeCd === '888800010006'">
              {{ $t('printPayFeeBangTai.none') }}
            </div>
            <div v-else>
              {{ formatDate(item.startTime) }}{{ $t('printPayFeeBangTai.to') }}{{ formatDate(item.endTime) }}
            </div>
          </td>
          <td class="text-center">{{ item.cycle }}</td>
          <td class="text-center">{{ item.squarePrice }}</td>
          <td class="text-center">{{ item.area }}</td>
          <td class="text-center">{{ item.primeRate }}</td>
          <td class="text-center">{{ item.amount }}</td>
          <td class="text-center">{{ item.discountPrice }}</td>
          <td class="text-center" width="200px">{{ item.remark }}</td>
        </tr>
        <tr>
          <td colspan="3" class="text-center">{{ $t('printPayFeeBangTai.capitalRMB') }}（元）</td>
          <td colspan="4" class="text-center">{{ changeNumMoneyToChinese(printPayFeeInfo.amount) }}</td>
          <td colspan="1" class="text-center">{{ computeSumArea() }}</td>
          <td colspan="3" class="text-center">{{ printPayFeeInfo.amount }}</td>
        </tr>
        <tr v-if="printPayFeeInfo.content || printPayFeeInfo.qrImg" height="60px">
          <td colspan="6">
            <div style="max-width: 600px;" v-html="printPayFeeInfo.content"></div>
          </td>
          <td colspan="5">
            <img v-if="printPayFeeInfo.qrImg" :src="printPayFeeInfo.qrImg" width="100px" height="100px">
          </td>
        </tr>
      </tbody>
    </table>

    <el-row style="color:#000;font-size:18px;margin-left: 10px;" class="flex justify-between">
      <el-col :span="6">{{ $t('printPayFeeBangTai.departmentHead') }}</el-col>
      <el-col :span="6">{{ $t('printPayFeeBangTai.handler') }}：{{ userInfo.name }}</el-col>
      <el-col :span="6">{{ $t('printPayFeeBangTai.financialCollection') }}</el-col>
      <el-col :span="6">{{ $t('printPayFeeBangTai.customerConfirmation') }}</el-col>
    </el-row>

    <el-row id="print-btn">
      <el-col :span="24">
        <el-button type="primary" class="float-right" @click="handlePrint">
          <i class="el-icon-printer"></i>&nbsp;{{ $t('common.print') }}
        </el-button>
        <el-button type="warning" class="float-right" style="margin-right:20px;" @click="handleClose">
          {{ $t('common.cancel') }}
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryFeeReceipt, queryFeeReceiptDetail, queryFeePrintSpec } from '@/api/fee/printPayFeeBangTaiApi'
import { getCommunityId,getCommunityName } from '@/api/community/communityApi'
import { getUserId,getUserName } from '@/api/user/userApi'
import { dateFormat } from '@/utils/dateUtil'
import { changeNumMoneyToChinese } from '@/utils/moneyUtil'

export default {
  name: 'PrintPayFeeBangTaiList',
  data() {
    return {
      printPayFeeInfo: {
        communityName: '',
        receiptId: '',
        receiptIds: '',
        roomName: '',
        amount: 0.00,
        fees: [],
        feeTime: '',
        wechatName: '',
        content: '',
        qrImg: '',
        payObjName: '',
        feeReceipt: [],
        apply: 'N',
        receiptNum: '',
        merge: ''
      },
      printFlag: '0',
      userInfo: {},
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.userInfo = {
      userId: getUserId(),
      name: getUserName()
    }
    this.initData()
  },
  methods: {
    initData() {
      this.printPayFeeInfo.receiptId = this.$route.query.receiptId
      this.printPayFeeInfo.receiptIds = this.$route.query.receiptIds
      this.printPayFeeInfo.detailIds = this.$route.query.detailIds

      this.printPayFeeInfo.apply = this.$route.query.apply || 'N'
      this.printPayFeeInfo.merge = this.$route.query.merge
      this.printPayFeeInfo.communityName = getCommunityName()

      this.loadReceipt()
      this.loadPrintSpec()
    },
    loadReceipt() {
      const params = {
        page: 1,
        row: 30,
        receiptId: this.printPayFeeInfo.receiptId,
        receiptIds: this.printPayFeeInfo.receiptIds,
        detailIds: this.printPayFeeInfo.detailIds,
        communityId: this.communityId
      }

      queryFeeReceipt(params).then(response => {
        const feeReceipt = response.data
        let amount = 0
        feeReceipt.forEach(item => {
          amount += parseFloat(item.amount)
        })

        this.printPayFeeInfo.amount = amount.toFixed(2)
        this.printPayFeeInfo.roomName = feeReceipt[0].objName
        this.printPayFeeInfo.feeTime = feeReceipt[0].createTime
        this.printPayFeeInfo.payObjName = feeReceipt[0].payObjName
        this.printPayFeeInfo.feeReceipt = feeReceipt
        this.printPayFeeInfo.receiptNum = feeReceipt[0].receiptCode

        this.loadReceiptDetail()
      })
    },
    loadReceiptDetail() {
      const params = {
        page: 1,
        row: 100,
        receiptId: this.printPayFeeInfo.receiptId,
        receiptIds: this.printPayFeeInfo.receiptIds,
        detailIds: this.printPayFeeInfo.detailIds,

        communityId: this.communityId,
        mergeFee: this.printPayFeeInfo.merge
      }

      queryFeeReceiptDetail(params).then(response => {
        const feeReceiptDetails = response.data
        this.printPayFeeInfo.receiptNum = this.printPayFeeInfo.receiptNum + "(" + feeReceiptDetails[0].payOrderId + ")"
        feeReceiptDetails.forEach(item => {
          this.printPayFeeInfo.feeReceipt.forEach(im => {
            if (item.receiptId == im.receiptId) {
              item.objName = im.objName
              item.feeTypeCd = im.feeTypeCd
            }
          })
        })
        this.printPayFeeInfo.fees = feeReceiptDetails
      })
    },
    loadPrintSpec() {
      const params = {
        page: 1,
        row: 1,
        specCd: 2020,
        communityId: this.communityId
      }

      queryFeePrintSpec(params).then(response => {
        const data = response.data
        if (data.length > 0) {
          this.printPayFeeInfo.content = data[0].content
          this.printPayFeeInfo.qrImg = data[0].qrImg
          if (data[0].printName) {
            this.printPayFeeInfo.communityName = data[0].printName
          }
        }
      })
    },
    computeSumArea() {
      let sum = 0.00
      this.printPayFeeInfo.fees.forEach(item => {
        if (item.area && this.isNumber(item.area)) {
          sum += parseFloat(item.area)
        }
      })
      return sum.toFixed(2)
    },
    isNumber(value) {
      return !isNaN(parseFloat(value)) && isFinite(value)
    },
    formatDate(date) {
      if (!date) return ''
      return dateFormat(date)
    },
    changeNumMoneyToChinese,
    handlePrint() {
      this.printFlag = '1'
      document.getElementById("print-btn").style.display = "none"
      window.print()
      window.close()
    },
    handleClose() {
      window.close()
    }
  }
}
</script>

<style scoped>
.print-pay-fee-container {
  padding: 20px;
}

.float-left {
  float: left;
}

.float-right {
  float: right;
}

.text-center {
  text-align: center;
}

.text-right {
  text-align: right;
}

.vc-table-border th,
.vc-table-border td {
  border: 1px solid #000;
  padding: 5px;
}

.el-row {
  width: 100%;
  margin-bottom: 20px;
}

.el-col {
  width: 100%;
}
</style>