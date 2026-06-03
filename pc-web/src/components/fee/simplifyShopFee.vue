<template>
  <div>
    <el-row class="margin-top">
      <el-col :span="2">
        <el-select v-model="simplifyShopFeeInfo.feeTypeCd" @change="changeSimplifyShopFeeFeeTypeCd" size="small"
          :placeholder="$t('simplifyShopFee.selectFeeType')">
          <template v-for="item in simplifyShopFeeInfo.feeTypeCds">
            <el-option :key="item.statusCd" :label="item.name" :value="item.statusCd"
              v-if="item.statusCd != '888800010008'"></el-option>
          </template>
        </el-select>
      </el-col>
      <el-col :span="2">
        <el-select v-model="simplifyShopFeeInfo.configId" @change="changeSimplifyShopConfigId" size="small"
          :placeholder="$t('simplifyShopFee.selectFeeItem')">
          <el-option v-for="item in simplifyShopFeeInfo.feeConfigs" :key="item.configId" :label="item.feeName"
            :value="item.configId"></el-option>
        </el-select>
      </el-col>
      <el-col :span="2">
        <el-select v-model="simplifyShopFeeInfo.state" @change="changeSimplifyShopConfigId" size="small"
          :placeholder="$t('simplifyShopFee.selectStatus')">
          <el-option :label="$t('simplifyShopFee.valid')" value="2008001"></el-option>
          <el-option :label="$t('simplifyShopFee.feeEnd')" value="2009001"></el-option>
        </el-select>
      </el-col>
      <el-col :span="17" class="text-right">
        <el-button type="primary" size="small" v-if="hasPrivilege('502020082314267912')"
          @click="openBatchPayShopFeeModal">
          <i class="el-icon-plus"></i>
          {{ $t('simplifyShopFee.batchPayment') }}
        </el-button>
        <el-button type="primary" size="small" style="margin-left:10px" v-if="hasPrivilege('502020080570210194')"
          @click="toShopPrePayFeeOrder">
          <i class="el-icon-plus"></i>
          {{ $t('simplifyShopFee.prePayFee') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <table class="el-table el-table--border el-table--striped" style="margin-top:10px; width:100%">
        <thead>
          <tr>
            <th class="text-center" style="width: 60px;"></th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyShopFee.feeItem') }}</th>
            <th class="text-center">{{ $t('simplifyShopFee.feeFlag') }}</th>
            <th class="text-center">{{ $t('simplifyShopFee.feeType') }}</th>
            <th class="text-center">{{ $t('simplifyShopFee.receivableAmount') }}</th>
            <th class="text-center">{{ $t('simplifyShopFee.chargeYearMonth') }}</th>
            <th class="text-center">{{ $t('simplifyShopFee.receivablePeriod') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyShopFee.description') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyShopFee.remark') }}</th>
            <th class="text-center">{{ $t('simplifyShopFee.status') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyShopFee.operation') }}</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="(fee, index) in simplifyShopFeeInfo.fees">
            <tr :key="'fee-'+index">
              <td class="text-center" style="color:#999;" @click="switchSimplifyFeeFlag(fee)">
                <span class="fa fa-chevron-down" v-if="fee.showFlag == 'Y'"></span>
                <span class="fa fa-chevron-right" v-else></span>
              </td>
              <td class="text-center hand" @click="viewShopFeeConfig(fee)">
                {{ fee.feeName }}
                <span class="fa fa-info-circle"></span>
              </td>
              <td class="text-center" @click="switchSimplifyFeeFlag(fee)">{{ fee.feeFlagName }}</td>
              <td class="text-center" @click="switchSimplifyFeeFlag(fee)">{{ fee.feeTypeCdName }}</td>
              <td class="text-center" @click="switchSimplifyFeeFlag(fee)">{{ fee.oweAmount }}</td>
              <td class="text-center" @click="switchSimplifyFeeFlag(fee)">-</td>
              <td class="text-center" @click="switchSimplifyFeeFlag(fee)">{{ dateFormat(fee.startTime) }}<br/>~{{ dateFormat(fee.endTime) }}</td>
              <td class="text-center multiline-text">-</td>
              <td class="text-center">-</td>
              <td class="text-center"></td>
              <td class="text-center"></td>
            </tr>
            <template v-if="fee.showFlag == 'Y'">
              <tr v-for="(feeSub, subIndex) in fee.childs" :key="'feeSub-'+index+'-'+subIndex" style="background-color:#f9f9f9;">
                <td class="text-center hand"></td>
                <td class="text-center hand">{{ feeSub.feeName }}</td>
                <td class="text-center">{{ feeSub.feeFlagName }}</td>
                <td class="text-center">{{ feeSub.feeTypeCdName }}</td>
                <td class="text-center">{{ feeSub.oweAmount || feeSub.amountOwed }}</td>
                <td class="text-center">{{ feeSub.yearMonth || (feeSub.detailYear && feeSub.detailMonth ? (feeSub.detailYear + '-' + feeSub.detailMonth) : '-') }}</td>
                <td class="text-center">{{ dateFormat(feeSub.startTime) }}<br/>~{{ dateFormat(feeSub.endTime) }}</td>
                <td class="text-center multiline-text" v-html="formatRemark(feeSub.remark)"></td>
                <td class="text-center multiline-text">-</td>
                <td class="text-center">{{ feeSub.stateName }}</td>
                <td class="text-center">
                  <el-button class="btn-link" type="text" v-if="feeSub.state != '2009001' && hasPrivilege('502020082314267912')" @click="payFee(feeSub)">{{ $t('simplifyShopFee.payment') }}</el-button>
                  <el-button class="btn-link" type="text" @click="payFeeHis(feeSub)">{{ $t('simplifyShopFee.paymentHistory') }}</el-button>
                  <el-dropdown v-if="hasPrivilege('502020090604200029') || hasPrivilege('502021070488970005') || hasPrivilege('502020090427190001')">
                    <span class="el-dropdown-link">{{ $t('simplifyShopFee.moreOperations') }}<i class="el-icon-arrow-down el-icon--right"></i></span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-if="hasPrivilege('502020090604200029')" @click.native="deleteFee(feeSub)">{{ $t('simplifyShopFee.cancelFee') }}</el-dropdown-item>
                      <el-dropdown-item v-if="feeSub.state != '2009001' && hasPrivilege('502020090427190001')" @click.native="editFee(feeSub)">{{ $t('simplifyShopFee.feeChange') }}</el-dropdown-item>
                      <el-dropdown-item v-if="feeSub.state != '2009001' && hasPrivilege('502020090427190001')" @click.native="splitFee(feeSub)">{{ $t('simplifyShopFee.splitFee') }}</el-dropdown-item>
                      <el-dropdown-item>
                        <a target="_blank" :href="'/#/views/fee/feeDetail?feeId=' + feeSub.feeId">{{ $t('simplifyShopFee.details') }}</a>
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </td>
              </tr>
            </template>
          </template>
        </tbody>
      </table>
    </div>

    <el-row class="margin-top">
      <el-col :span="8" class="text-left table-desc">
        <div>{{ $t('simplifyShopFee.note1') }}</div>
        <div>{{ $t('simplifyShopFee.note2') }}</div>
      </el-col>
      <el-col :span="6">
        <span>
          <span>{{ $t('simplifyShopFee.arrearsSubtotal') }}</span>{{ simplifyShopFeeInfo.totalAmount }}
        </span>
      </el-col>
      <el-col :span="8" class="text-right">
         <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
        layout="total, prev, pager, next" :total="total"></el-pagination>
      </el-col>
    </el-row>
    <delete-fee ref="deleteFee" @success="handleNotify"></delete-fee>
    <edit-fee ref="editFee" @success="handleNotify"></edit-fee>
    <finish-fee ref="finishFee" @success="handleNotify"></finish-fee>
    <split-fee ref="splitFee" @success="handleNotify"></split-fee>
  </div>
</template>

<script>
import { listShopFees, listFeeConfigs } from '@/api/shop/shopCreateFeeApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'
import DeleteFee from '@/components/fee/deleteFee'
import EditFee from '@/components/fee/editFee'
import FinishFee from '@/components/fee/finishFee'
import SplitFee from '@/components/fee/splitFee'

export default {
  name: 'SimplifyShopFee',
  components: {
    DeleteFee,
    EditFee,
    FinishFee,
    SplitFee
  },
  data() {
    return {
      simplifyShopFeeInfo: {
        total: 0,
        records: 1,
        fees: [],
        shopId: '',
        shopName: '',
        shopNum: '',
        configId: '',
        feeTypeCds: [],
        feeTypeCd: '',
        state: '2008001',
        totalAmount: 0.0,
        feeConfigs: [],
        ownerId: ''
      },
      currentPage: 1,
      pageSize: 100,
      total: 0
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    toShopPrePayFeeOrder() {
      this.$router.push({
        path: '/views/fee/prePayFeeOrder',
        query: {
          payerObjId: this.simplifyShopFeeInfo.shopId,
          payerObjType: '4444'
        }
      })
    },
    switchSimplifyFeeFlag(fee) {
      fee.showFlag = fee.showFlag === 'Y' ? 'N' : 'Y'
    },
    simplifyShopFeeMergeFees(fees) {
      const mergedMap = new Map()
      fees.forEach(fee => {
        const key = `${fee.feeName}|${fee.payerObjName}`
        if (!mergedMap.has(key)) {
          mergedMap.set(key, {
            feeName: fee.feeName,
            configId: fee.configId,
            payerObjName: fee.payerObjName,
            feeFlagName: fee.feeFlagName,
            feeTypeCdName: fee.feeTypeCdName,
            oweAmount: this.getValidAmount(fee.oweAmount || fee.amountOwed),
            startTime: fee.startTime,
            endTime: fee.endTime,
            remark: fee.remark,
            stateName: fee.stateName,
            showFlag: 'N',
            childs: [fee]
          })
        } else {
          const mergedItem = mergedMap.get(key)
          const currentAmount = parseFloat(mergedItem.oweAmount || 0)
          const newAmount = parseFloat(fee.oweAmount || fee.amountOwed || 0)
          if (!isNaN(currentAmount) && !isNaN(newAmount)) {
            mergedItem.oweAmount = (currentAmount + newAmount).toFixed(4)
          } else if (!isNaN(newAmount)) {
            mergedItem.oweAmount = newAmount.toFixed(4)
          }
          if (new Date(fee.startTime) < new Date(mergedItem.startTime)) {
            mergedItem.startTime = fee.startTime
          }
          if (new Date(fee.endTime) > new Date(mergedItem.endTime)) {
            mergedItem.endTime = fee.endTime
          }
          mergedItem.childs.push(fee)
        }
      })
      const mergedFees = Array.from(mergedMap.values())
      if (mergedFees.length > 0) {
        mergedFees[0].showFlag = 'Y'
      }
      return mergedFees
    },
    open(params) {
      this.handleSwitch(params)
    },
    handleSwitch(param) {
      this.clearSimplifyShopFeeInfo()
      if (!param.shopId) {
        return
      }
      Object.assign(this.simplifyShopFeeInfo, param)
      this.listSimplifyShopFee(1, this.pageSize)

      getDict('pay_fee_config', 'fee_type_cd').then(data => {
        this.simplifyShopFeeInfo.feeTypeCds = data
      })
    },
    handleNotify() {
      this.listSimplifyShopFee(this.currentPage, this.pageSize)
    },
    async listSimplifyShopFee(page, row) {
      const params = {
        page,
        row,
        communityId: getCommunityId(),
        payerObjId: this.simplifyShopFeeInfo.shopId,
        payerObjType: '4444',
        configId: this.simplifyShopFeeInfo.configId,
        state: this.simplifyShopFeeInfo.state,
        feeTypeCd: this.simplifyShopFeeInfo.feeTypeCd
      }

      try {
        const res = await listShopFees(params)
        this.simplifyShopFeeInfo.total = res.total
        this.simplifyShopFeeInfo.records = res.records
        let totalAmount = 0.0

        res.fees.forEach(item => {
          const amount = parseFloat(item.amountOwed || item.oweAmount || 0)
          if (!isNaN(amount)) {
            totalAmount += amount
          }
        })

        this.simplifyShopFeeInfo.totalAmount = totalAmount.toFixed(2)
        const merged = this.simplifyShopFeeMergeFees(res.fees)
        this.simplifyShopFeeInfo.fees = merged
        this.total = res.total
      } catch (error) {
        console.error('Failed to fetch fee list:', error)
      }
    },
    payFee(fee) {
      fee.shopName = this.simplifyShopFeeInfo.shopName
      fee.builtUpArea = this.simplifyShopFeeInfo.builtUpArea
      this.$router.push({
        path: '/views/fee/payFeeOrder',
        query: { feeId: fee.feeId }
      })
    },
    editFee(fee) {
      this.$refs.editFee.open(fee)
    },
    splitFee(fee) {
      this.$refs.splitFee.open(fee)
    },
    payFeeHis(fee) {
      fee.builtUpArea = this.simplifyShopFeeInfo.builtUpArea
      this.$router.push({
        path: '/views/fee/propertyFee',
        query: fee
      })
    },
    deleteFee(fee) {
      this.$refs.deleteFee.open({
        communityId: getCommunityId(),
        feeId: fee.feeId
      })
    },
    clearSimplifyShopFeeInfo() {
      const feeConfigs = this.simplifyShopFeeInfo.feeConfigs
      this.simplifyShopFeeInfo = {
        total: 0,
        records: 1,
        fees: [],
        shopId: '',
        shopName: '',
        shopNum: '',
        feeConfigs: feeConfigs,
        feeTypeCd: '',
        configId: '',
        state: '2008001',
        totalAmount: 0.0,
        ownerId: '',
        showFlag: 'DEFAULT'
      }
    },
    changeSimplifyShopFeeFeeTypeCd(feeTypeCd) {
      this.simplifyShopFeeInfo.configId = ''
      this.$emit('notify', {})

      const params = {
        page: 1,
        row: 50,
        communityId: getCommunityId(),
        feeTypeCd: feeTypeCd,
        isDefault: '',
        valid: '1'
      }

      listFeeConfigs(params).then(res => {
        this.simplifyShopFeeInfo.feeConfigs = res.feeConfigs
      }).catch(error => {
        console.error('Failed to fetch fee configs:', error)
      })
    },
    changeSimplifyShopConfigId() {
      this.$emit('notify', {})
    },
    viewShopFeeConfig(fee) {
      const params = {
        page: 1,
        row: 1,
        communityId: getCommunityId(),
        configId: fee.configId
      }

      listFeeConfigs(params).then(res => {
        const feeConfig = res.feeConfigs[0]
        this.$emit('openViewDataModal', {
          title: `${fee.feeName} 费用项`,
          data: {
            "费用项ID": feeConfig.configId,
            "费用类型": feeConfig.feeTypeCdName,
            "收费项目": feeConfig.feeName,
            "费用标识": feeConfig.feeFlagName,
            "催缴类型": feeConfig.billTypeName,
            "付费类型": feeConfig.paymentCd == '1200' ? '预付费' : '后付费',
            "缴费周期": feeConfig.paymentCycle,
            "应收开始时间": feeConfig.startTime,
            "应收结束时间": feeConfig.endTime,
            "公式": feeConfig.computingFormulaName,
            "计费单价": feeConfig.computingFormula == '2002' ? '-' : feeConfig.squarePrice,
            "附加/固定费用": feeConfig.additionalAmount
          }
        })
      }).catch(error => {
        console.error('Failed to fetch fee config:', error)
      })
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.listSimplifyShopFee(page, this.pageSize)
    },
    dateFormat,
    formatRemark(remark) {
      if (!remark || remark === '-') return '-'
      return remark.replace(/\n/g, '<br/>')
    },
    getValidAmount(amount) {
      const num = parseFloat(amount)
      return isNaN(num) ? '0.00' : num.toFixed(2)
    },
    openBatchPayShopFeeModal() {
      this.$router.push({
        path: '/views/fee/batchPayFeeOrder',
        query: {
          payerObjType: '4444',
          payerObjId: this.simplifyShopFeeInfo.shopId,
          ownerId: this.simplifyShopFeeInfo.ownerId
        }
      })
    }
  }
}
</script>

<style scoped>
.margin-top-lg {
  margin-top: 20px;
}

.margin-top {
  margin-top: 15px;
}

.text-right {
  text-align: right;
}

.hand {
  cursor: pointer;
}

::v-deep .el-table thead th {
  background-color: #fff !important;
  color: #606266 !important;
  font-weight: 500;
  border-bottom: 1px solid #ebeef5 !important;
  height: 48px;
  line-height: 48px;
}

::v-deep .el-table tbody td {
  border-top: none;
  border-bottom: 1px solid #ebeef5;
}

::v-deep .el-table {
  border: 1px solid #ebeef5;
  border-collapse: collapse;
}

.multiline-text {
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.4;
}
</style>
