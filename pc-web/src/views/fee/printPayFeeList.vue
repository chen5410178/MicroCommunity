<template>
  <div class="print-pay-fee-container">
    <div class="text-center">
      <div style="color:#000;font-size:26px">{{printPayFeeInfo.communityName}}
        <span v-if="printPayFeeInfo.apply === 'Y'">{{ $t('printPayFee.apply') }}</span>
        <span v-if="printPayFeeInfo.apply === 'N'">{{ $t('printPayFee.receipt') }}</span>
        <span v-if="printPayFeeInfo.apply === 'R'">{{ $t('printPayFee.refund') }}</span>
      </div>
    </div>
    <div style="color:#000;font-size:18px;margin-left:20px">
      <div class="float-left">
        <span></span>
        <br />
        {{ $t('printPayFee.owner') }}：{{printPayFeeInfo.payObjName}}
      </div>
      <div class="float-right text-right">
        <span>{{ $t('printPayFee.receiptNum') }}:{{printPayFeeInfo.receiptNum}}</span>
        <br />
        <span>{{ $t('printPayFee.payTime') }}：{{printPayFeeInfo.feeTime}}</span>
      </div>
    </div>
    
    <table class="table vc-table-border" style="color:#000;font-size:18px;width:100%;border-collapse:collapse;">
      <thead>
        <tr>
          <th class="text-center" width="80px">{{ $t('printPayFee.serialNumber') }}</th>
          <th class="text-center">{{ $t('printPayFee.feeItem') }}</th>
          <th class="text-center">{{ $t('printPayFee.houseCar') }}</th>
          <th class="text-center">{{ $t('printPayFee.feeRange') }}</th>
          <th class="text-center">{{ $t('printPayFee.unitPrice') }}</th>
          <th class="text-center">{{ $t('printPayFee.areaUsage') }}</th>
          <th class="text-center">{{ $t('printPayFee.paymentMethod') }}</th>
          <th class="text-center">{{ $t('printPayFee.receivableActual') }}</th>
          <th class="text-center">{{ $t('printPayFee.discountAmount') }}</th>
          <th class="text-center">{{ $t('printPayFee.remark') }}</th>
        </tr>
      </thead>
      <tbody style="color:#000;font-size:20px">
        <tr v-for="(item,index) in printPayFeeInfo.fees" :key="index" class="vc-table-border">
          <th scope="row" class="text-center">{{index +1}}</th>
          <td class="text-center">{{item.feeName}}</td>
          <td class="text-center">{{item.objName}}</td>
          <td class="text-center">
            <div v-if="item.preDegrees">
              {{dateFormat(item.startTime)}}
              <span>{{ $t('printPayFee.to') }}</span>
              {{dateFormat(item.endTime)}}<br />
              {{item.preDegrees}} {{ $t('printPayFee.to') }} {{item.curDegrees}}
            </div>
            <div v-else-if="item.feeTypeCd === '888800010006'">
              {{ $t('printPayFee.none') }}
            </div>
            <div v-else>
              {{dateFormat(item.startTime)}}{{$t('printPayFee.to')}}{{dateFormat(item.endTime)}}
            </div>
          </td>
          <td class="text-center">{{item.squarePrice}}<br />{{item.units}}</td>
          <td class="text-center">{{item.area}}</td>
          <td class="text-center">{{item.primeRate}}</td>
          <td class="text-center">{{item.amount}}{{$t('printPayFee.yuan')}}
            <span v-if="item.amount < 0">({{ $t('printPayFee.refund') }})</span>
          </td>
          <td class="text-center">{{item.discountPrice || item.deductionAmount || 0}}</td>
          <td class="text-center" width="200px">{{item.remark}}</td>
        </tr>
        <tr>
          <td colspan="3" class="text-center">{{ $t('printPayFee.capitalRMB') }}</td>
          <td colspan="2" class="text-center">{{changeNumMoneyToChinese(printPayFeeInfo.amount)}}</td>
          <td colspan="1" class="text-center">{{printPayFeeInfo.amount}}</td>
          <td colspan="2" class="text-center">{{ $t('printPayFee.accountDeduction') }}</td>
          <td colspan="2" class="text-center">{{printPayFeeInfo.acctAmount}}</td>
        </tr>
        <tr v-if="printPayFeeInfo.content || printPayFeeInfo.qrImg">
          <td colspan="6">
            <div style="max-width: 600px;" v-html="printPayFeeInfo.content"></div>
          </td>
          <td colspan="4">
            <img v-if="printPayFeeInfo.qrImg" :src="printPayFeeInfo.qrImg" width="100px" height="100px">
          </td>
        </tr>
      </tbody>
    </table>

    <el-row style="color:#000;font-size:18px;margin-left: 10px;" class="flex justify-between">
      <el-col :span="6">{{ $t('printPayFee.departmentHead') }}</el-col>
      <el-col :span="6">{{ $t('printPayFee.operator') }}：{{userInfo.name}}</el-col>
      <el-col :span="6">{{ $t('printPayFee.financeReceipt') }}</el-col>
      <el-col :span="6">{{ $t('printPayFee.customerConfirm') }}</el-col>
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
import { getCommunityId, getCommunityName } from '@/api/community/communityApi'
import { queryFeeReceipt, queryFeeReceiptDetail, queryFeePrintSpec } from '@/api/fee/printPayFeeApi'
import { getUserId, getUserName } from '@/api/user/userApi'
import { dateFormat } from '@/utils/dateUtil'
import { changeNumMoneyToChinese } from '@/utils/moneyUtil' 

export default {
  name: 'PrintPayFeeList',
  data() {
    return {
      printPayFeeInfo: {
        communityName: '',
        receiptId: '',
        receiptIds: '',
        detailIds: '',
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
        acctAmount: 0
      },
      userInfo: {},
      printFlag: '0',
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
      if(this.$route.query.apply){
        this.printPayFeeInfo.apply = this.$route.query.apply
      }
      this.printPayFeeInfo.merge = this.$route.query.merge
      this.printPayFeeInfo.communityName =  getCommunityName()

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
        if (feeReceiptDetails[0].amount < 0) {
          this.printPayFeeInfo.apply = 'R'
        }
        let acctAmount = 0.0
        feeReceiptDetails.forEach(item => {
          acctAmount = acctAmount + parseFloat(item.acctAmount)
          this.printPayFeeInfo.feeReceipt.forEach(im => {
            if (item.receiptId == im.receiptId) {
              item.objName = im.objName
              item.feeTypeCd = im.feeTypeCd
            }
          })
        })
        this.printPayFeeInfo.fees = feeReceiptDetails
        this.printPayFeeInfo.acctAmount = acctAmount.toFixed(2)
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
    dateFormat(date) {
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

.el-row {
  width: 100%;
  margin-bottom: 20px;
}

.el-col {
  width: 100%;
}
</style>