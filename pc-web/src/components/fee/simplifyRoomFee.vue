<template>
  <div>
    <el-row class="margin-top-lg">
      <el-col :span="2" >
        <el-select v-model="simplifyRoomFeeInfo.feeTypeCd" @change="changeSimplifyRoomFeeFeeTypeCd" size="small"
          :placeholder="$t('simplifyRoomFee.selectFeeType')">
          <template v-for="item in simplifyRoomFeeInfo.feeTypeCds">
            <el-option :key="item.statusCd" :label="item.name" :value="item.statusCd"
              v-if="item.statusCd != '888800010008'"></el-option>
          </template>
        </el-select>
      </el-col>
      <el-col :span="2" >
        <el-select v-model="simplifyRoomFeeInfo.configId" @change="changeSimplifyRoomConfigId" size="small"
          :placeholder="$t('simplifyRoomFee.selectFeeItem')">
          <el-option v-for="item in simplifyRoomFeeInfo.feeConfigs" :key="item.configId" :label="item.feeName"
            :value="item.configId"></el-option>
        </el-select>
      </el-col>
      <el-col :span="2" >
        <el-select v-model="simplifyRoomFeeInfo.state" @change="changeSimplifyRoomConfigId" size="small"
          :placeholder="$t('simplifyRoomFee.selectStatus')">
          <el-option :label="$t('simplifyRoomFee.valid')" value="2008001"></el-option>
          <el-option :label="$t('simplifyRoomFee.feeEnd')" value="2009001"></el-option>
        </el-select>
      </el-col>
      <el-col :span="2" >
        <el-select v-model="simplifyRoomFeeInfo.ownerFee" @change="changeSimplifyRoomConfigId" size="small">
          <el-option :label="$t('simplifyRoomFee.currentRoom')" value="N"></el-option>
          <el-option :label="$t('simplifyRoomFee.currentOwner')" value="Y"></el-option>
        </el-select>
      </el-col>
      <el-col :span="2">
        <el-button type="primary" size="small" @click="handleQuery">查询</el-button>
      </el-col>
      <el-col :span="13" class="text-right">
        <el-button type="primary" size="small" v-if="hasPrivilege('502020082314267912')"
          @click="openBatchPayRoomFeeModal">
          <i class="el-icon-plus"></i>
          {{ $t('simplifyRoomFee.batchPayment') }}
        </el-button>
        <el-button type="primary" size="small" style="margin-left:10px" v-if="hasPrivilege('502020082314267912')"
          @click="toRoomPrePayFeeOrder">
          <i class="el-icon-plus"></i>
          {{ $t('simplifyRoomFee.prePayFee') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <table class="el-table el-table--border el-table--striped" style="margin-top:10px; width:100%">
        <thead>
          <tr>
            <th class="text-center" style="width: 60px;"></th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyRoomFee.feeItem') }}</th>
            <th class="text-center">{{ $t('simplifyRoomFee.feeFlag') }}</th>
            <th class="text-center">{{ $t('simplifyRoomFee.feeType') }}</th>
            <th class="text-center">{{ $t('simplifyRoomFee.receivableAmount') }}</th>
            <th class="text-center">{{ $t('simplifyRoomFee.chargeYearMonth') || '计费年月' }}</th>
            <th class="text-center">{{ $t('simplifyRoomFee.receivablePeriod') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyRoomFee.description') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyRoomFee.remark') || '备注' }}</th>
            <th class="text-center">{{ $t('simplifyRoomFee.status') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyRoomFee.operation') }}</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="(fee, index) in simplifyRoomFeeInfo.fees">
            <tr :key="'fee-'+index">
              <td class="text-center" style="color:#999;" @click="switchSimplifyFeeFlag(fee)">
                <span class="fa fa-chevron-down" v-if="fee.showFlag == 'Y'"></span>
                <span class="fa fa-chevron-right" v-else></span>
              </td>
              <td class="text-center hand" @click="viewRoomFeeConfig(fee)">
                {{ fee.feeName }}{{ getSimplifyRoomFeeRoomName(fee) }}
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
                  <el-button class="btn-link" type="text" v-if="feeSub.state != '2009001' && hasPrivilege('502020082314267912')" @click="payFee(feeSub)">{{ $t('simplifyRoomFee.payment') || '缴费' }}</el-button>
                  <el-button class="btn-link" type="text" @click="payFeeHis(feeSub)">{{ $t('simplifyRoomFee.paymentHistory') || '缴费历史' }}</el-button>
                  <el-dropdown v-if="hasPrivilege('502020090604200029') || hasPrivilege('502021070488970005') || hasPrivilege('502020090427190001')">
                    <span class="el-dropdown-link">{{ $t('simplifyRoomFee.moreOperations') || '更多操作' }}<i class="el-icon-arrow-down el-icon--right"></i></span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-if="hasPrivilege('502020090604200029')" @click.native="deleteFee(feeSub)">{{ $t('simplifyRoomFee.cancelFee') }}</el-dropdown-item>
                      <el-dropdown-item v-if="feeSub.state != '2009001' && hasPrivilege('502020090427190001')" @click.native="editFee(feeSub)">{{ $t('simplifyRoomFee.feeChange') }}</el-dropdown-item>
                      <el-dropdown-item v-if="feeSub.state != '2009001' && hasPrivilege('502020090427190001')" @click.native="splitFee(feeSub)">{{ $t('simplifyRoomFee.splitFee') }}</el-dropdown-item>
                      <el-dropdown-item>
                        <a target="_blank" :href="'/#/views/fee/feeDetail?feeId=' + feeSub.feeId">{{ $t('simplifyRoomFee.details') }}</a>
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
      <el-col :span="12" class="text-left table-desc">
        <div>{{ $t('simplifyRoomFee.note1') }}</div>
        <div>{{ $t('simplifyRoomFee.note2') }}</div>
      </el-col>
      <el-col :span="6">
        <span>
          <span>{{ $t('simplifyRoomFee.arrearsSubtotal') }}</span>{{ simplifyRoomFeeInfo.totalAmount }}
        </span>
      </el-col>
    </el-row>
    <div class="text-right" style="margin-top: 20px">
      <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
        layout="total, prev, pager, next" :total="total"></el-pagination>
    </div>
    <!-- Modal Components -->
    <delete-fee ref="deleteFee" @success="handleNotify"></delete-fee>
    <edit-fee ref="editFee" @success="handleNotify"></edit-fee>
    <finish-fee ref="finishFee" @success="handleNotify"></finish-fee>
    <split-fee ref="splitFee" @success="handleNotify"></split-fee>
  </div>
</template>

<script>
import { listFee, listFeeConfigs } from '@/api/fee/simplifyRoomFeeApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
//import { listFeeConfigs } from '@/api/fee/feeConfigManageApi'
import { dateFormat } from '@/utils/dateUtil'
import DeleteFee from '@/components/fee/deleteFee'
import EditFee from '@/components/fee/editFee'
import FinishFee from '@/components/fee/finishFee'
import SplitFee from '@/components/fee/splitFee'

export default {
  name: 'SimplifyRoomFee',
  components: {
    DeleteFee,
    EditFee,
    FinishFee,
    SplitFee
  },
  data() {
    return {
      simplifyRoomFeeInfo: {
        total: 0,
        records: 1,
        fees: [],
        
        roomId: '',
        roomName: '',
        name: '',
        floorNum: '',
        unitNum: '',
        roomNum: '',
        configId: '',
        feeTypeCds: [],
        feeTypeCd: '',
        state: '2008001',
        totalAmount: 0.0,
        roomType: '',
        ownerFee: 'N',
        ownerId: '',
        
        feeConfigs: [],
        from: ''
      },
      currentPage: 1,
      pageSize: 100,
      total: 0
    }
  },
  computed: {
    filteredFeeTypeCds() {
      return this.simplifyRoomFeeInfo.feeTypeCds.filter(item => item.statusCd !== '888800010008')
    }
  },
  created() {
    this.communityId = getCommunityId()
    //this.loadDictData()
  },
  methods: {
    // 预付收款
    toRoomPrePayFeeOrder() {
      this.$router.push({
        path: '/views/fee/prePayFeeOrder',
        query: {
          payerObjId: this.simplifyRoomFeeInfo.roomId,
          payerObjType: '3333'
        }
      })
    },
    // 父子展开切换
    switchSimplifyFeeFlag(fee) {
      fee.showFlag = fee.showFlag === 'Y' ? 'N' : 'Y'
    },
    // 合并父子费用
    simplifyRoomFeeMergeFees(fees) {
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
      this.clearSimplifyRoomFeeInfo()
      if (param.roomId === '') return

      Object.assign(this.simplifyRoomFeeInfo, param)
      this.listSimplifyRoomFee(1, this.pageSize)

      getDict('pay_fee_config', 'fee_type_cd').then(data => {
        this.simplifyRoomFeeInfo.feeTypeCds = data
      })
    },
    handleQuery() {
      this.currentPage = 1
      this.listSimplifyRoomFee(1, this.pageSize)
    },
    handleNotify() {
      this.listSimplifyRoomFee(this.currentPage, this.pageSize)
    },
    getOnePrice1(fee) {
      let price = fee.mwPrice
      if (!price) return fee.squarePrice
      if (parseFloat(price) > 0) return price
      return fee.squarePrice
    },
    async listSimplifyRoomFee(page, row) {
      const params = {
        page,
        row,
        communityId: getCommunityId(),
        payerObjId: this.simplifyRoomFeeInfo.roomId,
        payerObjType: '3333',
        configId: this.simplifyRoomFeeInfo.configId,
        state: this.simplifyRoomFeeInfo.state,
        feeTypeCd: this.simplifyRoomFeeInfo.feeTypeCd
      }

      if (this.simplifyRoomFeeInfo.ownerFee === 'Y') {
        params.payerObjId = ''
        params.ownerId = this.simplifyRoomFeeInfo.ownerId
      }

      try {
        const res = await listFee(params)
        this.simplifyRoomFeeInfo.total = res.total
        this.simplifyRoomFeeInfo.records = res.records
        let totalAmount = 0.0

        res.fees.forEach(item => {
          const amount = parseFloat(item.amountOwed || item.oweAmount || 0)
          if (!isNaN(amount)) {
            totalAmount += amount
          }
        })

        this.simplifyRoomFeeInfo.totalAmount = totalAmount.toFixed(2)
        // 合并父子结构
        const merged = this.simplifyRoomFeeMergeFees(res.fees)
        this.simplifyRoomFeeInfo.fees = merged
        this.total = res.total
      } catch (error) {
        console.error('Failed to fetch fee list:', error)
      }
    },
    roomFeeCompare(a, b) {
      if (a.payerObjName < b.payerObjName) return -1
      if (a.payerObjName > b.payerObjName) return 1
      return 0
    },
    toOwnerPayFee() {
      this.$router.push({
        path: '/views/fee/owePayFeeOrder',
        query: {
          payObjId: this.simplifyRoomFeeInfo.roomId,
          payObjType: '3333',
          roomName: this.simplifyRoomFeeInfo.roomName
        }
      })
    },
    getAttrValue(attrs, specCd) {
      const attr = attrs.find(item => item.specCd === specCd)
      return attr ? attr.value : ""
    },

    payFee(fee) {
      fee.roomName = this.simplifyRoomFeeInfo.roomName
      fee.builtUpArea = this.simplifyRoomFeeInfo.builtUpArea
      this.$router.push({
        path: '/views/fee/payFeeOrder',
        query: { feeId: fee.feeId }
      })
    },
    editFee(fee) {
      fee.maxEndTime = this.getAttrValue(fee.feeAttrs, '390010')
      this.$refs.editFee.open(fee)
    },
    splitFee(fee) {
      this.$refs.splitFee.open(fee)
    },
    payFeeHis(fee) {
      fee.builtUpArea = this.simplifyRoomFeeInfo.builtUpArea
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
    finishFee(fee) {  
      this.$refs.finishFee.open({
        communityId: getCommunityId(),
        feeId: fee.feeId
      })
    },
    clearSimplifyRoomFeeInfo() {
      const feeConfigs = this.simplifyRoomFeeInfo.feeConfigs
      this.simplifyRoomFeeInfo = {
        total: 0,
        records: 1,
        fees: [],
        monthFees: [],
        accounts: [],
        roomId: '',
        name: '',
        roomName: '',
        floorNum: '',
        unitNum: '',
        roomNum: '',
        feeConfigs: feeConfigs,
        feeTypeCd: '',
        configId: '',
        state: '2008001',
        totalAmount: 0.0,
        roomType: '',
        ownerFee: 'N',
        ownerId: '',
        showFlag: 'DEFAULT'
      }
    },
    changeSimplifyRoomFeeFeeTypeCd(feeTypeCd) {
      this.simplifyRoomFeeInfo.configId = ''
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
        this.simplifyRoomFeeInfo.feeConfigs = res.feeConfigs
      }).catch(error => {
        console.error('Failed to fetch fee configs:', error)
      })
    },
    changeSimplifyRoomConfigId() {
      this.$emit('notify', {})
    },
    simplifyRoomGetFeeOwnerInfo(attrs) {
      const ownerName = this.getAttrValue(attrs, '390008')
      const ownerLink = this.getAttrValue(attrs, '390009')
      return `业主：${ownerName},电话：${ownerLink}`
    },
    getSimplifyRoomFeeRoomName(fee) {
      if (this.simplifyRoomFeeInfo.ownerFee != 'Y') return ''

      const attr = fee.feeAttrs.find(item => item.specCd === '390012')
      return attr ? `(${attr.value})` : ''
    },
    openBatchPayRoomFeeModal() {
      this.$router.push({
        path: '/views/fee/batchPayFeeOrder',
        query: {
          ownerId: this.simplifyRoomFeeInfo.ownerId,
          payerObjType: '3333'
        }
      })
    },
    openMonthPayRoomFeeModal() {
      this.$router.push({
        path: '/property/payFeeMonthOrder',
        query: {
          payerObjId: this.simplifyRoomFeeInfo.roomId,
          payerObjType: '3333'
        }
      })
    },
    openRoomCreateFeeComboModal() {
      this.$router.push({
        path: '/views/fee/createFeeByCombo',
        query: {
          payerObjId: this.simplifyRoomFeeInfo.roomId,
          payerObjName: this.simplifyRoomFeeInfo.roomName,
          payerObjType: '3333'
        }
      })
    },
    viewRoomFeeConfig(fee) {
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
    viewRoomFee(fee) {
      const data = {
        "费用ID": fee.feeId,
        "费用标识": fee.feeFlagName,
        "费用类型": fee.feeTypeCdName,
        "付费对象": fee.payerObjName,
        "费用项": fee.feeName,
        "费用状态": fee.stateName,
        "建账时间": fee.startTime,
        "应收开始时间": this.getEndTime(fee),
        "应收结束时间": this.getDeadlineTime(fee),
        "批次": fee.batchId
      }

      fee.feeAttrs.forEach(attr => {
        data[attr.specCdName] = attr.value
      })

      this.$emit('openViewDataModal', {
        title: `${fee.feeName} 详情`,
        data: data
      })
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.listSimplifyRoomFee(page, this.pageSize)
    },
    dateFormat,
    formatRemark(remark) {
      if (!remark || remark === '-') return '-'
      return remark.replace(/\n/g, '<br/>')
    },
    getValidAmount(amount) {
      const num = parseFloat(amount)
      return isNaN(num) ? '0.00' : num.toFixed(2)
    }
    // 其他方法类似
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

/* table UI 优化：贴近 el-table 风格 */
::v-deep .el-table thead th {
  background-color: #fff !important;     /* 白底表头 */
  color: #606266 !important;            /* Element 默认文字色 */
  font-weight: 500;                      /* 更接近 el-table 的字重 */
  border-bottom: 1px solid #ebeef5 !important;
  height: 48px;                          /* 提高表头高度 */
  line-height: 48px;                     /* 垂直居中 */
}

::v-deep .el-table tbody td {
  border-top: none;                      /* 避免形成双线 */
  border-bottom: 1px solid #ebeef5;      /* 行分隔线与 el-table 接近 */
}

::v-deep .el-table {
  border: 1px solid #ebeef5;             /* 外框单线 */
  border-collapse: collapse;              /* 合并边框，避免双线 */
}

.multiline-text {
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.4;
}
</style>