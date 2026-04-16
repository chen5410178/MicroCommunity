<template>
  <div class="margin-top">
    <div class="flex justify-end">
      <div :span="2" class="padding-right-xs padding-left-xl"></div>
      <div :span="2" class="padding-right-xs padding-right-xl"></div>
      <div :span="8" class="text-right">

      </div>
    </div>
    <div class="margin-top">
      <el-table :data="contractDetailFeeInfo.fees" style="width: 100%" border stripe>
        <el-table-column prop="feeName" :label="$t('contractDetailFee.feeItem')" align="center"></el-table-column>
        <el-table-column prop="feeFlagName" :label="$t('contractDetailFee.feeFlag')" align="center"></el-table-column>
        <el-table-column prop="feeTypeCdName" :label="$t('contractDetailFee.feeType')" align="center"></el-table-column>
        <el-table-column prop="oweAmount" :label="$t('contractDetailFee.receivableAmount')"
          align="center"></el-table-column>

        <el-table-column :label="$t('contractDetailFee.receivablePeriod')" align="center">
          <template slot-scope="scope">
            {{ dateFormat(scope.row.startTime) }}~<br />
            {{ dateFormat(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('contractDetailFee.description')" align="center" width="150">
          <template slot-scope="scope">
            <div class="text-center multiline-text">{{ scope.row.remark || '-' }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('contractDetailFee.status')" align="center"></el-table-column>
        
      </el-table>
      <el-row class="margin-top">
        <el-col :span="4">
          <div>{{ $t('contractDetailFee.note1') }}</div>
          <div>{{ $t('contractDetailFee.note2') }}</div>
        </el-col>
        <el-col :span="2">
          <span>{{ $t('contractDetailFee.arrearsSubtotal') }}:</span>
          {{ contractDetailFeeInfo.totalAmount }}
        </el-col>
        <el-col :span="6" class="text-right">
          <el-pagination @current-change="handlePageChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" layout="total, prev, pager, next"
            :total="pagination.total"></el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listFee } from '@/api/contract/contractDetailFeeApi'
import { getCommunityId } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'ContractDetailFee',

  data() {
    return {
      contractDetailFeeInfo: {
        rooms: [],
        contractId: '',
        roomNum: '',
        allOweFeeAmount: '0',
        totalAmount: 0,
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
      this.contractDetailFeeInfo.contractId = data.contractId
      this._loadContractDetailFeeData(1, this.pagination.pageSize)
    },
    _loadContractDetailFeeData(page, row) {
      const params = {
        page: page || 1,
        row: row || 10,
        communityId: this.communityId,
        payerObjId: this.contractDetailFeeInfo.contractId,
        state:'2008001'
      }

      listFee(params).then(response => {
        this.contractDetailFeeInfo.total = response.total
        this.contractDetailFeeInfo.records = response.records
        this.contractDetailFeeInfo.fees = response.fees
        this.pagination.total = response.total

        let totalAmount = 0.0
        response.fees.forEach(item => {
          totalAmount += parseFloat(item.oweAmount)
        })
        this.contractDetailFeeInfo.totalAmount = totalAmount.toFixed(2)
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _openTempImportContractFeeModal() {
      this.$refs.tempImportRoomFee.open({
        roomId: this.contractDetailFeeInfo.contractId,
        objType: '7777',
        roomName: this.contractDetailFeeInfo.contractName,
        ownerName: this.contractDetailFeeInfo.ownerName
      })
    },
    _payContractFee(fee) {
      this.$router.push(`/property/payFeeOrder?feeId=${fee.feeId}`)
    },
    _editContractFee(fee) {
      this.$emit('edit-fee', fee)
    },
    _payContractFeeHis(fee) {
      fee.builtUpArea = this.contractDetailFeeInfo.builtUpArea
      this.$router.push(`/property/propertyFee?${this.objToGetParam(fee)}`)
    },
    _deleteContractFee(fee) {
      this.$emit('delete-fee', {
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
    _toContractOwePayFee() {
      this.$router.push(`/property/owePayFeeOrder?payObjId=${this.contractDetailFeeInfo.contractId}&payObjType=7777&contractName=${this.contractDetailFeeInfo.contractName}`)
    },
    _openContractCreateFeeAddModal() {
      this.$emit('create-fee-add', {
        isMore: false,
        contract: this.contractDetailFeeInfo
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
    _getContractDeadlineTime(fee) {
      if (fee.amountOwed == 0 && fee.endTime == fee.deadlineTime) {
        return "-"
      }
      if (fee.state == '2009001') {
        return "-"
      }
      return this.$options.filters.dateFormat(fee.endTime)
    },
    _getContractEndTime(fee) {
      if (fee.state == '2009001') {
        return "-"
      }
      return this.$options.filters.dateFormat(fee.endTime)
    },
    handlePageChange(currentPage) {
      this.pagination.currentPage = currentPage
      this._loadContractDetailFeeData(currentPage, this.pagination.pageSize)
    },
    objToGetParam(obj) {
      return Object.keys(obj).map(key => `${key}=${obj[key]}`).join('&')
    },
    dateFormat
  },
  created() {
    this.communityId = getCommunityId()
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
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
</style>