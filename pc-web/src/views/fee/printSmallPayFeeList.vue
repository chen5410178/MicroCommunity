<template>
  <div class="printSmallPayFee-container">

      <div class="print_container">
        <div style="color:#000;font-size:32px" class="text-center">
          <span>{{ $t('printSmallPayFee.receipt') }}</span>
        </div>
        <span>**************************</span>
        <div class="section2" style="font-size: 12px; margin-left: 5px;">
          <div>
            <span>{{ $t('printSmallPayFee.receiptNum') }}</span>{{ printSmallPayFeeInfo.receiptNum }}
          </div>
          <div>
            <span>{{ $t('printSmallPayFee.orderNum') }}</span>{{ printSmallPayFeeInfo.payOrderId }}
          </div>
          <div>
            <span>{{ $t('printSmallPayFee.roomNum') }}</span>: {{ printSmallPayFeeInfo.roomName }}
          </div>
          <div>
            <span>{{ $t('printSmallPayFee.owner') }}</span>: {{ printSmallPayFeeInfo.payObjName }}
          </div>
          <div>
            <span>{{ $t('printSmallPayFee.time') }}</span>: {{ printSmallPayFeeInfo.feeTime }}
          </div>
        </div>
        <span>**************************</span>
        <div class="section2" style="font-size: 12px; margin-left: 5px;"
          v-for="(item, index) in printSmallPayFeeInfo.fees" :key="index">
          <div>
            <span>{{ $t('printSmallPayFee.feeItem') }}</span>: {{ item.feeName }}
          </div>
          <div>
            <span>{{ $t('printSmallPayFee.feeRange') }}</span>: {{ formatDate(item.startTime) }}
            <span>{{ $t('printSmallPayFee.to') }}</span>{{ formatDate(item.endTime) }}
          </div>
          <div>
            <span>{{ $t('printSmallPayFee.unitPrice') }}</span>{{ item.squarePrice }}
          </div>
          <div>
            <span>{{ $t('printSmallPayFee.areaUsage') }}</span>{{ item.area }}
          </div>
          <div>
            <span>{{ $t('printSmallPayFee.paymentMethod') }}</span>{{ item.primeRate }}
          </div>
          <div v-if="item.preDegrees">
            <span>{{ $t('printSmallPayFee.meterReading') }}</span>{{ item.preDegrees }} {{ $t('printSmallPayFee.to') }}
            {{ item.curDegrees }}
          </div>
          <div>
            <span>{{ $t('printSmallPayFee.amount') }}</span>
            <span>{{ formatAmount(item.amount) }}</span>
          </div>
          <div>
            <span>{{ $t('printSmallPayFee.discount') }}</span> {{ item.discountPrice || 0 }}
          </div>
          <div>
            <span>{{ $t('printSmallPayFee.remark') }}</span>: {{ item.remark }}
          </div>
          <span>**************************</span>
        </div>
        <div class="section5" style="font-size: 12px; margin-left: 5px;">
          <div>
            <span>{{ $t('printSmallPayFee.total') }}</span>: {{ printSmallPayFeeInfo.amount }}
          </div>
          <div>
            <span>{{ $t('printSmallPayFee.issuer') }}</span>: {{ userInfo.name }}
          </div>
          <div v-html="printSmallPayFeeInfo.content"></div>
          <div><img :src="printSmallPayFeeInfo.qrImg" width="100px" height="100px"></div>
        </div>
        <span>**************************</span>
      </div>

      <div id="print-btn" class="action-buttons">
        <el-button type="primary" class="float-right" @click="handlePrint">
          <i class="el-icon-printer"></i>&nbsp;{{ $t('printSmallPayFee.print') }}
        </el-button>
        <el-button class="float-right margin-right" @click="handleCloudPrint">
          <i class="el-icon-cloudy"></i>&nbsp;{{ $t('printSmallPayFee.cloudPrint') }}
        </el-button>
        <el-button class="float-right margin-right" @click="handleClose">
          <span>{{ $t('printSmallPayFee.cancel') }}</span>
        </el-button>
      </div>


    <el-dialog :title="$t('printSmallPayFee.cloudPrint')" :visible.sync="cloudPrintVisible" width="50%">
      <div class="cloud-print-content">
        <el-form label-width="120px">
          <el-form-item :label="$t('printSmallPayFee.printer')">
            <el-select v-model="printSmallPayFeeInfo.machineId" style="width:100%"
              :placeholder="$t('printSmallPayFee.printerRequired')">
              <el-option v-for="(item, index) in printSmallPayFeeInfo.machines" :key="index"
                :label="`${item.machineName}(${item.machineCode})`" :value="item.machineId" />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('printSmallPayFee.quantity')">
            <el-input v-model="printSmallPayFeeInfo.quantity" type="number"
              :placeholder="$t('printSmallPayFee.quantityRequired')" />
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">{{ $t('printSmallPayFee.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmitCloudPrint">{{ $t('printSmallPayFee.submit') }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryFeeReceipt,
  queryFeeReceiptDetail,
  queryFeePrintSpec,
  listMachinePrinter,
  printPayFeeDetail
} from '@/api/fee/printSmallPayFeeApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getUserId,getUserName } from '@/api/user/userApi'

export default {
  name: 'PrintSmallPayFeeList',
  data() {
    return {
      printSmallPayFeeInfo: {
        communityName: '',
        receiptId: '',
        receiptIds: '',
        receiptNum: '',
        payOrderId: '',
        roomName: '',
        amount: 0.00,
        fees: [],
        feeTime: '',
        wechatName: '',
        content: '',
        qrImg: '',
        payObjName: '',
        machineId: '',
        quantity: '1',
        machines: []
      },
      cloudPrintVisible: false,
      userInfo: {}
    }
  },
  created() {
    this.printSmallPayFeeInfo.receiptId = this.$route.query.receiptId
    this.printSmallPayFeeInfo.receiptIds = this.$route.query.receiptIds
    this.printSmallPayFeeInfo.communityName = getCommunityId().name
    this.loadData()
    this.userInfo = {
      userId: getUserId(),
      name: getUserName()
    }
  },
  methods: {
    async loadData() {
      await this.loadReceipt()
      await this.loadReceiptDetail()
      await this.loadPrintSpec()
    },
    async loadReceipt() {
      try {
        const params = {
          page: 1,
          row: 30,
          receiptId: this.printSmallPayFeeInfo.receiptId,
          receiptIds: this.printSmallPayFeeInfo.receiptIds,
          communityId: getCommunityId()
        }
        const { data } = await queryFeeReceipt(params)
        const _feeReceipt = data
        let _amount = 0
        _feeReceipt.forEach(item => {
          _amount += parseFloat(item.amount)
        })
        this.printSmallPayFeeInfo.amount = _amount.toFixed(2)
        this.printSmallPayFeeInfo.roomName = _feeReceipt[0].objName
        this.printSmallPayFeeInfo.feeTime = _feeReceipt[0].createTime
        this.printSmallPayFeeInfo.payObjName = _feeReceipt[0].payObjName
        this.printSmallPayFeeInfo.receiptNum = _feeReceipt[0].receiptCode
      } catch (error) {
        console.error('Failed to load receipt:', error)
      }
    },
    async loadReceiptDetail() {
      try {
        const params = {
          page: 1,
          row: 100,
          receiptId: this.printSmallPayFeeInfo.receiptId,
          receiptIds: this.printSmallPayFeeInfo.receiptIds,
          communityId: getCommunityId(),
          orderBy: 'start_time'
        }
        const { data } = await queryFeeReceiptDetail(params)
        this.printSmallPayFeeInfo.payOrderId = data[0].payOrderId
        this.printSmallPayFeeInfo.fees = data
      } catch (error) {
        console.error('Failed to load receipt detail:', error)
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
          this.printSmallPayFeeInfo.content = data[0].content
          this.printSmallPayFeeInfo.qrImg = data[0].qrImg
        }
      } catch (error) {
        console.error('Failed to load print spec:', error)
      }
    },
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleDateString()
    },
    formatAmount(amount) {
      if (amount < 0) {
        return amount + this.$t('printSmallPayFee.refund')
      }
      return amount
    },
    handlePrint() {
      window.print()
    },
    handleCloudPrint() {
      this.listMachinePrinter()
      this.cloudPrintVisible = true
    },
    async listMachinePrinter() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId()
        }
        const { data } = await listMachinePrinter(params)
        this.printSmallPayFeeInfo.machines = data
        if (this.printSmallPayFeeInfo.machines && this.printSmallPayFeeInfo.machines.length > 0) {
          this.printSmallPayFeeInfo.machineId = this.printSmallPayFeeInfo.machines[0].machineId
        }
      } catch (error) {
        console.error('Failed to list machine printer:', error)
      }
    },
    async handleSubmitCloudPrint() {
      try {
        const detailIds = this.printSmallPayFeeInfo.fees.map(item => item.detailId)
        if (detailIds.length < 1) {
          this.$message.error(this.$t('printSmallPayFee.noFee'))
          return
        }
        const data = {
          communityId: getCommunityId(),
          machineId: this.printSmallPayFeeInfo.machineId,
          quantity: this.printSmallPayFeeInfo.quantity,
          detailId: detailIds.join(',')
        }
        const {code,msg} = await printPayFeeDetail(data)
        if (code != 0) {
          this.$message.error(msg)
          return;
        }
        this.cloudPrintVisible = false
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        console.error('Failed to submit cloud print:', error)
        this.$message.error(error.message || this.$t('printSmallPayFee.submitFailed'))
      }
    },
    handleClose() {
      window.close()
    }
  }
}
</script>

<style scoped>
.printSmallPayFee-container {
  padding: 20px;
}

.print_container {
  padding: 20px;
  background: #fff;
}

.text-center {
  text-align: center;
}

.section2,
.section5 {
  margin-bottom: 15px;
}

.action-buttons {
  margin-top: 20px;
  text-align: right;
}

.float-right {
  float: right;
}

.margin-right {
  margin-right: 10px;
}

.cloud-print-content {
  padding: 20px;
}
</style>