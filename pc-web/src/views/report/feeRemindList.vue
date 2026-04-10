<template>
  <div class="fee-remind-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('feeRemind.queryCondition') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input v-model.trim="feeRemindInfo.conditions.objName" :placeholder="$t('feeRemind.objNamePlaceholder')"
            clearable />
        </el-col>
        <el-col :span="4">
          <el-input v-model.trim="feeRemindInfo.conditions.ownerName"
            :placeholder="$t('feeRemind.ownerNamePlaceholder')" clearable />
        </el-col>
        <el-col :span="4">
          <el-input v-model.trim="feeRemindInfo.conditions.link" :placeholder="$t('feeRemind.linkPlaceholder')"
            clearable />
        </el-col>
        <el-col :span="4">
          <el-select v-model="feeRemindInfo.conditions.configId" :placeholder="$t('feeRemind.feeConfigPlaceholder')"
            style="width:100%" clearable>
            <el-option v-for="item in feeRemindInfo.feeConfigs" :key="item.configId" :label="item.feeName"
              :value="item.configId" />
          </el-select>
        </el-col>
        <el-col v-if="feeRemindInfo.communitys.length > 1" :span="4">
          <el-select v-model="feeRemindInfo.conditions.communityId" :placeholder="$t('feeRemind.communityPlaceholder')"
            style="width:100%" @change="_changCommunity" clearable>
            <el-option v-for="item in feeRemindInfo.communitys" :key="item.communityId" :label="item.name"
              :value="item.communityId" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="_queryMethod">
            <i class="el-icon-search"></i>
            {{ $t('feeRemind.query') }}
          </el-button>
          <el-button @click="_resetMethod">
            <i class="el-icon-refresh"></i>
            {{ $t('feeRemind.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="box-card margin-top">
      <el-tabs v-model="feeRemindInfo._currentTab" @tab-click="changeTab(feeRemindInfo._currentTab)">
        <el-tab-pane :label="$t('feeRemind.prePaymentRemind')" name="reportPrePaymentFee">
          <report-pre-payment-fee v-if="feeRemindInfo._currentTab === 'reportPrePaymentFee'" ref="prePaymentFee" />
        </el-tab-pane>
        <el-tab-pane :label="$t('feeRemind.deadlineRemind')" name="reportDeadlineFee">
          <report-deadline-fee v-if="feeRemindInfo._currentTab === 'reportDeadlineFee'" ref="deadlineFee" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import ReportPrePaymentFee from '@/components/report/reportPrePaymentFee'
import ReportDeadlineFee from '@/components/report/reportDeadlineFee'
import {
  listFeeConfigs,
  listMyEnteredCommunitys
} from '@/api/report/feeRemindApi'

export default {
  name: 'FeeRemindList',
  components: {
    ReportPrePaymentFee,
    ReportDeadlineFee
  },
  data() {
    return {
      feeRemindInfo: {
        _currentTab: 'reportPrePaymentFee',
        feeConfigs: [],
        moreCondition: false,
        communitys: [],
        conditions: {
          objName: '',
          configId: '',
          ownerName: '',
          link: '',
          communityId: ''
        }
      }
    }
  },
  created() {
    this.feeRemindInfo.conditions.communityId = getCommunityId()
    this._loadStaffCommunitys()
    this._listFeeConfigs()
  },
  methods: {
    changeTab(tab) {
      this.feeRemindInfo._currentTab = tab
      setTimeout(() => {
        if (tab === 'reportPrePaymentFee') {
          this.$refs.prePaymentFee && this.$refs.prePaymentFee.loadData(this.feeRemindInfo.conditions)
        } else {
          this.$refs.deadlineFee && this.$refs.deadlineFee.loadData(this.feeRemindInfo.conditions)
        }
      }, 500)

    },
    _queryMethod() {
      this.changeTab(this.feeRemindInfo._currentTab)
    },
    _resetMethod() {
      this.feeRemindInfo.conditions = {
        objName: '',
        configId: '',
        ownerName: '',
        link: '',
        communityId: getCommunityId()
      }
      this.changeTab(this.feeRemindInfo._currentTab)
    },
    async _listFeeConfigs() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.feeRemindInfo.conditions.communityId,
          isDefault: 'F'
        }
        const data = await listFeeConfigs(params)
        this.feeRemindInfo.feeConfigs = data.feeConfigs
      } catch (error) {
        console.error('Failed to load fee configs:', error)
      }
    },
    _moreCondition() {
      this.feeRemindInfo.moreCondition = !this.feeRemindInfo.moreCondition
    },
    async _loadStaffCommunitys() {
      try {
        const params = {
          _uid: '123mlkdinkldldijdhuudjdjkkd',
          page: 1,
          row: 100
        }
        const data = await listMyEnteredCommunitys(params)
        this.feeRemindInfo.communitys = data.communitys
      } catch (error) {
        console.error('Failed to load communities:', error)
      }
    },
    _changCommunity() {
      this._listFeeConfigs()
      this.changeTab(this.feeRemindInfo._currentTab)
    }
  }
}
</script>

<style lang="scss" scoped>
.fee-remind-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .margin-top {
    margin-top: 20px;
  }

  .el-col {
    margin-bottom: 10px;
  }

  .el-button+.el-button {
    margin-left: 10px;
  }
}
</style>