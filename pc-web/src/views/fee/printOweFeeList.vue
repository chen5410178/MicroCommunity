<template>
  <div class="print-owe-fee-container">
    <el-card>
      <div class="text-center">
        <h1>{{ printPayFeeInfo.printName }}{{ $t('printOweFee.paymentNotice') }}</h1>
      </div>
      <div class="margin-top flex justify-between" style="color:#000;font-size:14px">
        <div >
          {{ $t('printOweFee.owner') }}：{{ printPayFeeInfo.ownerName }}
        </div>
        <div>
          {{ $t('printOweFee.printTime') }}：{{ printPayFeeInfo.feeTime }}
        </div>
      </div>
      <!-- 费用明细表格（原el-table） -->
      <table style="width: 100%; border-collapse: collapse; color: #000; font-size: 14px; margin-top: 10px;">
        <thead>
          <tr>
            <th style="border: 1px solid #dcdfe6; text-align: center;">{{$t('printOweFee.chargeObject')}}</th>
            <th style="border: 1px solid #dcdfe6; text-align: center;">{{$t('printOweFee.itemName')}}</th>
            <th style="border: 1px solid #dcdfe6; text-align: center;">{{$t('printOweFee.feePeriod')}}</th>
            <th style="border: 1px solid #dcdfe6; text-align: center;">{{$t('printOweFee.unit')}}</th>
            <th style="border: 1px solid #dcdfe6; text-align: center;">{{$t('printOweFee.areaUsage')}}</th>
            <th style="border: 1px solid #dcdfe6; text-align: center;">{{$t('printOweFee.unitPrice')}}</th>
            <th style="border: 1px solid #dcdfe6; text-align: center;">{{$t('printOweFee.amount')}}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, idx) in printPayFeeInfo.fees" :key="idx">
            <td style="border: 1px solid #dcdfe6; text-align: center;">{{ row.payerObjName }}</td>
            <td style="border: 1px solid #dcdfe6; text-align: center;">{{ row.feeName }}</td>
            <td style="border: 1px solid #dcdfe6; text-align: center;">
              <div v-if="row.preDegrees">
                {{ dateFormat(row.endTime) }}至{{ _getDeadlineTime(row) }}<br>
                {{ row.preDegrees }} 至 {{ row.curDegrees }}
              </div>
              <div v-else>
                {{ dateFormat(row.endTime) }}至{{ _getDeadlineTime(row) }}
              </div>
            </td>
            <td style="border: 1px solid #dcdfe6; text-align: center;">元</td>
            <td style="border: 1px solid #dcdfe6; text-align: center;">
              {{ row.preDegrees ? (row.curDegrees - row.preDegrees).toFixed(2) : printPayFeeInfo.builtUpArea }}
            </td>
            <td style="border: 1px solid #dcdfe6; text-align: center;">
              {{ row.preDegrees && row.mwPrice > 0 ? row.mwPrice : `${row.squarePrice}/${row.additionalAmount}` }}
            </td>
            <td style="border: 1px solid #dcdfe6; text-align: center;">{{ row.feeTotalPrice }}</td>
          </tr>
          <tr>
            <td style="border: 1px solid #dcdfe6; text-align: center;" >
              {{$t('printOweFee.amountInWords')}}
            </td>
            <td style="border: 1px solid #dcdfe6; text-align: center;" colspan="4">
              {{ changeNumMoneyToChinese(printPayFeeInfo.feePrices) }}
            </td>
            <td style="border: 1px solid #dcdfe6; text-align: center;" colspan="2">
              {{ printPayFeeInfo.feePrices }}
            </td>
          </tr>
          <tr v-if="printPayFeeInfo.content"> 
          <td style="border: 1px solid #dcdfe6; width: 300px;">
            <div style="max-width: 600px;" v-html="printPayFeeInfo.content"></div>
          </td>
          <td style="border: 1px solid #dcdfe6; text-align: center;">
            <img :src="printPayFeeInfo.qrImg" width="150px" height="150px">
          </td>
        </tr>
        </tbody>
      </table>


      <div id="print-btn" class="margin-top ">
        <el-button type="primary" class="float-right" @click="_printPurchaseApplyDiv">
          <i class="el-icon-printer"></i>&nbsp;{{ $t('common.print') }}
        </el-button>
        <el-button type="warning" class="float-right" style="margin-right:20px;" @click="_closePage">
          {{ $t('common.cancel') }}
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { listOweFees, listRoomsWhereFeeSet, queryFeePrintSpec } from '@/api/fee/printOweFeeApi'
import { getCommunityId } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'
import { changeNumMoneyToChinese } from '@/utils/moneyUtil'

export default {
  name: 'PrintOweFeeList',
  data() {
    return {
      printPayFeeInfo: {
        communityName: '',
        roomName: '',
        feePrices: 0.00,
        fees: [],
        feeTime: '',
        wechatName: '',
        content: '',
        qrImg: '',
        payObjId: '',
        builtUpArea: '',
        payObjType: '',
        payObjName: '',
        ownerName: '',
        printName: '',
        targetEndTime: '',
      },
      printFlag: '0',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initData()
  },
  methods: {
    _initData() {
      this.printPayFeeInfo.payObjId = this.$route.query.payObjId
      this.printPayFeeInfo.payObjType = this.$route.query.payObjType
      this.printPayFeeInfo.payObjName = this.$route.query.payObjName
      this.printPayFeeInfo.targetEndTime = this.$route.query.targetEndTime

      this._loadOweFees()
      this._printOweRoom()

      this.printPayFeeInfo.feeTime = dateFormat(new Date().getTime())
      this.printPayFeeInfo.communityName = this.communityId

      this._loadPrintSpec()
    },
    _loadPrintSpec() {
      const param = {
        page: 1,
        row: 1,
        specCd: 1010,
        communityId: this.communityId
      }

      queryFeePrintSpec(param).then(res => {
        if (res.data.length > 0) {
          this.printPayFeeInfo.content = res.data[0].content
          this.printPayFeeInfo.qrImg = res.data[0].qrImg
          this.printPayFeeInfo.printName = res.data[0].printName
        }
      }).catch(err => {
        console.error('请求失败:', err)
      })
    },
    _printPurchaseApplyDiv() {
      this.printFlag = '1'
      document.getElementById("print-btn").style.display = "none"
      window.print()
      window.opener = null
      window.close()
    },
    _closePage() {
      window.opener = null
      window.close()
    },
    _printOweRoom() {
      if (this.printPayFeeInfo.payObjType !== '3333') {
        return
      }
      const param = {
        page: 1,
        row: 1,
        roomId: this.printPayFeeInfo.payObjId,
        communityId: this.communityId
      }

      listRoomsWhereFeeSet(param).then(res => {
        const listRoomData = res
        Object.assign(this.printPayFeeInfo, listRoomData.rooms[0])
        if (listRoomData.rooms[0].roomType === '2020602') {
          this.printPayFeeInfo.roomName = `${listRoomData.rooms[0].floorNum}-${listRoomData.rooms[0].roomNum}`
        } else {
          this.printPayFeeInfo.roomName = `${listRoomData.rooms[0].floorNum}-${listRoomData.rooms[0].unitNum}-${listRoomData.rooms[0].roomNum}`
        }
      }).catch(err => {
        console.error('请求失败:', err)
      })
    },
    _loadOweFees() {
      const param = {
        page: 1,
        row: 50,
        communityId: this.communityId,
        payObjId: this.printPayFeeInfo.payObjId,
        payObjType: this.printPayFeeInfo.payObjType,
        targetEndTime: this.printPayFeeInfo.targetEndTime,
      }

      listOweFees(param).then(res => {
        const _fees = res.data
        if (_fees.length < 1) {
          this.printPayFeeInfo.oweFees = []
          this.$message.warning(this.$t('printOweFee.noData'))
          return
        }
        this.printPayFeeInfo.fees = _fees
        let _totalAmount = 0.0
        this.printPayFeeInfo.fees.forEach(item => {
          _totalAmount += item.feeTotalPrice
        })
        _totalAmount = Math.round(_totalAmount * 100) / 100
        this.printPayFeeInfo.feePrices = _totalAmount
      }).catch(err => {
        console.error('请求失败:', err)
      })
    },
    _getDeadlineTime(_fee) {
      if (_fee.amountOwed === 0 && _fee.endTime === _fee.deadlineTime) {
        return "-"
      }
      if (_fee.state === '2009001') {
        return "-"
      }
      return dateFormat(_fee.deadlineTime)
    },
    dateFormat,
    changeNumMoneyToChinese
  }
}
</script>

<style scoped>
.print-owe-fee-container {
  padding: 20px;
}

.margin-top {
  margin-top: 20px;
}

.float-right {
  float: right;
}

.text-center {
  text-align: center;
}
th{
  height: 40px;
}
td{
  height: 40px;
}
</style>