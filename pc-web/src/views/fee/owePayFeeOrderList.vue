<template>
  <div class="owe-pay-fee-order-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ owePayFeeOrderInfo.roomName }}</span>
        <div class="card-header-actions">
          <el-button type="primary" size="small" @click="_printOwnOrder()" v-if="owePayFeeOrderInfo.oweFees.length > 0">
            {{ $t('owePayFeeOrder.printNotice1') }}
          </el-button>
          <el-button type="primary" size="small" @click="_printOwnOrder2()"
            v-if="owePayFeeOrderInfo.oweFees.length > 0">
            {{ $t('owePayFeeOrder.printNotice2') }}
          </el-button>
          <el-button type="primary" size="small" @click="_goBack()">
            <i class="el-icon-close"></i>
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-table ref="oweTable" :data="owePayFeeOrderInfo.oweFees" border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="payerObjName" :label="$t('owePayFeeOrder.payerObj')" align="center"></el-table-column>
        <el-table-column prop="feeTypeCdName" :label="$t('owePayFeeOrder.feeType')" align="center"></el-table-column>
        <el-table-column prop="feeName" :label="$t('owePayFeeOrder.feeItem')" align="center"></el-table-column>
        <el-table-column prop="feeFlagName" :label="$t('owePayFeeOrder.feeFlag')" align="center"></el-table-column>
        <el-table-column prop="feeId" :label="$t('owePayFeeOrder.feeId')" align="center"></el-table-column>
        <el-table-column :label="$t('owePayFeeOrder.startTime')" align="center">
          <template slot-scope="scope">
            {{ _getEndTime(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('owePayFeeOrder.endTime')" align="center">
          <template slot-scope="scope">
            {{ _getDeadlineTime(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column prop="feeTotalPrice" :label="$t('owePayFeeOrder.oweAmount')" align="center"></el-table-column>
      </el-table>

      <el-row class="footer-row" :gutter="10">
        <el-col :span="16"></el-col>
        <el-col :span="4" class="amount-col">
          {{ $t('owePayFeeOrder.paymentAmount') }}：
          <span class="amount-text">{{ owePayFeeOrderInfo.feePrices }}</span>
          <span>{{ $t('owePayFeeOrder.yuan') }}</span>
        </el-col>
        <el-col :span="2">
          <el-button type="success" size="medium" @click="_openPayFee('qrCode')" class="pay-btn">
            {{ $t('owePayFeeOrder.scanPay') }}
          </el-button>
        </el-col>
        <el-col :span="2" >
          <el-button type="primary" size="medium" @click="_payFee()" class="pay-btn">
            {{ $t('owePayFeeOrder.pay') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 缴费确认弹窗 -->
    <el-dialog :title="$t('owePayFeeOrder.paymentConfirm')" :visible.sync="showPayModal"  width="40%">
      <el-form label-width="120px" class="text-left">
        <el-form-item :label="$t('owePayFeeOrder.room')">
          <span>{{ owePayFeeOrderInfo.roomName }}</span>
        </el-form-item>
        <el-form-item :label="$t('owePayFeeOrder.amount')">
          <span>{{ owePayFeeOrderInfo.feePrices }}</span>
          <span>{{ $t('owePayFeeOrder.yuan') }}</span>
        </el-form-item>
        <el-form-item :label="$t('owePayFeeOrder.paymentMethod')" required>
          <el-select v-model="owePayFeeOrderInfo.primeRate" style="width:100%"
            :placeholder="$t('owePayFeeOrder.selectPaymentMethod')">
            <el-option v-for="item in filteredPrimeRates" :key="item.statusCd" :label="item.name"
              :value="item.statusCd">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('owePayFeeOrder.remark')">
          <el-input type="textarea" :placeholder="$t('owePayFeeOrder.inputRemark')" v-model="owePayFeeOrderInfo.remark">
          </el-input>
        </el-form-item>
        <el-form-item v-if="owePayFeeOrderInfo.payType === 'qrCode'" :label="$t('owePayFeeOrder.authCode')">
          <el-input v-model="owePayFeeOrderInfo.authCode" :placeholder="$t('owePayFeeOrder.scanCodeTip')"
            @keyup.enter.native="_qrCodePayFee">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="_closeDoOwePayFeeModal()">{{ $t('common.close') }}</el-button>
        <el-button type="primary" @click="_doPayFee()" v-if="owePayFeeOrderInfo.payType === 'common'">
          {{ $t('owePayFeeOrder.confirmPayment') }}
        </el-button>
      </span>
    </el-dialog>

    <!-- 缴费结果弹窗 -->
    <el-dialog :title="$t('owePayFeeOrder.paymentTip')" :visible.sync="showResultModal" width="30%">
      <p>{{ $t('owePayFeeOrder.paymentSuccess') }}</p>
      <span slot="footer" class="dialog-footer">
        <el-button @click="_back()">{{ $t('common.back') }}</el-button>
        <el-button type="primary" @click="_printAndBack('ON')">
          {{ $t('owePayFeeOrder.mergePrint') }}
        </el-button>
        <el-button type="primary"  @click="_printAndBack('OFF')">
          {{ $t('owePayFeeOrder.printReceipt') }}
        </el-button>
      </span>
    </el-dialog>

    <print-own-fee ref="printOwnFee"></print-own-fee>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'
import {
  listOweFees,
  payOweFee,
  qrCodePayment,
  checkPayFinish,
  listFeePrintPages
} from '@/api/fee/owePayFeeOrderApi'
import PrintOwnFee from '@/components/fee/printOwnFee'

export default {
  name: 'OwePayFeeOrderList',
  components: {
    PrintOwnFee
  },
  data() {
    return {
      owePayFeeOrderInfo: {
        oweFees: [],
        selectPayFeeIds: [],
        feePrices: 0.00,
        communityId: '',
        payObjId: '',
        payObjType: '',
        roomName: '',
        receiptIds: '',
        remark: '',
        primeRate: '',
        primeRates: [],
        toFixedSign: 1,
        payType: 'common',
        authCode: '',
        orderId: '',
        printUrl: '/#/pages/property/printPayFee',
        detailIds: '',
        paymentPoolId: ''
      },
      showPayModal: false,
      showResultModal: false
    }
  },
  computed: {
    filteredPrimeRates() {
      return this.owePayFeeOrderInfo.primeRates.filter(item => {
        if (this.owePayFeeOrderInfo.payType === 'common') {
          return item.statusCd !== '5' && item.statusCd !== '6'
        } else {
          return item.statusCd === '3' || item.statusCd === '4'
        }
      })
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initData()
  },
  methods: {
    async _initData() {
      const _payObjId = this.$route.query.payObjId
      const _payObjType = this.$route.query.payObjType

      if (!_payObjId) {
        this.$message.error(this.$t('owePayFeeOrder.illegalOperation'))
        this.$router.go(-1)
        return
      }

      this.owePayFeeOrderInfo.payObjId = _payObjId
      this.owePayFeeOrderInfo.payObjType = _payObjType
      this.owePayFeeOrderInfo.roomName = this.$route.query.roomName
      this.owePayFeeOrderInfo.communityId = this.communityId

      await this._loadOweFees()
      await this._loadPrimeRates()
      await this._listFeePrintPages()
    },

    handleSelectionChange(val) {
      this.owePayFeeOrderInfo.selectPayFeeIds = val.map(item => item.feeId)
      this._dealSelectFee()

    },

    async _loadOweFees() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: this.communityId,
          payObjId: this.owePayFeeOrderInfo.payObjId,
          payObjType: this.owePayFeeOrderInfo.payObjType,
        }

        const { data } = await listOweFees(params)

        if (data.length < 1) {
          this.owePayFeeOrderInfo.oweFees = []
          this.$message.warning(this.$t('owePayFeeOrder.noOweData'))
          return
        }

        this.owePayFeeOrderInfo.oweFees = data
        this.owePayFeeOrderInfo.selectPayFeeIds = data.map(item => item.feeId)
        this.owePayFeeOrderInfo.toFixedSign = data[0].val

        this.owePayFeeOrderInfo.oweFees.forEach(item => {
          item.receivableAmount = item.feeTotalPrice
          item.feePrice = item.feeTotalPrice
        })

        this._dealSelectFee()
        
        // 等待表格渲染完成后选中所有行
        this.$nextTick(() => {
          this.owePayFeeOrderInfo.oweFees.forEach(row => {
            this.$refs.oweTable.toggleRowSelection(row, true)
          })
        })
      } catch (error) {
        console.error('获取欠费数据失败:', error)
      }
    },

    async _loadPrimeRates() {
      try {
        const data = await getDict('pay_fee_detail', 'prime_rate')
        this.owePayFeeOrderInfo.primeRates = data
      } catch (error) {
        console.error('获取支付方式失败:', error)
      }
    },

    async _listFeePrintPages() {
      try {
        const params = {
          page: 1,
          row: 1,
          state: 'T',
          communityId: this.communityId
        }

        const { data } = await listFeePrintPages(params)
        if (data && data.length > 0) {
          this.owePayFeeOrderInfo.printUrl = data[0].url
        }
      } catch (error) {
        console.error('获取打印页面失败:', error)
      }
    },

    _payFee() {
      if (this.owePayFeeOrderInfo.selectPayFeeIds.length <= 0) {
        this.$message.warning(this.$t('owePayFeeOrder.noSelectedFee'))
        return
      }
      this.showPayModal = true
      this.owePayFeeOrderInfo.payType = 'common'
    },

    _openPayFee() {
      if (this.owePayFeeOrderInfo.selectPayFeeIds.length <= 0) {
        this.$message.warning(this.$t('owePayFeeOrder.noSelectedFee'))
        return
      }
      this.showPayModal = true
      this.owePayFeeOrderInfo.payType = 'qrCode'
    },

    _closeDoOwePayFeeModal() {
      this.showPayModal = false
    },

    _getPayFees() {
      const _fees = []
      this.owePayFeeOrderInfo.selectPayFeeIds.forEach(_item => {
        const feeItem = this.owePayFeeOrderInfo.oweFees.find(_oweFeeItem => _item === _oweFeeItem.feeId)
        if (feeItem) {
          _fees.push({
            feeId: _item,
            startTime: feeItem.endTime,
            endTime: feeItem.deadlineTime,
            receivableAmount: feeItem.receivableAmount,
            receivedAmount: feeItem.feePrice,
            primeRate: this.owePayFeeOrderInfo.primeRate
          })
        }
      })
      return _fees
    },

    async _doPayFee() {
      if (!this.owePayFeeOrderInfo.primeRate) {
        this.$message.warning(this.$t('owePayFeeOrder.selectPaymentMethod'))
        return
      }

      const _fees = this._getPayFees()
      if (_fees.length < 1) {
        this.$message.warning(this.$t('owePayFeeOrder.noSelectedFeeItem'))
        return
      }

      const _data = {
        communityId: this.communityId,
        fees: _fees,
        remark: this.owePayFeeOrderInfo.remark
      }

      try {
        const res = await payOweFee(_data)
        if (res.code === 0) {
          this._doDealPayResult(res)
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('缴费失败:', error)
        this._closeDoOwePayFeeModal()
        this.$message.error(error)
      }
    },

    async _qrCodePayFee() {
      if (!this.owePayFeeOrderInfo.primeRate) {
        this.$message.warning(this.$t('owePayFeeOrder.selectPaymentMethod'))
        return
      }

      const _fees = this._getPayFees()
      if (_fees.length < 1) {
        this.$message.warning(this.$t('owePayFeeOrder.noSelectedFeeItem'))
        return
      }

      const _data = {
        communityId: this.communityId,
        fees: _fees,
        remark: this.owePayFeeOrderInfo.remark,
        authCode: this.owePayFeeOrderInfo.authCode,
        receivedAmount: this.owePayFeeOrderInfo.feePrices,
        subServiceCode: 'fee.payOweFee'
      }

      try {
        const res = await qrCodePayment(_data)
        if (res.code === 404) {
          this.$message.error(res.msg)
          if (res.data && res.data.orderId) {
            this.owePayFeeOrderInfo.orderId = res.data.orderId
            this.owePayFeeOrderInfo.paymentPoolId = res.data.paymentPoolId
            setTimeout(() => {
              this._qrCodeCheckPayFinish()
            }, 5000)
          }
        } else if (res.code === 0) {
          this._doDealPayResult(res)
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('扫码支付失败:', error)
        this.$message.error(error)
      }
    },

    async _qrCodeCheckPayFinish() {
      const _fees = this._getPayFees()
      if (_fees.length < 1) {
        this.$message.warning(this.$t('owePayFeeOrder.noSelectedFeeItem'))
        return
      }

      const _data = {
        communityId: this.communityId,
        fees: _fees,
        remark: this.owePayFeeOrderInfo.remark,
        authCode: this.owePayFeeOrderInfo.authCode,
        receivedAmount: this.owePayFeeOrderInfo.feePrices,
        orderId: this.owePayFeeOrderInfo.orderId,
        paymentPoolId: this.owePayFeeOrderInfo.paymentPoolId,
        subServiceCode: 'fee.payOweFee'
      }

      try {
        const res = await checkPayFinish(_data)
        if (res.code === 404) {
          this.$message.error(res.msg)
        } else if (res.code === 41) {
          setTimeout(() => {
            this._qrCodeCheckPayFinish()
          }, 5000)
        } else {
          this._doDealPayResult(res)
        }
      } catch (error) {
        console.error('检查支付状态失败:', error)
        this.$message.error(error)
      }
    },

    _doDealPayResult(res) {
      this._closeDoOwePayFeeModal()
      const _data = res.data
      let _detailIds = ''
      _data.details.forEach(item => {
        _detailIds += (item + ',')
      })
      this.owePayFeeOrderInfo.detailIds = _detailIds

      setTimeout(() => {
        this.showResultModal = true
      }, 2000)

      this.owePayFeeOrderInfo.selectPayFeeIds = []
      this.$refs.oweTable && this.$refs.oweTable.clearSelection()
      this._loadOweFees()
      this.$message.success(res.msg)
    },

    _back() {
      this.showResultModal = false
      this.$router.go(-1)
    },

    _printAndBack(merge) {
      this.showResultModal = false
      window.open(`${this.owePayFeeOrderInfo.printUrl}?detailIds=${this.owePayFeeOrderInfo.detailIds}&merge=${merge}`)
    },

    _dealSelectFee() {
      let totalFee = 0.00
      this.owePayFeeOrderInfo.selectPayFeeIds.forEach(_item => {
        const feeItem = this.owePayFeeOrderInfo.oweFees.find(_oweFeeItem => _item === _oweFeeItem.feeId)
        if (feeItem) {
          totalFee += parseFloat(feeItem.feePrice)
        }
      })
      this.owePayFeeOrderInfo.feePrices = Math.round(totalFee * 100, 2) / 100
    },

    _goBack() {
      this.$router.go(-1)
    },

    _printOwnOrder() {
      window.open(`/#/views/fee/printOweFee?payObjId=${this.owePayFeeOrderInfo.payObjId}&payObjType=${this.owePayFeeOrderInfo.payObjType}&payObjName=${this.owePayFeeOrderInfo.roomName}`)
    },

    _printOwnOrder2() {
      this.$refs.printOwnFee.open({
        payObjId: this.owePayFeeOrderInfo.payObjId,
        payObjType: this.owePayFeeOrderInfo.payObjType,
        payObjName: this.owePayFeeOrderInfo.roomName
      })
    },

    _getDeadlineTime(fee) {
      if (fee.amountOwed === 0 && fee.endTime === fee.deadlineTime) {
        return "-"
      }
      if (fee.state === '2009001') {
        return "-"
      }
      return dateFormat(fee.deadlineTime)
    },

    _getEndTime(fee) {
      if (fee.state === '2009001') {
        return "-"
      }
      return dateFormat(fee.endTime)
    }
  }
}
</script>

<style lang="scss" scoped>
.owe-pay-fee-order-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .card-header-actions {
      .el-button {
        margin-left: 10px;
      }
    }
  }

  .footer-row {
    margin-top: 20px;
    display: flex;
    align-items: center;

    .amount-col {
      display: flex;
      align-items: center;
      justify-content: flex-end;
      padding-right: 20px;

      .amount-text {
        font-size: 30px;
        color: red;
        padding: 0 5px;
      }
    }

    .pay-btn {
      width: 100%;
    }
  }
}
</style>