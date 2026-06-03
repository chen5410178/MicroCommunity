<template>
  <div class="fee-detail-shop-fee-rule">
    <el-row class="toolbar">
      <el-col :span="24" class="text-right">
        <el-button v-if="hasPrivilege('502022073096990007')" type="primary" size="small"
          @click="_openShopCreateFeeAddModal">
          <i class="el-icon-plus"></i>
          {{ $t('feeDetailShopFeeRule.createFee') }}
        </el-button>
        <el-button v-if="hasPrivilege('502022073007100003')" type="primary" size="small"
          @click="_openTempImportRoomFeeModal">
          <i class="el-icon-plus"></i>
          {{ $t('feeDetailShopFeeRule.tempFee') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="feeDetailShopFeeRuleInfo.rules" class="rule-table" border stripe style="width: 100%; margin-top: 20px">
      <el-table-column :label="$t('feeDetailShopFeeRule.ruleId')" align="center" width="120">
        <template slot-scope="scope">
          <span class="hand" @click="viewRuleDetail(scope.row)">
            {{ scope.row.ruleId }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailShopFeeRule.feeItem')" align="center">
        <template slot-scope="scope">
          <span class="hand" @click="viewRuleDetail(scope.row)">
            {{ scope.row.feeTypeCdName }}>{{ scope.row.feeName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailShopFeeRule.feeFlag')" prop="feeFlagName" align="center" />
      <el-table-column :label="$t('feeDetailShopFeeRule.createTime')" prop="startTime" align="center" />
      <el-table-column :label="$t('feeDetailShopFeeRule.validity')" align="center" width="200">
        <template slot-scope="scope">
          {{ scope.row.endTime }}<br>~{{ scope.row.maxTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailShopFeeRule.formula')" prop="computingFormulaName" align="center" />
      <el-table-column :label="$t('feeDetailShopFeeRule.unitPrice')" align="center">
        <template slot-scope="scope">
          {{ scope.row.computingFormula === '2002' ? '-' : scope.row.squarePrice }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailShopFeeRule.additionalFee')" prop="additionalAmount" align="center" />
      <el-table-column :label="$t('feeDetailShopFeeRule.status')" align="center" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.state === '2008001'">{{ $t('feeDetailShopFeeRule.valid') }}</span>
          <span v-else>{{ $t('feeDetailShopFeeRule.invalid') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailShopFeeRule.billDate')" prop="curYearMonth" align="center" />
      <el-table-column :label="$t('feeDetailShopFeeRule.billCycle')" align="center">
        <template>{{ $t('feeDetailShopFeeRule.monthly') }}</template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailShopFeeRule.operation')" align="center" width="200">
        <template slot-scope="scope">
          <div v-if="scope.row.state !== '2009001'">
            <el-button v-if="hasPrivilege('502020090427190001')" type="text" size="mini"
              @click="_updateFeeRule(scope.row)">
              {{ $t('feeDetailShopFeeRule.change') }}
            </el-button>
            <el-button v-if="hasPrivilege('502021070488970005')" type="text" size="mini"
              @click="_finishFeeRule(scope.row)">
              {{ $t('feeDetailShopFeeRule.manualFinish') }}
            </el-button>
            <el-button v-if="hasPrivilege('502020090604200029')" type="text" size="mini"
              @click="_deleteFeeRule(scope.row)">
              {{ $t('feeDetailShopFeeRule.cancel') }}
            </el-button>
          </div>
          <div v-else>
            <el-button v-if="hasPrivilege('502020090604200029')" type="text" size="mini"
              @click="_deleteFeeRule(scope.row)">
              {{ $t('feeDetailShopFeeRule.cancel') }}
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page.sync="currentPage" :page-size="pageSize" :total="total"
      layout="total, prev, pager, next" @current-change="handlePageChange"
      style="margin-top: 20px; text-align: right;" />

    <delete-fee-rule ref="deleteFeeRule" @success="handleSuccess" />
    <edit-fee-rule ref="editFeeRule" @success="handleSuccess" />
    <finish-fee-rule ref="finishFeeRule" @success="handleSuccess" />
    <temp-import-room-fee ref="tempImportRoomFee" @success="handleSuccess" />
    <shop-create-fee-add ref="shopCreateFeeAdd" @success="handleSuccess" />

  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listPayFeeRule } from '@/api/shop/shopCreateFeeApi'
import DeleteFeeRule from './deleteFeeRule.vue'
import EditFeeRule from './editFeeRule.vue'
import FinishFeeRule from './finishFeeRule.vue'
import TempImportRoomFee from './tempImportRoomFee.vue'
import ShopCreateFeeAdd from '@/components/fee/shopCreateFeeAdd'

export default {
  name: 'FeeDetailShopFeeRule',
  components: {
    DeleteFeeRule,
    EditFeeRule,
    FinishFeeRule,
    ShopCreateFeeAdd,
    TempImportRoomFee
  },

  data() {
    return {
      shopId: '',
      shopNum: '',
      ownerId: '',
      ownerName: '',
      communityId: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      feeDetailShopFeeRuleInfo: {
        rules: [],
        feeId: '',
        shopId: '',
        shopNum: '',
        ownerId: '',
        ownerName: ''
      }
    }
  },
  watch: {
    shopId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.switchShopFeeRule()
        }
      }
    }
  },
  async created() {
    this.communityId = await getCommunityId()
  },
  methods: {
    open(params) {
      this.feeDetailShopFeeRuleInfo.shopId = params.shopId
      this.feeDetailShopFeeRuleInfo.shopNum = params.shopNum
      this.feeDetailShopFeeRuleInfo.ownerId = params.ownerId
      this.feeDetailShopFeeRuleInfo.ownerName = params.ownerName
      this.switchShopFeeRule()
    },
    async switchShopFeeRule() {
      await this._loadFeeDetailShopFeeRuleData(this.currentPage, this.pageSize)
    },

    async _loadFeeDetailShopFeeRuleData(page, row) {
      try {
        const params = {
          communityId: this.communityId,
          feeId: this.feeDetailShopFeeRuleInfo.feeId,
          payerObjId: this.feeDetailShopFeeRuleInfo.shopId,
          detailId: '-1',
          page,
          row
        }

        const response = await listPayFeeRule(params)
        this.feeDetailShopFeeRuleInfo.rules = response.data || []
        this.total = response.records || 0
      } catch (error) {
        console.error('Failed to load shop fee rules:', error)
        this.$message.error('Failed to load shop fee rules')
      }
    },

    handlePageChange(page) {
      this.currentPage = page
      this._loadFeeDetailShopFeeRuleData(page, this.pageSize)
    },

    _qureyFeeDetailShopFeeRule() {
      this._loadFeeDetailShopFeeRuleData(this.currentPage, this.pageSize)
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
        roomId: this.feeDetailShopFeeRuleInfo.shopId,
        roomName: this.feeDetailShopFeeRuleInfo.shopNum,
        ownerName: this.feeDetailShopFeeRuleInfo.ownerName,
        objType: '4444'
      })
    },

    _openShopCreateFeeAddModal() {
      this.$refs.shopCreateFeeAdd.open({
        shopId: this.feeDetailShopFeeRuleInfo.shopId,
        shopName: this.feeDetailShopFeeRuleInfo.shopNum,
        ownerId: this.feeDetailShopFeeRuleInfo.ownerId,
        ownerName: this.feeDetailShopFeeRuleInfo.ownerName
      })
    },

    viewRuleDetail(rule) {
      console.log('View rule detail:', rule)
    },

    handleSuccess() {
      this._loadFeeDetailShopFeeRuleData(this.currentPage, this.pageSize)
    }
  }
}
</script>

<style lang="scss" scoped>
.fee-detail-shop-fee-rule {
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
