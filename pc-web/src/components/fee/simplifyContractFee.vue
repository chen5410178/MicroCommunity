<template>
  <div class="">
    <el-row class="margin-top">
      <el-col :span="2" class="padding-right-xs padding-left-xl">
        <el-select v-model="simplifyContractFeeInfo.state" @change="handleNotify" size="small"
          :placeholder="$t('simplifyRoomFee.selectStatus')">
          <el-option :label="$t('simplifyRoomFee.valid')" value="2008001"></el-option>
          <el-option :label="$t('simplifyRoomFee.feeEnd')" value="2009001"></el-option>
        </el-select>
      </el-col>
      <el-col :span="22" class="text-right">
        <el-button type="success" size="small" style="margin-left:10px" @click="_openBatchPayFee">
          {{ $t('simplifyContractFee.batchPayFee') }}
        </el-button>
        <el-button type="warning" size="small" style="margin-left:10px" @click="_openPrePayFee">
          {{ $t('simplifyContractFee.prePayFee') }}
        </el-button>
      </el-col>
    </el-row>
    <div class="margin-top">
      <table class="el-table el-table--border el-table--striped" style="margin-top:10px; width:100%">
        <thead>
          <tr>
            <th class="text-center" style="width: 60px;"></th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyContractFee.feeItem') }}</th>
            <th class="text-center">{{ $t('simplifyContractFee.feeFlag') }}</th>
            <th class="text-center">{{ $t('simplifyContractFee.feeType') }}</th>
            <th class="text-center">{{ $t('simplifyContractFee.receivableAmount') }}</th>
            <th class="text-center">{{ $t('simplifyContractFee.receivablePeriod') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyContractFee.description') }}</th>
            <th class="text-center">{{ $t('simplifyContractFee.status') }}</th>
            <th class="text-center" style="width: 150px;">{{ $t('simplifyContractFee.operation') }}</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="(fee, index) in simplifyContractFeeInfo.fees">
            <tr :key="'fee-'+index">
              <td class="text-center" style="color:#999;" @click="switchSimplifyContractFeeFlag(fee)">
                <span class="fa fa-chevron-down" v-if="fee.showFlag == 'Y'"></span>
                <span class="fa fa-chevron-right" v-else></span>
              </td>
              <td class="text-center hand" @click="_viewContractFeeConfig(fee)">
                {{ fee.feeName }}
                <span class="fa fa-info-circle"></span>
              </td>
              <td class="text-center" @click="switchSimplifyContractFeeFlag(fee)">{{ fee.feeFlagName }}</td>
              <td class="text-center" @click="switchSimplifyContractFeeFlag(fee)">{{ fee.feeTypeCdName }}</td>
              <td class="text-center" @click="switchSimplifyContractFeeFlag(fee)">{{ fee.oweAmount }}</td>
              <td class="text-center" @click="switchSimplifyContractFeeFlag(fee)">{{ dateFormat(fee.startTime) }}<br/>~{{ dateFormat(fee.endTime) }}</td>
              <td class="text-center multiline-text">-</td>
              <td class="text-center">-</td>
              <td class="text-center"></td>
            </tr>
            <template v-if="fee.showFlag == 'Y'">
              <tr v-for="(feeSub, subIndex) in fee.childs" :key="'feeSub-'+index+'-'+subIndex" style="background-color:#f9f9f9;">
                <td class="text-center hand"></td>
                <td class="text-center hand">{{ feeSub.feeName }}</td>
                <td class="text-center">{{ feeSub.feeFlagName }}</td>
                <td class="text-center">{{ feeSub.feeTypeCdName }}</td>
                <td class="text-center">{{ feeSub.oweAmount }}</td>
                <td class="text-center">{{ dateFormat(feeSub.startTime) }}<br/>~{{ dateFormat(feeSub.endTime) }}</td>
                <td class="text-center multiline-text">{{ feeSub.remark || '-' }}</td>

                <td class="text-center">{{ feeSub.stateName }}</td>
                <td class="text-center">
                  <el-button class="btn-link" type="text" v-if="feeSub.state != '2009001' && hasPrivilege('502020082314267912')" @click="_payContractFee(feeSub)">{{ $t('simplifyContractFee.payment') }}</el-button>
                  <el-button class="btn-link" type="text" @click="_payContractFeeHis(feeSub)">{{ $t('simplifyContractFee.paymentHistory') }}</el-button>
                  <el-dropdown v-if="hasPrivilege('502020090604200029') || hasPrivilege('502021070488970005') || hasPrivilege('502020090427190001')">
                    <span class="el-dropdown-link">{{ $t('simplifyRoomFee.moreOperations') }}<i class="el-icon-arrow-down el-icon--right"></i></span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-if="hasPrivilege('502020090604200029')" @click.native="_deleteContractFee(feeSub)">{{ $t('simplifyContractFee.cancel') }}</el-dropdown-item>
                      <el-dropdown-item v-if="feeSub.state != '2009001' && hasPrivilege('502020090427190001')" @click.native="_editContractFee(feeSub)">{{ $t('simplifyContractFee.change') }}</el-dropdown-item>
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
      <el-row class="margin-top">
        <el-col :span="4">
          <div>{{ $t('simplifyContractFee.note1') }}</div>
          <div>{{ $t('simplifyContractFee.note2') }}</div>
        </el-col>
        <el-col :span="2">
          <span>{{ $t('simplifyContractFee.arrearsSubtotal') }}:</span>
          {{ simplifyContractFeeInfo.totalAmount }}
        </el-col>
        <el-col :span="6" class="text-right">
          <el-pagination @current-change="handlePageChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" layout="total, prev, pager, next"
            :total="pagination.total"></el-pagination>
        </el-col>
      </el-row>
    </div>
    <temp-import-room-fee ref="tempImportRoomFee"></temp-import-room-fee>
    <delete-fee ref="deleteFee" @success="handleNotify"></delete-fee>
    <edit-fee ref="editFee" @success="handleNotify"></edit-fee>
  </div>
</template>

<script>
import { listFee } from '@/api/contract/contractDetailFeeApi'
import { getCommunityId } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'
import TempImportRoomFee from '@/components/fee/tempImportRoomFee'
import EditFee from '@/components/fee/editFee'
import DeleteFee from '@/components/fee/deleteFee'

export default {
  name: 'SimplifyContractFee',
  components: {
    TempImportRoomFee,
    EditFee,
    DeleteFee
  },
  data() {
    return {
      simplifyContractFeeInfo: {
        fees: [],
        contractId: '',
        contractCode: '',
        contractName: '',
        ownerId: '',
        ownerName: '',
        allOweFeeAmount: '0',
        totalAmount: 0,
        state: '2008001',
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(data) {
      this.simplifyContractFeeInfo.contractId = data.contractId
      this.simplifyContractFeeInfo.contractCode = data.contractCode
      this.simplifyContractFeeInfo.contractName = data.contractName
      this.simplifyContractFeeInfo.ownerId = data.ownerId
      this.simplifyContractFeeInfo.ownerName = data.ownerName
      this._loadSimplifyContractFeeData(1, this.pagination.pageSize)
    },
    async _loadSimplifyContractFeeData(page, row) {
      if (!this.simplifyContractFeeInfo.contractId) return

      try {
        const params = {
          page: page || 1,
          row: row || 10,
          communityId: this.communityId,
          payerObjId: this.simplifyContractFeeInfo.contractId,
          payerObjType: "7777",
          state: this.simplifyContractFeeInfo.state
        }

        const response = await listFee(params)
        this.pagination.total = response.total || 0
        this.simplifyContractFeeInfo.records = response.records || 0

        let totalAmount = 0.0
        response.fees.forEach(item => {
          totalAmount += parseFloat(item.amountOwed || 0)
        })
        this.simplifyContractFeeInfo.totalAmount = totalAmount.toFixed(2)

        const mergedFees = this.simplifyContractFeeMergeFees(response.fees || [])
        this.simplifyContractFeeInfo.fees = mergedFees
        console.log(this.simplifyContractFeeInfo.fees)
      } catch (error) {
        console.error('获取合同费用失败:', error)
        this.$message.error('获取合同费用失败')
      }
    },
    _openTempImportContractFeeModal() {
      this.$refs.tempImportRoomFee.open({
        roomId: this.simplifyContractFeeInfo.contractId,
        objType: '7777',
        roomName: this.simplifyContractFeeInfo.contractName,
        ownerName: this.simplifyContractFeeInfo.ownerName
      })
    },
    _payContractFee(fee) {
      fee.roomName = this.simplifyContractFeeInfo.contractName
      this.$router.push({
        path: '/views/fee/payFeeOrder',
        query: { feeId: fee.feeId }
      })
    },
    _editContractFee(fee) {
      this.$refs.editFee.open(fee)
    },
    _payContractFeeHis(fee) {
      fee.builtUpArea = this.simplifyContractFeeInfo.builtUpArea
      this.$router.push({
        path: '/views/fee/propertyFee',
        query: fee
      })
    },
    _deleteContractFee(fee) {
      this.$refs.deleteFee.open({
        communityId: this.communityId,
        feeId: fee.feeId
      })
    },
    _finishContractFee(fee) {
      this.$emit('finish-fee', {
        communityId: this.communityId,
        feeId: fee.feeId
      })
    },
    _openBatchPayFee() {
      this.$router.push({
        path: '/views/fee/batchPayFeeOrder',
        query: {
          ownerId: this.simplifyContractFeeInfo.ownerId,
          payerObjType: '7777'
        }
      })
    },

    _openPrePayFee() {
      this.$router.push({
        path: '/views/fee/prePayFeeOrder',
        query: {
          payerObjId: this.simplifyContractFeeInfo.contractId,
          payerObjType: '7777',
          payerObjName: this.simplifyContractFeeInfo.contractName,
          ownerName: this.simplifyContractFeeInfo.ownerName
        }
      })
    },
    _openContractCreateFeeAddModal() {
      this.$emit('create-fee-add', {
        isMore: false,
        contract: this.simplifyContractFeeInfo
      })
    },
    _getContractAttrValue(attrs, specCd) {
      let value = ""
      attrs.forEach(item => {
        if (item.specCd == specCd) {
          value = item.value
          return
        }
      })
      return value
    },
    handlePageChange(currentPage) {
      this.pagination.currentPage = currentPage
      this._loadSimplifyContractFeeData(currentPage, this.pagination.pageSize)
    },
    simplifyContractFeeMergeFees(fees) {
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
            deadlineTime: fee.deadlineTime,
            state: fee.state,
            stateName: fee.stateName,
            computingFormula: fee.computingFormula,
            feeTypeCd: fee.feeTypeCd,
            feeFlag: fee.feeFlag,
            preDegrees: fee.preDegrees,
            curDegrees: fee.curDegrees,
            mwPrice: fee.mwPrice,
            squarePrice: fee.squarePrice,
            additionalAmount: fee.additionalAmount,
            feeAttrs: fee.feeAttrs,
            feeId: fee.feeId,
            showFlag: 'N',
            childs: [fee]
          })
        } else {
          const mergedItem = mergedMap.get(key)
          mergedItem.amountOwed = (parseFloat(mergedItem.amountOwed) + parseFloat(fee.amountOwed)).toFixed(4)
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

    switchSimplifyContractFeeFlag(fee) {
      fee.showFlag = fee.showFlag === 'Y' ? 'N' : 'Y'
      this.$forceUpdate()
    },

    _viewContractFeeConfig(fee) {
      this.$emit('viewConfig', fee)
    },

    dateFormat,

    handleNotify() {
      this._loadSimplifyContractFeeData(this.pagination.currentPage, this.pagination.pageSize)
    },

    objToGetParam(obj) {
      return Object.keys(obj).map(key => `${key}=${obj[key]}`).join('&')
    },
    getValidAmount(amount) {
      const num = parseFloat(amount)
      return isNaN(num) ? '0.00' : num.toFixed(2)
    }
  },
  created() {
    this.communityId = getCommunityId()
  }
}
</script>

<style lang="scss" scoped>
.margin-top {
  margin-top: 15px;
}

.margin-top-lg {
  margin-top: 30px;
}

.padding-right-xs {
  padding-right: 5px;
}

.padding-left-xl {
  padding-left: 20px;
}

.text-right {
  text-align: right;
}

.hand {
  cursor: pointer;
}

.text-center {
  text-align: center;
}

.multiline-text {
  white-space: pre-wrap;
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
</style>
