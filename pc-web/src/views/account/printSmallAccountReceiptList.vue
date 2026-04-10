<template>
  <div class="printSmallAccountReceipt-container">
      <div class="print_container">
        <div style="color:#000;font-size:32px" class="text-center">
          <span>{{ $t('printSmallAccountReceipt.title') }}</span>
        </div>
        <span>**************************</span>
        <div class="section2" style="font-size: 12px; margin-left: 5px;">
          <div>
            <span>{{ $t('printSmallAccountReceipt.receiptNum') }}</span>：{{ printSmallAccountReceiptInfo.receiptNum }}
          </div>
          <div>
            <span>{{ $t('printSmallAccountReceipt.feeTime') }}</span>：{{ printSmallAccountReceiptInfo.feeTime }}
          </div>
        </div>
        <span>**************************</span>
        <div class="section2" style="font-size: 12px; margin-left: 5px;"
          v-for="(item, index) in printSmallAccountReceiptInfo.feeReceipts" :key="index">
          <div>
            <span>{{ $t('printSmallAccountReceipt.acctName') }}</span>{{ item.acctName }}
          </div>
          <div>
            <span>{{ $t('printSmallAccountReceipt.acctTypeName') }}</span>{{ item.acctTypeName }}
          </div>
          <div>
            <span>{{ $t('printSmallAccountReceipt.ownerName') }}</span>{{ item.ownerName }}
          </div>
          <div>
            <span>{{ $t('printSmallAccountReceipt.receivedAmount') }}</span>{{ item.receivedAmount }}
          </div>
          <div>
            <span>{{ $t('printSmallAccountReceipt.primeRateName') }}</span>{{ item.primeRateName }}
          </div>
          <div>
            <span>{{ $t('printSmallAccountReceipt.amount') }}</span>
            {{ item.amount }}
          </div>
          <div>
            <span>{{ $t('printSmallAccountReceipt.remark') }}</span>{{ item.remark }}
          </div>
          <span>**************************</span>
        </div>
        <div class="section5" style="font-size: 12px; margin-left: 5px;">
          <div>
            <span>{{ $t('printSmallAccountReceipt.totalAmount') }}</span>：{{ printSmallAccountReceiptInfo.amount }}
          </div>
          <div>
            <span>{{ $t('printSmallAccountReceipt.issuer') }}</span>：{{ userInfo.name }}
          </div>
          <div v-html="printSmallAccountReceiptInfo.content"></div>
          <div><img :src="printSmallAccountReceiptInfo.qrImg" width="100px" height="100px"></div>
        </div>
        <span>**************************</span>
      </div>

      <div id="print-btn">
        <el-button type="primary" class="float-right" @click="_printPurchaseApplyDiv()">
          <i class="el-icon-printer"></i>&nbsp;{{ $t('printSmallAccountReceipt.print') }}
        </el-button>
        <el-button class="float-right margin-right" @click="_openCloudPrint()">
          <i class="el-icon-cloudy"></i>&nbsp;{{ $t('printSmallAccountReceipt.cloudPrint') }}
        </el-button>
        <el-button type="default" class="float-right margin-right" @click="_closePage()">
          {{ $t('printSmallAccountReceipt.cancel') }}
        </el-button>
      </div>


  </div>
</template>

<script>
import { getCommunityId, getCommunityName } from '@/api/community/communityApi'
import { getUserId, getUserName } from '@/api/user/userApi'
import { listAccountReceipt, queryFeePrintSpec, listMachinePrinter, printAccountReceipt } from '@/api/account/printSmallAccountReceiptApi'

export default {
  name: 'PrintSmallAccountReceiptList',
  components: {

  },
  data() {
    return {
      printSmallAccountReceiptInfo: {
        communityName: '',
        arIds: '',
        amount: 0.00,
        feeReceipts: [],
        content: '',
        qrImg: '',
        apply: 'N',
        receiptNum: '',
        feeTime: '',
        machineId: '',
        quantity: '1',
        machines: []
      },
      userInfo: {},
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.printSmallAccountReceiptInfo.arIds = this.$route.query.arIds
    this.printSmallAccountReceiptInfo.communityName = getCommunityName()
    this.userInfo = {
      id: getUserId(),
      name: getUserName()
    }
    this._loadReceipt()
    this._loadPrintSpec()
  },
  methods: {
    async _loadReceipt() {
      try {
        const param = {
          page: 1,
          row: 30,
          arIds: this.printSmallAccountReceiptInfo.arIds,
          communityId: this.communityId
        }

        const { data } = await listAccountReceipt(param)
        let _amount = 0
        data.forEach(item => {
          _amount += parseFloat(item.receivedAmount)
        })
        this.printSmallAccountReceiptInfo.amount = _amount.toFixed(2)
        this.printSmallAccountReceiptInfo.feeReceipts = data
        if (data && data.length > 0) {
          this.printSmallAccountReceiptInfo.receiptNum = data[0].arId
          this.printSmallAccountReceiptInfo.feeTime = data[0].createTime
        }
      } catch (error) {
        console.error('加载收据失败:', error)
      }
    },
    async _loadPrintSpec() {
      try {
        const param = {
          page: 1,
          row: 1,
          specCd: 2020,
          communityId: this.communityId
        }

        const { data } = await queryFeePrintSpec(param)
        if (data.length > 0) {
          this.printSmallAccountReceiptInfo.content = data[0].content
          this.printSmallAccountReceiptInfo.qrImg = data[0].qrImg
        }
      } catch (error) {
        console.error('加载打印规格失败:', error)
      }
    },
    _printPurchaseApplyDiv() {
      let bdhtml = window.document.body.innerHTML
      let sprnstr = "<startprint></startprint>"
      let eprnstr = "<endprint></endprint>"
      let prnhtml = bdhtml.substr(bdhtml.indexOf(sprnstr) + sprnstr.length)
      prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr))
      window.document.body.innerHTML = prnhtml
      console.log(window.document.body.innerHTML)
      window.print()
      window.opener = null
      window.close()
    },
    _closePage() {
      window.opener = null
      window.close()
    },
    async _openCloudPrint() {
      let _arIds = [];
      this.printSmallAccountReceiptInfo.feeReceipts.forEach(_data => {
        _arIds.push(_data.arId);
      })
      if (_arIds.length < 1) {
        this.$message.error(this.$t('printSmallAccountReceipt.noFee'))
        return;
      }
      let _data = {
        communityId: getCommunityId(),
        machineId: this.printSmallAccountReceiptInfo.machineId,
        quantity: this.printSmallAccountReceiptInfo.quantity,
        arIds: _arIds.join(',')
      }
      await printAccountReceipt(_data)

    },
    async _listMachinePrinter() {
      try {
        const param = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }

        const { data } = await listMachinePrinter(param)
        this.printSmallAccountReceiptInfo.machines = data

        if (this.printSmallAccountReceiptInfo.machines && this.printSmallAccountReceiptInfo.machines.length > 0) {
          this.printSmallAccountReceiptInfo.machineId = this.printSmallAccountReceiptInfo.machines[0].machineId
        }
      } catch (error) {
        console.error('加载打印机失败:', error)
      }
    },
    handleCloudPrintSuccess() {
      this.$message.success(this.$t('common.operationSuccess'))
    }
  }
}
</script>

<style lang="scss" scoped>
.printSmallAccountReceipt-container {
  padding: 20px;

  .print_container {
    margin-bottom: 20px;
  }

  #print-btn {
    margin-top: 20px;
    text-align: right;

    .float-right {
      float: right;
    }

    .margin-right {
      margin-right: 10px;
    }
  }
}
</style>