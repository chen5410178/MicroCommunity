<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <div class="flex justify-between">
          <div class="text-title">{{ $t('feeConfigDetail.feeItemInfo') }}</div>
        </div>
      </div>

      <div class="form-wrapper">
        <el-row :gutter="20" class="text-left">
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.feeType') }}：</label>
              <label>{{ feeDetailInfo.feeTypeCdName }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.feeItem') }}：</label>
              <label>{{ feeDetailInfo.feeName }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.feeFlag') }}：</label>
              <label>{{ feeDetailInfo.feeFlagName || '-' }}</label>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="text-left">
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.paymentCycle') }}：</label>
              <label>{{ feeDetailInfo.paymentCycle }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.billType') }}：</label>
              <label>{{ feeDetailInfo.billTypeName }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.startTime') }}：</label>
              <label>{{ feeDetailInfo.startTime }}</label>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="text-left">
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.endTime') }}：</label>
              <label>{{ feeDetailInfo.endTime }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.deductFrom') }}：</label>
              <label>{{ feeDetailInfo.deductFrom == 'Y' ? $t('common.yes') : $t('common.no') }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.payOnline') }}：</label>
              <label>{{ feeDetailInfo.payOnline == 'Y' ? $t('common.yes') : $t('common.no') }}</label>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="text-left">
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.scale') }}：</label>
              <label v-if="feeDetailInfo.scale == '1'">{{ $t('feeConfigDetail.rounding') }}</label>
              <label v-if="feeDetailInfo.scale == '3'">{{ $t('feeConfigDetail.upRound') }}</label>
              <label v-if="feeDetailInfo.scale == '4'">{{ $t('feeConfigDetail.downRound') }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.decimalPlace') }}：</label>
              <label>{{ feeDetailInfo.decimalPlace }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.computingFormula') }}：</label>
              <label>{{ feeDetailInfo.computingFormulaName }}</label>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="text-left">
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.squarePrice') }}：</label>
              <label>{{ feeDetailInfo.squarePrice }}</label>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="form-group">
              <label class="col-form-label">{{ $t('feeConfigDetail.additionalAmount') }}：</label>
              <label>{{ feeDetailInfo.additionalAmount }}</label>
            </div>
          </el-col>
        </el-row>
      </div>

      <divider></divider>

      <div class="tab-wrapper">
        <el-tabs v-model="feeDetailInfo.currentTab" @tab-click="changeTab(feeDetailInfo.currentTab)">
          <el-tab-pane :label="$t('feeConfigDetail.modifyRecord')" name="feeConfigDetailHis">
            <fee-config-detail-his ref="feeConfigDetailHis"
              v-if="feeDetailInfo.currentTab === 'feeConfigDetailHis'"></fee-config-detail-his>
          </el-tab-pane>
          <el-tab-pane :label="$t('feeConfigDetail.createObject')" name="feeDetailFeeObj">
            <fee-detail-fee-obj ref="feeDetailFeeObj"
              v-if="feeDetailInfo.currentTab === 'feeDetailFeeObj'"></fee-detail-fee-obj>
          </el-tab-pane>
          <el-tab-pane :label="$t('feeConfigDetail.discount')" name="feeConfigDetailDiscount">
            <fee-config-detail-discount ref="feeConfigDetailDiscount"
              v-if="feeDetailInfo.currentTab === 'feeConfigDetailDiscount'"></fee-config-detail-discount>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getFeeConfigDetail } from '@/api/fee/feeConfigDetailApi'
import FeeConfigDetailHis from '@/components/fee/FeeConfigDetailHis'
import FeeDetailFeeObj from '@/components/fee/FeeDetailFeeObj'
import FeeConfigDetailDiscount from '@/components/fee/FeeConfigDetailDiscount'
import divider from '@/components/system/divider'

export default {
  name: 'FeeConfigDetailList',
  components: {
    FeeConfigDetailHis,
    FeeDetailFeeObj,
    FeeConfigDetailDiscount,
    divider
  },
  data() {
    return {
      feeDetailInfo: {
        configId: '',
        feeTypeCdName: '',
        feeName: '',
        feeFlagName: '',
        startTime: '',
        endTime: '',
        computingFormulaName: '',
        squarePrice: '',
        additionalAmount: '0.00',
        isDefault: '',
        billTypeName: '',
        paymentCycle: '',
        paymentCd: '',
        computingFormulaText: '',
        deductFrom: '',
        payOnline: 'Y',
        scale: '1',
        decimalPlace: '2',
        units: '元',
        currentTab: 'feeConfigDetailHis',
        needBack: false
      }
    }
  },
  created() {
    this.feeDetailInfo.configId = this.$route.query.configId
    if (this.feeDetailInfo.configId) {
      this.loadFeeDetailInfo()
    }
  },
  methods: {
    async loadFeeDetailInfo() {
      try {
        const params = {
          configId: this.feeDetailInfo.configId,
          communityId: this.getCommunityId(),
          page: 1,
          row: 1
        }
        const { feeConfigs } = await getFeeConfigDetail(params)
        Object.assign(this.feeDetailInfo, feeConfigs[0])
        this.changeTab(this.feeDetailInfo.currentTab)
      } catch (error) {
        console.error('Failed to load fee detail info:', error)
      }
    },
    changeTab(tab) {
      this.feeDetailInfo.currentTab = tab
      setTimeout(() => {
        this.$nextTick(() => {
          this.$refs[tab].switchTab({
            configId: this.feeDetailInfo.configId
          })
        })
      },500)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.form-wrapper {
  margin-top: 20px;

  .form-group {
    margin-bottom: 20px;

    .col-form-label {
      margin-right: 10px;
    }
  }
}

.tab-wrapper {
  margin-top: 20px;
}</style>