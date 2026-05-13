<template>
  <div>
    <el-row class="margin-top" v-if="feeDetailFeeRuleInfo.roomId" :gutter="10">
      <el-col :span="4">
        <el-select v-model="feeDetailFeeRuleInfo.state" size="small" placeholder="$t('common.pleaseSelect')" @change="_queryFeeDetailFeeRule">
          <el-option :label="$t('common.pleaseSelect')" value=""></el-option>
          <el-option :label="$t('common.valid')" value="2008001"></el-option>
          <el-option :label="$t('common.invalid')" value="2009001"></el-option>
        </el-select>
      </el-col>
      <el-col :span="20" class="text-right">
        <el-button type="primary" size="small" style="margin-left:10px" v-if="hasPrivilege('502022073096990007')" @click="_openRoomCreateFeeAddModal">
          <i class="fa fa-plus"></i>
          {{ $t('simplifyRoomFee.createFee')}}
        </el-button>
        <el-button type="primary" size="small" style="margin-left:10px" v-if="hasPrivilege('502022073007100003')" @click="_openTempImportRoomFeeModal">
          <i class="fa fa-plus"></i>
          {{ $t('simplifyRoomFee.tempFee')}}
        </el-button>
        <el-button type="primary" size="small" style="margin-left:10px" v-if="hasPrivilege('502022073049300004')" @click="_openProxyFeeModal">
          <i class="fa fa-plus"></i>
          {{ $t('simplifyRoomFee.quantityPayment')  }}
        </el-button>
        <el-button type="primary" size="small" style="margin-left:10px" v-if="hasPrivilege('502022073042960005')" @click="_openAddMeterWaterSimplifyModal">
          <i class="fa fa-plus"></i>
          {{ $t('simplifyRoomFee.waterElectricity')  }}
        </el-button>
      </el-col>
    </el-row>
    <div class="margin-top-sm">
    <el-table :data="feeDetailFeeRuleInfo.rules" border style="width: 100%">
      <el-table-column prop="ruleId" :label="$t('feeDetailFeeRule.ruleId')" align="center"></el-table-column>
      <el-table-column :label="$t('feeDetailFeeRule.feeItem')" align="center">
        <template slot-scope="scope">
          {{ scope.row.feeTypeCdName }}>{{ scope.row.feeName }}
        </template>
      </el-table-column>
      <el-table-column prop="feeFlagName" :label="$t('feeDetailFeeRule.feeFlag')" align="center"></el-table-column>
      <el-table-column prop="startTime" :label="$t('feeDetailFeeRule.createTime')" align="center"></el-table-column>
      <el-table-column :label="$t('feeDetailFeeRule.validPeriod')" align="center">
        <template slot-scope="scope">
          {{ scope.row.endTime }}~{{ scope.row.maxTime }}
        </template>
      </el-table-column>
      <el-table-column prop="computingFormulaName" :label="$t('feeDetailFeeRule.formula')" align="center"></el-table-column>
      <el-table-column :label="$t('feeDetailFeeRule.price')" align="center">
        <template slot-scope="scope">
          {{ scope.row.computingFormula === '2002' ? '-' : scope.row.squarePrice }}
        </template>
      </el-table-column>
      <el-table-column prop="additionalAmount" :label="$t('feeDetailFeeRule.additionalFee')" align="center"></el-table-column>
      <el-table-column :label="$t('feeDetailFeeRule.status')" align="center">
        <template slot-scope="scope">
          {{ scope.row.state === '2008001' ? $t('common.valid') : $t('common.invalid') }}
        </template>
      </el-table-column>
      <el-table-column prop="curYearMonth" :label="$t('feeDetailFeeRule.billDay')" align="center"></el-table-column>
      <el-table-column :label="$t('feeDetailFeeRule.billCycle')" align="center">
        <template>按月</template>
      </el-table-column>
      <el-table-column :label="$t('simplifyRoomFee.remark')" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.feeRemark">
            {{ scope.row.feeRemark }}(<a href="javascript:void(0)" @click="_writeRuleFeeRemark(scope.row)">{{$t('common.remark')}}</a>)
          </span>
          <span v-else>
            {{$t('common.none')}}(<a href="javascript:void(0)" @click="_writeRuleFeeRemark(scope.row)">{{$t('common.remark')}}</a>)
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" v-if="hasPrivilege">
        <template slot-scope="scope">
          <el-button
            v-if="hasPrivilege('502020090427190001')"
            type="text"
            size="small"
            @click="_updateFeeRule(scope.row)"
          >
            {{ $t('common.edit') }}
          </el-button>
          <el-button
            v-if="hasPrivilege('502021070488970005')"
            type="text"
            size="small"
            @click="_finishFeeRule(scope.row)"
          >
            {{ $t('feeDetailCarFeeRule.manualFinish') }}
          </el-button>
          <el-button
            v-if="hasPrivilege('502020090604200029')"
            type="text"
            size="small"
            @click="_deleteFeeRule(scope.row)"
          >
            {{ $t('common.cancel') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    </div>
    
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    ></el-pagination>

    <edit-fee-rule ref="editFeeRule" @success="loadData"></edit-fee-rule>
    <finish-fee-rule ref="finishFeeRule" @success="loadData"></finish-fee-rule>
    <delete-fee-rule ref="deleteFee" @success="loadData"></delete-fee-rule>
    <!-- Newly integrated modals moved from simplifyRoomFee -->
    <temp-import-room-fee ref="tempImportRoomFee" @success="loadData"></temp-import-room-fee>
    <room-create-fee-add ref="roomCreateFeeAdd" @success="loadData"></room-create-fee-add>
    <add-meter-water ref="addMeterWater" @success="loadData"></add-meter-water>
    <add-proxy-fee ref="addProxyFee" @success="loadData"></add-proxy-fee>
  </div>
</template>

<script>
import { listPayFeeRule } from '@/api/fee/feeDetailFeeRuleApi'
import EditFeeRule from './editFeeRule'
import FinishFeeRule from './finishFeeRule'
import DeleteFeeRule from './deleteFeeRule'
import TempImportRoomFee from './tempImportRoomFee'
import RoomCreateFeeAdd from './roomCreateFeeAdd'
import AddMeterWater from './addMeterWater'
import AddProxyFee from './addProxyFee'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeDetailFeeRule',
  components: {
    EditFeeRule,
    FinishFeeRule,
    DeleteFeeRule,
    TempImportRoomFee,
    RoomCreateFeeAdd,
    AddMeterWater,
    AddProxyFee
  },
  props: {
    feeId: {
      type: String,
      default: ''
    },
    roomId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      feeDetailFeeRuleInfo: {
        rules: [],
        feeId: '',
        roomId: '',
        roomName: '',
        ownerName: '',
        state: '2008001'
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  computed: {
  },
  methods: {
    async loadData() {
      const params = {
        communityId: getCommunityId(),
        feeId: this.feeDetailFeeRuleInfo.feeId,
        payerObjId: this.feeDetailFeeRuleInfo.roomId,
        detailId: '-1',
        state: this.feeDetailFeeRuleInfo.state,
        page: this.currentPage,
        row: this.pageSize
      }

      try {
        const res = await listPayFeeRule(params)
        if (res.code === 0) {
          this.feeDetailFeeRuleInfo.rules = res.data
          this.total = res.total
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _queryFeeDetailFeeRule() {
      this.currentPage = 1
      this.loadData()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadData()
    },
    _deleteFeeRule(fee) {
      this.$refs.deleteFee.open(fee)
    },
    _updateFeeRule(fee) {
      this.$refs.editFeeRule.open(fee)
    },
    _finishFeeRule(fee) {
      this.$refs.finishFeeRule.open(fee)
    },
    _writeRuleFeeRemark(fee) {
      const ruleId = fee && fee.ruleId ? fee.ruleId : ''
      if (!ruleId) return
      this.$router.push({ path: '/views/fee/wirteRuleFeeRemark', query: { ruleId } })
    },
    _openRoomCreateFeeAddModal() {
      this.$refs.roomCreateFeeAdd.open({
        isMore: false,
        roomId: this.feeDetailFeeRuleInfo.roomId,
        roomName: this.feeDetailFeeRuleInfo.roomName,
        ownerName: this.feeDetailFeeRuleInfo.ownerName
      })
    },
    _openTempImportRoomFeeModal() {
      this.$refs.tempImportRoomFee.open({
        roomId: this.feeDetailFeeRuleInfo.roomId,
        roomName: this.feeDetailFeeRuleInfo.roomName,
        ownerName: this.feeDetailFeeRuleInfo.ownerName
      })
    },
    _openProxyFeeModal() {
      this.$refs.addProxyFee.open({
        roomId: this.feeDetailFeeRuleInfo.roomId,
        roomName: this.feeDetailFeeRuleInfo.roomName,
        ownerName: this.feeDetailFeeRuleInfo.ownerName
      })
    },
    _openAddMeterWaterSimplifyModal() {
      this.$refs.addMeterWater.open({
        roomId: this.feeDetailFeeRuleInfo.roomId,
        roomName: this.feeDetailFeeRuleInfo.roomName,
        ownerName: this.feeDetailFeeRuleInfo.ownerName
      })
    },
    open(params) {
      // 这个方法用于接收参数并初始化组件
      if (params.feeId) {
        this.feeDetailFeeRuleInfo.feeId = params.feeId
      }
      if (params.roomId) {
        this.feeDetailFeeRuleInfo.roomId = params.roomId
      }
      if (params.roomName) {
        this.feeDetailFeeRuleInfo.roomName = params.roomName
      }
      if (params.ownerName) {
        this.feeDetailFeeRuleInfo.ownerName = params.ownerName
      }
      this.loadData()
    }
  },
  watch: {
    feeId(newVal) {
      this.feeDetailFeeRuleInfo.feeId = newVal
      this.loadData()
    },
    roomId(newVal) {
      this.feeDetailFeeRuleInfo.roomId = newVal
      this.loadData()
    }
  }
}
</script>