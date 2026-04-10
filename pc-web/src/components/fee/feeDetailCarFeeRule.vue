<template>
  <div class="fee-detail-car-fee-rule">
    <el-row class="toolbar">
      <el-col :span="24" class="text-right">
        <el-button v-if="hasPrivilege('502022073096990007')" type="primary" size="small"
          @click="_openCarCreateFeeAddModal">
          <i class="el-icon-plus"></i>
          {{ $t('feeDetailCarFeeRule.createFee') }}
        </el-button>
        <el-button v-if="hasPrivilege('502022073007100003')" type="primary" size="small"
          @click="_openTempImportRoomFeeModal">
          <i class="el-icon-plus"></i>
          {{ $t('feeDetailCarFeeRule.tempFee') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="feeDetailCarFeeRuleInfo.rules" class="rule-table" style="width: 100%; margin-top: 20px">
      <el-table-column :label="$t('feeDetailCarFeeRule.ruleId')" align="center" width="120">
        <template slot-scope="scope">
          <span class="hand" @click="viewRuleDetail(scope.row)">
            {{ scope.row.ruleId }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailCarFeeRule.feeItem')" align="center">
        <template slot-scope="scope">
          <span class="hand" @click="viewRuleDetail(scope.row)">
            {{ scope.row.feeTypeCdName }}>{{ scope.row.feeName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailCarFeeRule.feeFlag')" prop="feeFlagName" align="center" />
      <el-table-column :label="$t('feeDetailCarFeeRule.createTime')" prop="startTime" align="center" />
      <el-table-column :label="$t('feeDetailCarFeeRule.validity')" align="center" width="200">
        <template slot-scope="scope">
          {{ scope.row.endTime }}<br>~{{ scope.row.maxTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailCarFeeRule.formula')" prop="computingFormulaName" align="center" />
      <el-table-column :label="$t('feeDetailCarFeeRule.unitPrice')" align="center">
        <template slot-scope="scope">
          {{ scope.row.computingFormula === '2002' ? '-' : scope.row.squarePrice }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailCarFeeRule.additionalFee')" prop="additionalAmount" align="center" />
      <el-table-column :label="$t('feeDetailCarFeeRule.status')" align="center" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.state === '2008001'">{{ $t('feeDetailCarFeeRule.valid') }}</span>
          <span v-else>{{ $t('feeDetailCarFeeRule.invalid') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailCarFeeRule.billDate')" prop="curYearMonth" align="center" />
      <el-table-column :label="$t('feeDetailCarFeeRule.billCycle')" align="center">
        <template>{{ $t('feeDetailCarFeeRule.monthly') }}</template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailCarFeeRule.operation')" align="center" width="200">
        <template slot-scope="scope">
          <div v-if="scope.row.state !== '2009001'">
            <el-button v-if="hasPrivilege('502020090427190001')" type="text" size="mini"
              @click="_updateFeeRule(scope.row)">
              {{ $t('feeDetailCarFeeRule.change') }}
            </el-button>
            <el-button v-if="hasPrivilege('502021070488970005')" type="text" size="mini"
              @click="_finishFeeRule(scope.row)">
              {{ $t('feeDetailCarFeeRule.manualFinish') }}
            </el-button>
            <el-button v-if="hasPrivilege('502020090604200029')" type="text" size="mini"
              @click="_deleteFeeRule(scope.row)">
              {{ $t('feeDetailCarFeeRule.cancel') }}
            </el-button>
          </div>
          <div v-else>
            <el-button v-if="hasPrivilege('502020090604200029')" type="text" size="mini"
              @click="_deleteFeeRule(scope.row)">
              {{ $t('feeDetailCarFeeRule.cancel') }}
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
    <car-create-fee-add ref="carCreateFeeAdd" @success="handleSuccess" />

  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listPayFeeRule } from '@/api/fee/carCreateFeeApi'
import DeleteFeeRule from './deleteFeeRule.vue'
import EditFeeRule from './editFeeRule.vue'
import FinishFeeRule from './finishFeeRule.vue'
import TempImportRoomFee from './tempImportRoomFee.vue'
import CarCreateFeeAdd from '@/components/fee/carCreateFeeAdd'

export default {
  name: 'FeeDetailCarFeeRule',
  components: {
    DeleteFeeRule,
    EditFeeRule,
    FinishFeeRule,
    CarCreateFeeAdd,
    TempImportRoomFee
  },

  data() {
    return {
      carId: '',
      carNum: '',
      ownerId: '',
      ownerName: '',
      communityId: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      feeDetailCarFeeRuleInfo: {
        rules: [],
        feeId: '',
        carId: '',
        carNum: '',
        ownerId: '',
        ownerName: ''
      }
    }
  },
  watch: {
    carId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.switchCarFeeRule()
        }
      }
    }
  },
  async created() {
    this.communityId = await getCommunityId()
  },
  methods: {
    open(params) {
      this.feeDetailCarFeeRuleInfo.carId = params.carId
      this.feeDetailCarFeeRuleInfo.carNum = params.carNum
      this.feeDetailCarFeeRuleInfo.ownerId = params.ownerId
      this.feeDetailCarFeeRuleInfo.ownerName = params.ownerName
      this.switchCarFeeRule()
    },
    async switchCarFeeRule() {

      await this._loadFeeDetailCarFeeRuleData(this.currentPage, this.pageSize)
    },

    async _loadFeeDetailCarFeeRuleData(page, row) {
      try {
        const params = {
          communityId: this.communityId,
          feeId: this.feeDetailCarFeeRuleInfo.feeId,
          payerObjId: this.feeDetailCarFeeRuleInfo.carId,
          detailId: '-1',
          page,
          row
        }

        const response = await listPayFeeRule(params)
        this.feeDetailCarFeeRuleInfo.rules = response.data || []
        this.total = response.records || 0
      } catch (error) {
        console.error('获取车辆费用规则失败:', error)
        this.$message.error('获取车辆费用规则失败')
      }
    },

    handlePageChange(page) {
      this.currentPage = page
      this._loadFeeDetailCarFeeRuleData(page, this.pageSize)
    },

    _qureyFeeDetailCarFeeRule() {
      this._loadFeeDetailCarFeeRuleData(this.currentPage, this.pageSize)
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

    _openTempImportRoomFeeModal() {
      this.$refs.tempImportRoomFee.open({
        roomId: this.feeDetailCarFeeRuleInfo.carId,
        roomName: this.feeDetailCarFeeRuleInfo.carNum,
        ownerName: this.feeDetailCarFeeRuleInfo.ownerName,
        objType: '6666'
      })
    },

    _openCarCreateFeeAddModal() {
      this.$refs.carCreateFeeAdd.open({
        carId: this.feeDetailCarFeeRuleInfo.carId,
        carNum: this.feeDetailCarFeeRuleInfo.carNum,
        ownerId: this.feeDetailCarFeeRuleInfo.ownerId,
        ownerName: this.feeDetailCarFeeRuleInfo.ownerName
      })
    },

    viewRuleDetail(rule) {
      // 查看规则详情
      console.log('查看规则详情:', rule)
    },

    handleSuccess() {
      this._loadFeeDetailCarFeeRuleData(this.currentPage, this.pageSize)
    },


  }
}
</script>

<style lang="scss" scoped>
.fee-detail-car-fee-rule {
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