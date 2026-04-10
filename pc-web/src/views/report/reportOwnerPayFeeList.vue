<template>
  <div class="report-owner-pay-fee-container animated fadeInRight">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('reportOwnerPayFee.queryCondition') }}</span>
            <div style="float: right;">
              <el-button type="text" @click="_moreCondition()">
                {{ reportOwnerPayFeeInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
              </el-button>
            </div>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-select v-model="reportOwnerPayFeeInfo.conditions.feeTypeCd"
                :placeholder="$t('reportOwnerPayFee.selectFeeType')" @change="_changeReporficientFeeTypeCd"
                style="width:100%">
                <el-option v-for="(item, index) in reportOwnerPayFeeInfo.feeTypeCds" :key="index" :label="item.name"
                  :value="item.statusCd">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="reportOwnerPayFeeInfo.conditions.configId"
                :placeholder="$t('reportOwnerPayFee.selectFeeItem')" style="width:100%">
                <el-option v-for="(item, index) in reportOwnerPayFeeInfo.feeConfigDtos" :key="index" :label="item.feeName"
                  :value="item.configId">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model="reportOwnerPayFeeInfo.conditions.roomName"
                :placeholder="$t('reportOwnerPayFee.inputRoomNum')" @input="_meterInputRoom">
                <input-search-room slot="append" ref="inputSearchRoom"></input-search-room>
              </el-input>
            </el-col>
            <el-col :span="4">
              <el-input v-model="reportOwnerPayFeeInfo.conditions.ownerName"
                :placeholder="$t('reportOwnerPayFee.inputOwnerName')" @input="_meterInputOwner">
                <input-search-owner slot="append" ref="inputSearchOwner"></input-search-owner>
              </el-input>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="_queryMethod">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="_resetMethod">
                <i class="el-icon-refresh"></i>
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
          <el-row :gutter="20" v-show="reportOwnerPayFeeInfo.moreCondition">
            <el-col :span="4">
              <el-input v-model="reportOwnerPayFeeInfo.conditions.pfYear"
                :placeholder="$t('reportOwnerPayFee.inputYear')">
              </el-input>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top:20px;">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <div>
              <span>{{ $t('reportOwnerPayFee.paymentDetails') }}</span>
              <el-tooltip class="item" effect="dark" :content="$t('reportOwnerPayFee.paymentDetailsTip')" placement="top">
                <i class="el-icon-info" style="cursor:pointer;"></i>
              </el-tooltip>
            </div>
          </div>
          <el-table :data="reportOwnerPayFeeInfo.ownerPayFees" border style="width: 100%" v-loading="loading">
            <el-table-column prop="ownerName" :label="$t('reportOwnerPayFee.owner')" align="center"></el-table-column>
            <el-table-column prop="roomName" :label="$t('reportOwnerPayFee.room')" align="center"></el-table-column>
            <el-table-column prop="feeName" :label="$t('reportOwnerPayFee.feeItem')" align="center"></el-table-column>
            <el-table-column v-for="month in 12" :key="month" :label="month + $t('reportOwnerPayFee.month')"
              align="center">
              <template slot-scope="scope">
                {{ _getAmountByMonth(scope.row, month) }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('reportOwnerPayFee.total')" align="center">
              <template slot-scope="scope">
                {{ _getTotalAmount(scope.row) }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('reportOwnerPayFee.receivable')" align="center">
              <template slot-scope="scope">
                {{ _getReceivableTotalAmount(scope.row) }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('reportOwnerPayFee.prepayment')" align="center">
              <template slot-scope="scope">
                {{ _getCollectTotalAmount(scope.row) }}
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryReportOwnerPayFee, listFeeConfigs } from '@/api/report/reportOwnerPayFeeApi'
import { getDict } from '@/api/community/communityApi'
import InputSearchRoom from '@/components/report/InputSearchRoom'
import InputSearchOwner from '@/components/report/InputSearchOwner'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ReportOwnerPayFeeList',
  components: {
    InputSearchRoom,
    InputSearchOwner
  },
  data() {
    return {
      loading: false,
      reportOwnerPayFeeInfo: {
        ownerPayFees: [],
        moreCondition: false,
        feeTypeCds: [],
        feeConfigDtos: [],
        conditions: {
          feeTypeCd: '',
          configId: '',
          roomName: '',
          ownerName: '',
          pfYear: '',
          page: 1,
          row: 10,
          communityId: ''
        },
        timer: null
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.reportOwnerPayFeeInfo.conditions.communityId = this.communityId
    this._initData()
  },
  methods: {
    async _initData() {
      try {
        // 获取费用类型字典
        const feeTypeData = await getDict('pay_fee_config', 'fee_type_cd')
        this.reportOwnerPayFeeInfo.feeTypeCds = feeTypeData

        // 获取初始列表数据
        this._listOwnerPayFees()
      } catch (error) {
        console.error('初始化数据失败:', error)
      }
    },
    async _listOwnerPayFees() {
      this.loading = true
      try {
        const params = {
          ...this.reportOwnerPayFeeInfo.conditions,
          page: this.pagination.current,
          row: this.pagination.size
        }

        const { data, total } = await queryReportOwnerPayFee(params)
        this.reportOwnerPayFeeInfo.ownerPayFees = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取业主缴费明细失败:', error)
        this.$message.error(this.$t('reportOwnerPayFee.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _queryMethod() {
      this.pagination.current = 1
      this._listOwnerPayFees()
    },
    _resetMethod() {
      this.reportOwnerPayFeeInfo.conditions = {
        feeTypeCd: '',
        configId: '',
        roomName: '',
        ownerName: '',
        pfYear: '',
        page: 1,
        row: 10,
        communityId: this.communityId
      }
      this.reportOwnerPayFeeInfo.feeConfigDtos = []
      this.pagination.current = 1
      this._listOwnerPayFees()
    },
    async _changeReporficientFeeTypeCd() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: this.communityId,
          feeTypeCd: this.reportOwnerPayFeeInfo.conditions.feeTypeCd,
          isDefault: '',
          feeFlag: '',
          valid: '1'
        }

        const { data } = await listFeeConfigs(params)
        this.reportOwnerPayFeeInfo.feeConfigDtos = data
      } catch (error) {
        console.error('获取收费项失败:', error)
      }
    },
    _meterInputRoom() {
      if (this.reportOwnerPayFeeInfo.timer) {
        clearTimeout(this.reportOwnerPayFeeInfo.timer)
      }
      this.reportOwnerPayFeeInfo.timer = setTimeout(() => {
        this.$refs.inputSearchRoom.open({
          callComponent: 'reportOwnerPayFee',
          roomName: this.reportOwnerPayFeeInfo.conditions.roomName
        })
      }, 1500)
    },
    _meterInputOwner() {
      if (this.reportOwnerPayFeeInfo.timer) {
        clearTimeout(this.reportOwnerPayFeeInfo.timer)
      }
      this.reportOwnerPayFeeInfo.timer = setTimeout(() => {
        this.$refs.inputSearchOwner.open({
          callComponent: 'reportOwnerPayFee',
          ownerTypeCd: '1001',
          ownerName: this.reportOwnerPayFeeInfo.conditions.ownerName
        })
      }, 1500)
    },
    _getAmountByMonth(fee, month) {
      let amount = 0
      if (!fee['reportOwnerPayFeeDtos']) {
        return amount
      }
      const reportOwnerPayFeeDtos = fee.reportOwnerPayFeeDtos
      if (reportOwnerPayFeeDtos.length === 0) {
        return amount
      }
      reportOwnerPayFeeDtos.forEach(item => {
        if (item.pfMonth == month) {
          amount = item.amount
          return
        }
      })
      return amount
    },
    _getTotalAmount(fee) {
      let amount = 0
      if (!fee['reportOwnerPayFeeDtos']) {
        return amount.toFixed(2)
      }
      const reportOwnerPayFeeDtos = fee.reportOwnerPayFeeDtos
      if (reportOwnerPayFeeDtos.length === 0) {
        return amount.toFixed(2)
      }
      reportOwnerPayFeeDtos.forEach(item => {
        amount += parseFloat(item.amount)
      })
      return amount.toFixed(2)
    },
    _getReceivableTotalAmount(fee) {
      let amount = 0
      if (!fee['reportOwnerPayFeeDtos']) {
        return amount.toFixed(2)
      }
      const reportOwnerPayFeeDtos = fee.reportOwnerPayFeeDtos
      if (reportOwnerPayFeeDtos.length === 0) {
        return amount.toFixed(2)
      }
      const now = new Date()
      const month = now.getMonth() + 1
      reportOwnerPayFeeDtos.forEach(item => {
        if (parseInt(item.pfMonth) <= month) {
          amount += parseFloat(item.amount)
        }
      })
      return amount.toFixed(2)
    },
    _getCollectTotalAmount(fee) {
      let amount = 0
      if (!fee['reportOwnerPayFeeDtos']) {
        return amount.toFixed(2)
      }
      const reportOwnerPayFeeDtos = fee.reportOwnerPayFeeDtos
      if (reportOwnerPayFeeDtos.length === 0) {
        return amount.toFixed(2)
      }
      const now = new Date()
      const month = now.getMonth() + 1
      reportOwnerPayFeeDtos.forEach(item => {
        if (parseInt(item.pfMonth) > month) {
          amount += parseFloat(item.amount)
        }
      })
      return amount.toFixed(2)
    },
    _moreCondition() {
      this.reportOwnerPayFeeInfo.moreCondition = !this.reportOwnerPayFeeInfo.moreCondition
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this._listOwnerPayFees()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this._listOwnerPayFees()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-owner-pay-fee-container {
  padding: 20px;
  background-color: #f0f2f5;

  .el-card {
    margin-bottom: 20px;

    .el-card__header {
      padding: 18px 20px;
      border-bottom: 1px solid #ebeef5;
      box-sizing: border-box;
    }
  }

  .el-row {
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .el-col {
    border-radius: 4px;
  }

  .el-pagination {
    margin-top: 15px;
    text-align: right;
  }

  .el-icon-info {
    margin-left: 10px;
    color: #409EFF;
  }
}
</style>