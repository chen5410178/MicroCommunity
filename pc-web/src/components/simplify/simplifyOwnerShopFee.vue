<template>
  <div>
    <el-row class="margin-top-lg">
      <el-col :span="4">
        <el-select v-model="simplifyOwnerShopFeeInfo.shopId" @change="handleShopChange" clearable
          :placeholder="$t('simplifyOwnerShopFee.selectShop')" size="small">
          <el-option v-for="shop in shopList" :key="shop.shopId"
            :label="shop.shopNum + ' - ' + shop.locationName"
            :value="shop.shopId">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="2">
        <el-select v-model="simplifyOwnerShopFeeInfo.feeTypeCd" @change="changeFeeTypeCd" size="small"
          :placeholder="$t('simplifyOwnerShopFee.selectFeeType')">
          <template v-for="item in simplifyOwnerShopFeeInfo.feeTypeCds">
            <el-option :key="item.statusCd" :label="item.name" :value="item.statusCd"
              v-if="item.statusCd != '888800010008'"></el-option>
          </template>
        </el-select>
      </el-col>
      <el-col :span="2">
        <el-select v-model="simplifyOwnerShopFeeInfo.configId" @change="changeConfigId" size="small"
          :placeholder="$t('simplifyOwnerShopFee.selectFeeItem')">
          <el-option v-for="item in simplifyOwnerShopFeeInfo.feeConfigs" :key="item.configId" :label="item.feeName"
            :value="item.configId"></el-option>
        </el-select>
      </el-col>
      <el-col :span="2">
        <el-select v-model="simplifyOwnerShopFeeInfo.state" @change="changeConfigId" size="small"
          :placeholder="$t('simplifyOwnerShopFee.selectStatus')">
          <el-option :label="$t('simplifyOwnerShopFee.valid')" value="2008001"></el-option>
          <el-option :label="$t('simplifyOwnerShopFee.feeEnd')" value="2009001"></el-option>
        </el-select>
      </el-col>
      <el-col :span="14" class="text-right">
        <el-button type="primary" size="small" v-if="hasPrivilege('502020082314267912')"
          @click="openBatchPayShopFeeModal">
          <i class="el-icon-plus"></i>
          {{ $t('simplifyOwnerShopFee.batchPayment') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <table class="el-table el-table--border el-table--striped" style="margin-top:10px; width:100%">
        <thead>
          <tr>
            <th class="text-center" style="width: 60px;"></th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyOwnerShopFee.feeItem') }}</th>
            <th class="text-center">{{ $t('simplifyOwnerShopFee.feeFlag') }}</th>
            <th class="text-center">{{ $t('simplifyOwnerShopFee.feeType') }}</th>
            <th class="text-center">{{ $t('simplifyOwnerShopFee.receivableAmount') }}</th>
            <th class="text-center">{{ $t('simplifyOwnerShopFee.chargeYearMonth') }}</th>
            <th class="text-center">{{ $t('simplifyOwnerShopFee.receivablePeriod') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyOwnerShopFee.description') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyOwnerShopFee.remark') }}</th>
            <th class="text-center">{{ $t('simplifyOwnerShopFee.status') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyOwnerShopFee.operation') }}</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="(fee, index) in simplifyOwnerShopFeeInfo.fees">
            <tr :key="'fee-'+index">
              <td class="text-center" style="color:#999;" @click="switchFeeFlag(fee)">
                <span class="fa fa-chevron-down" v-if="fee.showFlag == 'Y'"></span>
                <span class="fa fa-chevron-right" v-else></span>
              </td>
              <td class="text-center hand" @click="viewShopFeeConfig(fee)">
                {{ fee.feeName }}
                <span class="fa fa-info-circle"></span>
              </td>
              <td class="text-center" @click="switchFeeFlag(fee)">{{ fee.feeFlagName }}</td>
              <td class="text-center" @click="switchFeeFlag(fee)">{{ fee.feeTypeCdName }}</td>
              <td class="text-center" @click="switchFeeFlag(fee)">{{ fee.oweAmount }}</td>
              <td class="text-center" @click="switchFeeFlag(fee)">-</td>
              <td class="text-center" @click="switchFeeFlag(fee)">{{ dateFormat(fee.startTime) }}<br/>~{{ dateFormat(fee.endTime) }}</td>
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
                  <el-button class="btn-link" type="text" v-if="feeSub.state != '2009001' && hasPrivilege('502020082314267912')" @click="payFee(feeSub)">{{ $t('simplifyOwnerShopFee.payment') }}</el-button>
                  <el-button class="btn-link" type="text" @click="payFeeHis(feeSub)">{{ $t('simplifyOwnerShopFee.paymentHistory') }}</el-button>
                </td>
              </tr>
            </template>
          </template>
        </tbody>
      </table>
      <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
        layout="total, prev, pager, next" :total="total" class="pagination text-right"></el-pagination>
    </div>

    <el-row>
      <el-col :span="12" class="text-left table-desc">
        <div>{{ $t('simplifyOwnerShopFee.note1') }}</div>
        <div>{{ $t('simplifyOwnerShopFee.note2') }}</div>
      </el-col>
      <el-col :span="6">
        <span>
          <span>{{ $t('simplifyOwnerShopFee.arrearsSubtotal') }}</span>{{ simplifyOwnerShopFeeInfo.totalAmount }}
        </span>
      </el-col>
    </el-row>
    <delete-fee ref="deleteFee" @success="handleNotify"></delete-fee>
    <edit-fee ref="editFee" @success="handleNotify"></edit-fee>
  </div>
</template>

<script>
import { listShopFees, listFeeConfigs } from '@/api/shop/shopCreateFeeApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { listOwnerShops } from '@/api/shop/shopCreateFeeApi'
import { dateFormat } from '@/utils/dateUtil'
import DeleteFee from '@/components/fee/deleteFee'
import EditFee from '@/components/fee/editFee'

export default {
  name: 'SimplifyOwnerShopFee',
  components: {
    DeleteFee,
    EditFee
  },
  data() {
    return {
      simplifyOwnerShopFeeInfo: {
        shopId: '',
        shopName: '',
        shopNum: '',
        ownerId: '',
        ownerName: '',
        builtUpArea: '',
        feeTypeCd: '',
        configId: '',
        state: '2008001',
        feeTypeCds: [],
        feeConfigs: [],
        totalAmount: 0,
        fees: []
      },
      shopList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadFeeTypeCds()
  },
  methods: {
    loadFeeTypeCds() {
      getDict('pay_fee_config', 'fee_type_cd').then(data => {
        this.simplifyOwnerShopFeeInfo.feeTypeCds = data
      })
    },
    open(params) {
      this.handleSwitch(params)
    },
    handleSwitch(param) {
      this.clearInfo()
      this.simplifyOwnerShopFeeInfo.ownerId = param.ownerId
      this.simplifyOwnerShopFeeInfo.ownerName = param.ownerName
      this.simplifyOwnerShopFeeInfo.shopId = param.shopId || ''
      this.simplifyOwnerShopFeeInfo.shopName = param.shopName || ''
      this.simplifyOwnerShopFeeInfo.shopNum = param.shopNum || ''
      this.simplifyOwnerShopFeeInfo.builtUpArea = param.builtUpArea || ''
      this.loadOwnerShops()
      if (param.shopId) {
        this.listSimplifyShopFee(1, this.pageSize)
      }
    },
    loadOwnerShops() {
      var that = this
      var params = {
        page: 1,
        row: 100,
        communityId: this.communityId,
        ownerId: this.simplifyOwnerShopFeeInfo.ownerId
      }

      listOwnerShops(params).then(function(res) {
        that.shopList = res.data || []
      }).catch(function(error) {
        console.error('Failed to load owner shops:', error)
      })
    },
    handleShopChange(shopId) {
      this.simplifyOwnerShopFeeInfo.configId = ''
      this.$emit('notifyShop', { shopId: shopId })
      if (!shopId) {
        this.simplifyOwnerShopFeeInfo.fees = []
        this.simplifyOwnerShopFeeInfo.totalAmount = 0
        this.total = 0
        return
      }
      var selectedShop = null
      this.shopList.forEach(function(shop) {
        if (shop.shopId === shopId) {
          selectedShop = shop
        }
      })
      if (selectedShop) {
        this.simplifyOwnerShopFeeInfo.shopName = selectedShop.locationName
        this.simplifyOwnerShopFeeInfo.shopNum = selectedShop.shopNum
        this.simplifyOwnerShopFeeInfo.builtUpArea = selectedShop.builtUpArea
      }
      this.listSimplifyShopFee(1, this.pageSize)
    },
    changeFeeTypeCd(feeTypeCd) {
      this.simplifyOwnerShopFeeInfo.configId = ''
      this.$emit('notify', {})

      var params = {
        page: 1,
        row: 50,
        communityId: getCommunityId(),
        feeTypeCd: feeTypeCd,
        isDefault: '',
        valid: '1'
      }

      var that = this
      listFeeConfigs(params).then(function(res) {
        that.simplifyOwnerShopFeeInfo.feeConfigs = res.feeConfigs
      }).catch(function(error) {
        console.error('Failed to fetch fee configs:', error)
      })
    },
    changeConfigId() {
      this.$emit('notify', {})
    },
    switchFeeFlag(fee) {
      fee.showFlag = fee.showFlag === 'Y' ? 'N' : 'Y'
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.listSimplifyShopFee(page, this.pageSize)
    },
    handleNotify() {
      this.listSimplifyShopFee(this.currentPage, this.pageSize)
    },
    async listSimplifyShopFee(page, row) {
      if (!this.simplifyOwnerShopFeeInfo.shopId) {
        return
      }
      var params = {
        page: page,
        row: row,
        communityId: getCommunityId(),
        payerObjId: this.simplifyOwnerShopFeeInfo.shopId,
        payerObjType: '4444',
        configId: this.simplifyOwnerShopFeeInfo.configId,
        state: this.simplifyOwnerShopFeeInfo.state,
        feeTypeCd: this.simplifyOwnerShopFeeInfo.feeTypeCd
      }

      try {
        var res = await listShopFees(params)
        var totalAmount = 0

        res.fees.forEach(function(item) {
          var amount = parseFloat(item.amountOwed || item.oweAmount || 0)
          if (!isNaN(amount)) {
            totalAmount += amount
          }
        })

        this.simplifyOwnerShopFeeInfo.totalAmount = totalAmount.toFixed(2)
        var merged = this.simplifyShopFeeMergeFees(res.fees)
        this.simplifyOwnerShopFeeInfo.fees = merged
        this.total = res.total
      } catch (error) {
        console.error('Failed to fetch fee list:', error)
      }
    },
    simplifyShopFeeMergeFees(fees) {
      var mergedMap = {}
      var that = this
      fees.forEach(function(fee) {
        var key = fee.feeName + '|' + fee.payerObjName
        if (!mergedMap[key]) {
          mergedMap[key] = {
            feeName: fee.feeName,
            configId: fee.configId,
            payerObjName: fee.payerObjName,
            feeFlagName: fee.feeFlagName,
            feeTypeCdName: fee.feeTypeCdName,
            oweAmount: that.getValidAmount(fee.oweAmount || fee.amountOwed),
            startTime: fee.startTime,
            endTime: fee.endTime,
            remark: fee.remark,
            stateName: fee.stateName,
            showFlag: 'N',
            childs: [fee]
          }
        } else {
          var mergedItem = mergedMap[key]
          var currentAmount = parseFloat(mergedItem.oweAmount || 0)
          var newAmount = parseFloat(fee.oweAmount || fee.amountOwed || 0)
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
      var mergedFees = Object.values(mergedMap)
      if (mergedFees.length > 0) {
        mergedFees[0].showFlag = 'Y'
      }
      return mergedFees
    },
    payFee(fee) {
      fee.shopName = this.simplifyOwnerShopFeeInfo.shopName
      fee.builtUpArea = this.simplifyOwnerShopFeeInfo.builtUpArea
      this.$router.push({
        path: '/views/fee/payFeeOrder',
        query: { feeId: fee.feeId }
      })
    },
    editFee(fee) {
      this.$refs.editFee.open(fee)
    },
    payFeeHis(fee) {
      fee.builtUpArea = this.simplifyOwnerShopFeeInfo.builtUpArea
      this.$router.push({
        path: '/views/fee/propertyFee',
        query: fee
      })
    },
    openBatchPayShopFeeModal() {
      this.$router.push({
        path: '/views/fee/batchPayFeeOrder',
        query: {
          payerObjType: '4444',
          payerObjId: this.simplifyOwnerShopFeeInfo.shopId,
          ownerId: this.simplifyOwnerShopFeeInfo.ownerId
        }
      })
    },
    viewShopFeeConfig(fee) {
      var that = this
      var params = {
        page: 1,
        row: 1,
        communityId: getCommunityId(),
        configId: fee.configId
      }

      listFeeConfigs(params).then(function(res) {
        var feeConfig = res.feeConfigs[0]
        that.$emit('openViewDataModal', {
          title: fee.feeName + ' 费用项',
          data: {
            '费用项ID': feeConfig.configId,
            '费用类型': feeConfig.feeTypeCdName,
            '收费项目': feeConfig.feeName,
            '费用标识': feeConfig.feeFlagName,
            '催缴类型': feeConfig.billTypeName,
            '付费类型': feeConfig.paymentCd == '1200' ? '预付费' : '后付费',
            '缴费周期': feeConfig.paymentCycle,
            '应收开始时间': feeConfig.startTime,
            '应收结束时间': feeConfig.endTime,
            '公式': feeConfig.computingFormulaName,
            '计费单价': feeConfig.computingFormula == '2002' ? '-' : feeConfig.squarePrice,
            '附加/固定费用': feeConfig.additionalAmount
          }
        })
      }).catch(function(error) {
        console.error('Failed to fetch fee config:', error)
      })
    },
    clearInfo() {
      this.simplifyOwnerShopFeeInfo = {
        shopId: '',
        shopName: '',
        shopNum: '',
        ownerId: '',
        ownerName: '',
        builtUpArea: '',
        feeTypeCd: '',
        configId: '',
        state: '2008001',
        feeTypeCds: this.simplifyOwnerShopFeeInfo.feeTypeCds,
        feeConfigs: [],
        totalAmount: 0,
        fees: []
      }
      this.shopList = []
      this.currentPage = 1
      this.total = 0
    },
    dateFormat: dateFormat,
    formatRemark(remark) {
      if (!remark || remark === '-') return '-'
      return remark.replace(/\n/g, '<br/>')
    },
    getValidAmount(amount) {
      var num = parseFloat(amount)
      return isNaN(num) ? '0.00' : num.toFixed(2)
    },
    hasPrivilege(privilegeCode) {
      if (this.$store && this.$store.state && this.$store.state.userInfo && this.$store.state.userInfo.privileges) {
        var privileges = this.$store.state.userInfo.privileges
        for (var i = 0; i < privileges.length; i++) {
          if (privileges[i] === privilegeCode) {
            return true
          }
        }
      }
      return false
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

.pagination {
  margin-top: 20px;
}

.table-desc {
  color: #909399;
  font-size: 12px;
}

.table-desc div {
  margin-bottom: 5px;
}
</style>