<template>
  <div class="fee-detail-contract-fee-rule">
    <el-row class="toolbar">
      <el-col :span="24" class="text-right">
        <el-button v-if="hasPrivilege('502022073096990007')" type="primary" size="small"
          @click="_openContractCreateFeeAddModal">
          <i class="el-icon-plus"></i>
          {{ $t('feeDetailContractFeeRule.createFee') }}
        </el-button>
        <el-button v-if="hasPrivilege('502022073007100003')" type="primary" size="small"
          @click="_openTempImportContractFeeModal">
          <i class="el-icon-plus"></i>
          {{ $t('feeDetailContractFeeRule.tempFee') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="feeDetailContractFeeRuleInfo.rules" class="rule-table" style="width: 100%; margin-top: 20px">
      <el-table-column :label="$t('feeDetailContractFeeRule.ruleId')" align="center" width="120">
        <template slot-scope="scope">
          <span class="hand" @click="viewRuleDetail(scope.row)">
            {{ scope.row.ruleId }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailContractFeeRule.feeItem')" align="center">
        <template slot-scope="scope">
          <span class="hand" @click="viewRuleDetail(scope.row)">
            {{ scope.row.feeTypeCdName }}>{{ scope.row.feeName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailContractFeeRule.feeFlag')" prop="feeFlagName" align="center" />
      <el-table-column :label="$t('feeDetailContractFeeRule.createTime')" prop="startTime" align="center" />
      <el-table-column :label="$t('feeDetailContractFeeRule.validity')" align="center" width="200">
        <template slot-scope="scope">
          {{ scope.row.endTime }}<br>~{{ scope.row.maxTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailContractFeeRule.formula')" prop="computingFormulaName" align="center" />
      <el-table-column :label="$t('feeDetailContractFeeRule.unitPrice')" align="center">
        <template slot-scope="scope">
          {{ scope.row.computingFormula === '2002' ? '-' : scope.row.squarePrice }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailContractFeeRule.additionalFee')" prop="additionalAmount" align="center" />
      <el-table-column :label="$t('feeDetailContractFeeRule.status')" align="center" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.state === '2008001'">{{ $t('feeDetailContractFeeRule.valid') }}</span>
          <span v-else>{{ $t('feeDetailContractFeeRule.invalid') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailContractFeeRule.billDate')" prop="curYearMonth" align="center" />
      <el-table-column :label="$t('feeDetailContractFeeRule.billCycle')" align="center">
        <template>{{ $t('feeDetailContractFeeRule.monthly') }}</template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailContractFeeRule.operation')" align="center" width="200">
        <template slot-scope="scope">
          <div v-if="scope.row.state !== '2009001'">
            <el-button v-if="hasPrivilege('502020090427190001')" type="text" size="mini"
              @click="_updateFeeRule(scope.row)">
              {{ $t('feeDetailContractFeeRule.change') }}
            </el-button>
            <el-button v-if="hasPrivilege('502021070488970005')" type="text" size="mini"
              @click="_finishFeeRule(scope.row)">
              {{ $t('feeDetailContractFeeRule.manualFinish') }}
            </el-button>
            <el-button v-if="hasPrivilege('502020090604200029')" type="text" size="mini"
              @click="_deleteFeeRule(scope.row)">
              {{ $t('feeDetailContractFeeRule.cancel') }}
            </el-button>
          </div>
          <div v-else>
            <el-button v-if="hasPrivilege('502020090604200029')" type="text" size="mini"
              @click="_deleteFeeRule(scope.row)">
              {{ $t('feeDetailContractFeeRule.cancel') }}
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page.sync="currentPage" :page-size="pageSize" :total="total"
      layout="total, prev, pager, next" @current-change="handlePageChange"
      style="margin-top: 20px; text-align: right;" />

    <!-- 模态框组件 -->
    <delete-fee-rule ref="deleteFeeRule" @success="handleSuccess" />
    <edit-fee-rule ref="editFeeRule" @success="handleSuccess" />
    <finish-fee-rule ref="finishFeeRule" @success="handleSuccess" />
    <temp-import-room-fee ref="tempImportRoomFee" @success="handleSuccess" />
    <contract-create-fee-add ref="contractCreateFeeAdd" @success="handleSuccess" />

  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listPayFeeRule } from '@/api/fee/carCreateFeeApi'
import DeleteFeeRule from './deleteFeeRule.vue'
import EditFeeRule from './editFeeRule.vue'
import FinishFeeRule from './finishFeeRule.vue'
import TempImportRoomFee from './tempImportRoomFee.vue'
import ContractCreateFeeAdd from '@/components/fee/contractCreateFeeAdd'

export default {
  name: 'FeeDetailContractFeeRule',
  components: {
    DeleteFeeRule,
    EditFeeRule,
    FinishFeeRule,
    ContractCreateFeeAdd,
    TempImportRoomFee
  },

  data() {
    return {
      contractId: '',
      contractCode: '',
      ownerId: '',
      ownerName: '',
      communityId: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      feeDetailContractFeeRuleInfo: {
        rules: [],
        feeId: '',
        contractId: '',
        contractCode: '',
        ownerId: '',
        ownerName: ''
      }
    }
  },
  watch: {
    contractId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.switchContractFeeRule()
        }
      }
    }
  },
  async created() {
    this.communityId = await getCommunityId()
  },
  methods: {
    open(params) {
      this.feeDetailContractFeeRuleInfo.contractId = params.contractId
      this.feeDetailContractFeeRuleInfo.contractCode = params.contractCode
      this.feeDetailContractFeeRuleInfo.ownerId = params.ownerId
      this.feeDetailContractFeeRuleInfo.ownerName = params.ownerName
      this.switchContractFeeRule()
    },
    async switchContractFeeRule() {
      await this._loadFeeDetailContractFeeRuleData(this.currentPage, this.pageSize)
    },

    async _loadFeeDetailContractFeeRuleData(page, row) {
      try {
        const params = {
          communityId: this.communityId,
          feeId: this.feeDetailContractFeeRuleInfo.feeId,
          payerObjId: this.feeDetailContractFeeRuleInfo.contractId,
          detailId: '-1',
          page,
          row
        }

        const response = await listPayFeeRule(params)
        this.feeDetailContractFeeRuleInfo.rules = response.data || []
        this.total = response.records || 0
      } catch (error) {
        console.error('获取合同费用规则失败:', error)
        this.$message.error('获取合同费用规则失败')
      }
    },

    handlePageChange(page) {
      this.currentPage = page
      this._loadFeeDetailContractFeeRuleData(page, this.pageSize)
    },

    _qureyFeeDetailContractFeeRule() {
      this._loadFeeDetailContractFeeRuleData(this.currentPage, this.pageSize)
    },

    _deleteFeeRule(fee) {
      this.$refs.deleteFeeRule.open(fee)
    },

    _updateFeeRule(fee) {
      this.$refs.editFeeRule.open(fee)
    },

    _finishFeeRule(fee) {
      this.$refs.finishFeeRule.open(fee)
    },

    _openTempImportContractFeeModal() {
      this.$refs.tempImportRoomFee.open({
        roomId: this.feeDetailContractFeeRuleInfo.contractId,
        roomName: this.feeDetailContractFeeRuleInfo.contractCode,
        ownerName: this.feeDetailContractFeeRuleInfo.ownerName,
        objType: '7777'
      })
    },

    _openContractCreateFeeAddModal() {
      this.$refs.contractCreateFeeAdd.open({
        contractId: this.feeDetailContractFeeRuleInfo.contractId,
        contractCode: this.feeDetailContractFeeRuleInfo.contractCode,
        ownerId: this.feeDetailContractFeeRuleInfo.ownerId,
        ownerName: this.feeDetailContractFeeRuleInfo.ownerName
      })
    },

    viewRuleDetail(rule) {
      // 查看规则详情
      console.log('查看规则详情:', rule)
    },

    handleSuccess() {
      this._loadFeeDetailContractFeeRuleData(this.currentPage, this.pageSize)
    },


  }
}
</script>

<style lang="scss" scoped>
.fee-detail-contract-fee-rule {
  .toolbar {
    margin-bottom: 20px;
  }

  .rule-table {
    .hand {
      cursor: pointer;
      color: #409eff;

      &:hover {
        text-decoration: underline;
      }
    }
  }

  .text-right {
    text-align: right;
  }
}
</style>
