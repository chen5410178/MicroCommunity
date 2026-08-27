<template>
  <div class="fee-detail-parking-space-fee-rule">
    <el-row class="toolbar">
      <el-col :span="24" class="text-right">
        <el-button v-if="hasPrivilege('502022073096990007')" type="primary" size="small"
          @click="_openParkingSpaceCreateFeeAddModal">
          <i class="el-icon-plus"></i>
          {{ $t('feeDetailParkingSpaceFeeRule.createFee') }}
        </el-button>
        <el-button v-if="hasPrivilege('502022073007100003')" type="primary" size="small"
          @click="_openTempImportRoomFeeModal">
          <i class="el-icon-plus"></i>
          {{ $t('feeDetailParkingSpaceFeeRule.tempFee') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="feeDetailParkingSpaceFeeRuleInfo.rules" class="rule-table" style="width: 100%; margin-top: 20px">
      <el-table-column :label="$t('feeDetailParkingSpaceFeeRule.ruleId')" align="center" width="120">
        <template slot-scope="scope">
          <span class="hand" @click="viewRuleDetail(scope.row)">
            {{ scope.row.ruleId }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailParkingSpaceFeeRule.feeItem')" align="center">
        <template slot-scope="scope">
          <span class="hand" @click="viewRuleDetail(scope.row)">
            {{ scope.row.feeTypeCdName }}>{{ scope.row.feeName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailParkingSpaceFeeRule.feeFlag')" prop="feeFlagName" align="center" />
      <el-table-column :label="$t('feeDetailParkingSpaceFeeRule.createTime')" prop="startTime" align="center" />
      <el-table-column :label="$t('feeDetailParkingSpaceFeeRule.validity')" align="center" width="200">
        <template slot-scope="scope">
          {{ scope.row.endTime }}<br>~{{ scope.row.maxTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailParkingSpaceFeeRule.formula')" prop="computingFormulaName" align="center" />
      <el-table-column :label="$t('feeDetailParkingSpaceFeeRule.unitPrice')" align="center">
        <template slot-scope="scope">
          {{ scope.row.computingFormula === '2002' ? '-' : scope.row.squarePrice }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailParkingSpaceFeeRule.additionalFee')" prop="additionalAmount" align="center" />
      <el-table-column :label="$t('feeDetailParkingSpaceFeeRule.status')" align="center" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.state === '2008001'">{{ $t('feeDetailParkingSpaceFeeRule.valid') }}</span>
          <span v-else>{{ $t('feeDetailParkingSpaceFeeRule.invalid') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailParkingSpaceFeeRule.billDate')" prop="curYearMonth" align="center" />
      <el-table-column :label="$t('feeDetailParkingSpaceFeeRule.billCycle')" align="center">
        <template>{{ $t('feeDetailParkingSpaceFeeRule.monthly') }}</template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailParkingSpaceFeeRule.operation')" align="center" width="200">
        <template slot-scope="scope">
          <div v-if="scope.row.state !== '2009001'">
            <el-button v-if="hasPrivilege('502020090427190001')" type="text" size="mini"
              @click="_updateFeeRule(scope.row)">
              {{ $t('feeDetailParkingSpaceFeeRule.change') }}
            </el-button>
            <el-button v-if="hasPrivilege('502021070488970005')" type="text" size="mini"
              @click="_finishFeeRule(scope.row)">
              {{ $t('feeDetailParkingSpaceFeeRule.manualFinish') }}
            </el-button>
            <el-button v-if="hasPrivilege('502020090604200029')" type="text" size="mini"
              @click="_deleteFeeRule(scope.row)">
              {{ $t('feeDetailParkingSpaceFeeRule.cancel') }}
            </el-button>
          </div>
          <div v-else>
            <el-button v-if="hasPrivilege('502020090604200029')" type="text" size="mini"
              @click="_deleteFeeRule(scope.row)">
              {{ $t('feeDetailParkingSpaceFeeRule.cancel') }}
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
    <parking-space-create-fee-add ref="parkingSpaceCreateFeeAdd" @success="handleSuccess" />

  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listPayFeeRule } from '@/api/fee/parkingSpaceCreateFeeApi'
import DeleteFeeRule from './deleteFeeRule.vue'
import EditFeeRule from './editFeeRule.vue'
import FinishFeeRule from './finishFeeRule.vue'
import TempImportRoomFee from './tempImportRoomFee.vue'
import ParkingSpaceCreateFeeAdd from '@/components/fee/parkingSpaceCreateFeeAdd'

export default {
  name: 'FeeDetailParkingSpaceFeeRule',
  components: {
    DeleteFeeRule,
    EditFeeRule,
    FinishFeeRule,
    ParkingSpaceCreateFeeAdd,
    TempImportRoomFee
  },

  data() {
    return {
      psId: '',
      psName: '',
      ownerId: '',
      ownerName: '',
      communityId: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      feeDetailParkingSpaceFeeRuleInfo: {
        rules: [],
        feeId: '',
        psId: '',
        psName: '',
        ownerId: '',
        ownerName: ''
      }
    }
  },
  watch: {
    psId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.switchParkingSpaceFeeRule()
        }
      }
    }
  },
  async created() {
    this.communityId = await getCommunityId()
  },
  methods: {
    open(params) {
      this.feeDetailParkingSpaceFeeRuleInfo.psId = params.psId
      this.feeDetailParkingSpaceFeeRuleInfo.psName = params.psName
      this.feeDetailParkingSpaceFeeRuleInfo.ownerId = params.ownerId
      this.feeDetailParkingSpaceFeeRuleInfo.ownerName = params.ownerName
      this.switchParkingSpaceFeeRule()
    },
    async switchParkingSpaceFeeRule() {

      await this._loadFeeDetailParkingSpaceFeeRuleData(this.currentPage, this.pageSize)
    },

    async _loadFeeDetailParkingSpaceFeeRuleData(page, row) {
      try {
        const params = {
          communityId: this.communityId,
          feeId: this.feeDetailParkingSpaceFeeRuleInfo.feeId,
          payerObjId: this.feeDetailParkingSpaceFeeRuleInfo.psId,
          detailId: '-1',
          page,
          row
        }

        const response = await listPayFeeRule(params)
        this.feeDetailParkingSpaceFeeRuleInfo.rules = response.data || []
        this.total = response.records || 0
      } catch (error) {
        console.error('获取车辆费用规则失败:', error)
        this.$message.error('获取车辆费用规则失败')
      }
    },

    handlePageChange(page) {
      this.currentPage = page
      this._loadFeeDetailParkingSpaceFeeRuleData(page, this.pageSize)
    },

    _qureyFeeDetailParkingSpaceFeeRule() {
      this._loadFeeDetailParkingSpaceFeeRuleData(this.currentPage, this.pageSize)
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
        roomId: this.feeDetailParkingSpaceFeeRuleInfo.psId,
        roomName: this.feeDetailParkingSpaceFeeRuleInfo.psName,
        ownerName: this.feeDetailParkingSpaceFeeRuleInfo.ownerName,
        objType: '6666'
      })
    },

    _openParkingSpaceCreateFeeAddModal() {
      this.$refs.parkingSpaceCreateFeeAdd.open({
        psId: this.feeDetailParkingSpaceFeeRuleInfo.psId,
        psName: this.feeDetailParkingSpaceFeeRuleInfo.psName,
        ownerId: this.feeDetailParkingSpaceFeeRuleInfo.ownerId,
        ownerName: this.feeDetailParkingSpaceFeeRuleInfo.ownerName
      })
    },

    viewRuleDetail(rule) {
      // 查看规则详情
      console.log('查看规则详情:', rule)
    },

    handleSuccess() {
      this._loadFeeDetailParkingSpaceFeeRuleData(this.currentPage, this.pageSize)
    },


  }
}
</script>

<style lang="scss" scoped>
.fee-detail-parking-space-fee-rule {
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