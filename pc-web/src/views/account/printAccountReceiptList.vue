<template>
  <div class="print-account-receipt-container">
    <div >
      <div class="print-content">
        <div class="text-center">
          <div style="color:#000;font-size:26px">{{ printAccountReceiptInfo.communityName }} {{
            $t('printAccountReceipt.depositSlip') }}</div>
        </div>
        <div style="color:#000;font-size:14px;margin-left:20px">
          <div class="float-left">
            <span style="color:#000;font-size:14px">
              {{ $t('printAccountReceipt.receiptNum') }}：{{ printAccountReceiptInfo.receiptNum }}
            </span>
          </div>
          <div class="float-right text-right">
            <span>{{ $t('printAccountReceipt.depositTime') }}：{{ printAccountReceiptInfo.feeTime }}</span>
          </div>
        </div>

        <table class="table vc-table-border" style="width:100%;color:#000;font-size:14px;border-collapse:collapse;border:1px solid #000">
          <thead>
            <tr>
              <th style="width:80px;text-align:center;padding:8px;border:1px solid #000">{{ $t('printAccountReceipt.number') }}</th>
              <th style="text-align:center;padding:8px;border:1px solid #000">{{ $t('printAccountReceipt.accountName') }}</th>
              <th style="text-align:center;padding:8px;border:1px solid #000">{{ $t('printAccountReceipt.accountType') }}</th>
              <th style="text-align:center;padding:8px;border:1px solid #000">{{ $t('printAccountReceipt.owner') }}</th>
              <th style="text-align:center;padding:8px;border:1px solid #000">{{ $t('printAccountReceipt.depositAmount') }}</th>
              <th style="text-align:center;padding:8px;border:1px solid #000">{{ $t('printAccountReceipt.depositMethod') }}</th>
              <th style="text-align:center;padding:8px;border:1px solid #000">{{ $t('printAccountReceipt.currentBalance') }}</th>
              <th style="width:200px;text-align:center;padding:8px;border:1px solid #000">{{ $t('printAccountReceipt.remark') }}</th>
            </tr>
          </thead>
          <tbody style="border:1px solid #000">
            <tr v-for="(item, index) in printAccountReceiptInfo.feeReceipts" :key="index" style="border:1px solid #000">
              <td style="text-align:center;padding:8px;border:1px solid #000">{{ index + 1 }}</td>
              <td style="text-align:center;padding:8px;border:1px solid #000">{{ item.acctName }}</td>
              <td style="text-align:center;padding:8px;border:1px solid #000">{{ item.acctTypeName }}</td>
              <td style="text-align:center;padding:8px;border:1px solid #000">{{ item.ownerName }}</td>
              <td style="text-align:center;padding:8px;border:1px solid #000">{{ item.receivedAmount }}</td>
              <td style="text-align:center;padding:8px;border:1px solid #000">{{ item.primeRateName }}</td>
              <td style="text-align:center;padding:8px;border:1px solid #000">{{ item.amount }}</td>
              <td style="text-align:center;padding:8px;border:1px solid #000">{{ item.remark }}</td>
            </tr>
            <tr style="border:1px solid #000">
              <td colspan="3" style="text-align:center;padding:8px;border:1px solid #000">
                {{ $t('printAccountReceipt.chineseAmount') }}（元）
              </td>
              <td colspan="4" style="text-align:center;padding:8px;border:1px solid #000">
                {{ changeNumMoneyToChinese(printAccountReceiptInfo.amount) }}
              </td>
              <td colspan="1" style="text-align:center;padding:8px;border:1px solid #000">
                {{ printAccountReceiptInfo.amount }}
              </td>
            </tr>
            <tr v-if="printAccountReceiptInfo.content || printAccountReceiptInfo.qrImg" style="border:1px solid #000">
              <td colspan="4" style="padding:8px;border:1px solid #000">
                <div style="max-width:600px" v-html="printAccountReceiptInfo.content"></div>
              </td>
              <td colspan="4" style="text-align:right;padding:8px;border:1px solid #000">
                <img v-if="printAccountReceiptInfo.qrImg" :src="printAccountReceiptInfo.qrImg" width="100px" height="100px">
              </td>
            </tr>
          </tbody>
        </table>

        <div style="color:#000;font-size:14px;margin:20px 0 0 10px;overflow:hidden">
          <div style="float:left;width:25%">
            {{ $t('printAccountReceipt.departmentHead') }}：
          </div>
          <div style="float:left;width:25%">
            {{ $t('printAccountReceipt.operator') }}：{{ userInfo.name }}
          </div>
          <div style="float:left;width:25%">
            {{ $t('printAccountReceipt.financialCollection') }}：
          </div>
          <div style="float:left;width:25%">
            {{ $t('printAccountReceipt.customerConfirmation') }}：
          </div>
        </div>

        <div id="print-btn" style="margin-top:20px;text-align:right">
          <button type="button" class="el-button el-button--warning" style="margin-right:20px" @click="handleCancel">
            {{ $t('common.cancel') }}
          </button>
          <button type="button" class="el-button el-button--primary" @click="handlePrint">
            <i class="el-icon-printer"></i>&nbsp;{{ $t('common.print') }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { listAccountReceipt, queryFeePrintSpec } from '@/api/account/printAccountReceiptApi'
import { getUserId, getUserName } from '@/api/user/userApi'
import { getCommunityId, getCommunityName } from '@/api/community/communityApi'
import { changeNumMoneyToChinese } from '@/utils/moneyUtil' 

export default {
  name: 'PrintAccountReceiptList',
  data() {
    return {
      printAccountReceiptInfo: {
        communityName: '',
        arIds: '',
        amount: 0.00,
        feeReceipts: [],
        content: '',
        qrImg: '',
        receiptNum: '',
        feeTime: ''
      },
      printFlag: '0',
      userInfo: {}
    }
  },
  created() {
    this.printAccountReceiptInfo.arIds = this.$route.query.arIds
    this.printAccountReceiptInfo.communityName = getCommunityName()
    this.loadReceipt()
    this.loadPrintSpec()
    this.userInfo = {
      id: getUserId(),
      name: getUserName()
    }
  },
  methods: {
    async loadReceipt() {
      try {
        const params = {
          page: 1,
          row: 30,
          arIds: this.printAccountReceiptInfo.arIds,
          communityId: getCommunityId()
        }

        const { data } = await listAccountReceipt(params)
        let amount = 0
        data.forEach(item => {
          amount += parseFloat(item.receivedAmount)
        })
        this.printAccountReceiptInfo.amount = amount.toFixed(2)
        this.printAccountReceiptInfo.feeReceipts = data
        if (data && data.length > 0) {
          this.printAccountReceiptInfo.receiptNum = data[0].arId
          this.printAccountReceiptInfo.feeTime = data[0].createTime
        }
      } catch (error) {
        this.$message.error(this.$t('printAccountReceipt.loadError'))
      }
    },
    async loadPrintSpec() {
      try {
        const params = {
          page: 1,
          row: 1,
          specCd: 2020,
          communityId: getCommunityId()
        }

        const { data } = await queryFeePrintSpec(params)
        if (data.length > 0) {
          this.printAccountReceiptInfo.content = data[0].content
          this.printAccountReceiptInfo.qrImg = data[0].qrImg
          if (data[0].printName) {
            this.printAccountReceiptInfo.communityName = data[0].printName
          }
        }
      } catch (error) {
        this.$message.error(this.$t('printAccountReceipt.loadSpecError'))
      }
    },
    changeNumMoneyToChinese,
    handlePrint() {
      this.printFlag = '1'
      document.getElementById("print-btn").style.display = "none"
      window.print()
      window.opener = null
      window.close()
    },
    handleCancel() {
      window.opener = null
      window.close()
    }
  }
}
</script>

<style scoped>
.print-account-receipt-container {
  padding: 20px;
}

.print-content {
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
</style>