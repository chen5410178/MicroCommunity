<template>
  <div class="operate-data-log-container">
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('operateDataLog.search.title') }}</span>
        <el-button type="text" style="float: right; padding: 3px 0" @click="_moreCondition">
          {{ operateDataLogInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button>
      </div>

      <el-row :gutter="20">
        <el-col :span="6">
          <el-date-picker v-model="operateDataLogInfo.conditions.logStartTime" type="datetime"
            :placeholder="$t('operateDataLog.search.logStartTime')" style="width: 100%"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-col>

        <el-col :span="6">
          <el-date-picker v-model="operateDataLogInfo.conditions.logEndTime" type="datetime"
            :placeholder="$t('operateDataLog.search.logEndTime')" style="width: 100%" value-format="yyyy-MM-dd HH:mm:ss"
            :picker-options="endDateOptions">
          </el-date-picker>
        </el-col>

        <el-col :span="6">
          <el-input v-model="operateDataLogInfo.conditions.staffNameLike"
            :placeholder="$t('operateDataLog.search.staffNameLike')">
          </el-input>
        </el-col>

        <el-col :span="6">
          <el-button type="primary" @click="_queryDataMethod">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="_resetDataMethod">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-show="operateDataLogInfo.moreCondition">
        <el-col :span="6"
          v-if="operateDataLogInfo._currentTab === 'feeConfigDetailHis' || operateDataLogInfo._currentTab === 'feeDetailHis'">
          <el-input v-model="operateDataLogInfo.conditions.feeNameLike"
            :placeholder="$t('operateDataLog.search.feeNameLike')">
          </el-input>
        </el-col>

        <el-col :span="6" v-if="operateDataLogInfo._currentTab === 'feeDetailHis'">
          <el-input v-model="operateDataLogInfo.conditions.payerObjName"
            :placeholder="$t('operateDataLog.search.payerObjName')">
          </el-input>
        </el-col>

        <el-col :span="6" v-if="operateDataLogInfo._currentTab === 'ownerDetailHis'">
          <el-input v-model="operateDataLogInfo.conditions.ownerNameLike"
            :placeholder="$t('operateDataLog.search.ownerNameLike')">
          </el-input>
        </el-col>

        <el-col :span="6" v-if="operateDataLogInfo._currentTab === 'roomDetailHis'">
          <el-input v-model="operateDataLogInfo.conditions.roomName"
            :placeholder="$t('operateDataLog.search.roomName')">
          </el-input>
        </el-col>

        <el-col :span="6" v-if="operateDataLogInfo._currentTab === 'carDetailHis'">
          <el-input v-model="operateDataLogInfo.conditions.carNumLike"
            :placeholder="$t('operateDataLog.search.carNumLike')">
          </el-input>
        </el-col>

        <el-col :span="6" v-if="operateDataLogInfo._currentTab === 'contractDetailChange'">
          <el-input v-model="operateDataLogInfo.conditions.contractCode"
            :placeholder="$t('operateDataLog.search.contractCode')">
          </el-input>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="table-card">
      <el-tabs v-model="operateDataLogInfo._currentTab" @tab-click="changeTab(operateDataLogInfo._currentTab)">
        <el-tab-pane label="费用项" name="feeConfigDetailHis"></el-tab-pane>
        <el-tab-pane label="费用" name="feeDetailHis"></el-tab-pane>
        <el-tab-pane label="业主" name="ownerDetailHis"></el-tab-pane>
        <el-tab-pane label="房屋" name="roomDetailHis"></el-tab-pane>
        <el-tab-pane label="车辆" name="carDetailHis"></el-tab-pane>
        <el-tab-pane label="合同" name="contractDetailChange"></el-tab-pane>
      </el-tabs>

      <component :is="currentComponent" :ref="currentComponent"></component>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import FeeConfigDetailHis from '@/components/system/feeConfigDetailHis'
import FeeDetailHis from '@/components/fee/feeDetailHis'
import OwnerDetailHis from '@/components/owner/ownerDetailHis'
import RoomDetailHis from '@/components/system/roomDetailHis'
import CarDetailHis from '@/components/system/carDetailHis'
import ContractDetailChange from '@/components/system/contractDetailChange'

export default {
  name: 'OperateDataLogList',
  components: {
    FeeConfigDetailHis,
    FeeDetailHis,
    OwnerDetailHis,
    RoomDetailHis,
    CarDetailHis,
    ContractDetailChange
  },
  data() {
    return {
      communityId: '',
      operateDataLogInfo: {
        _currentTab: 'feeConfigDetailHis',
        moreCondition: false,
        conditions: {
          logStartTime: '',
          logEndTime: '',
          staffNameLike: '',
          feeNameLike: '',
          payerObjName: '',
          ownerNameLike: '',
          roomName: '',
          carNumLike: '',
          contractCode: ''
        }
      },
      endDateOptions: {
        disabledDate: (time) => {
          if (this.operateDataLogInfo.conditions.logStartTime) {
            return time.getTime() <= new Date(this.operateDataLogInfo.conditions.logStartTime).getTime()
          }
          return false
        }
      }
    }
  },
  computed: {
    currentComponent() {
      return this.operateDataLogInfo._currentTab
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.changeTab(this.operateDataLogInfo._currentTab)
  },
  methods: {
    changeTab(tab) {  
        this.operateDataLogInfo._currentTab = tab
      setTimeout(() => {
        if(tab === 'ownerDetailHis') {
          this.$refs[tab].open('','','',this.operateDataLogInfo.conditions.logStartTime,this.operateDataLogInfo.conditions.logEndTime,this.operateDataLogInfo.conditions.staffNameLike)
          return;
        }
        if (this.$refs[tab]) {
          this.$refs[tab].open(this.operateDataLogInfo.conditions)
        }
      }, 100)
    },
    _queryDataMethod() {
      this.changeTab(this.operateDataLogInfo._currentTab)
    },
    _resetDataMethod() {
      this.operateDataLogInfo.conditions = {
        logStartTime: '',
        logEndTime: '',
        staffNameLike: '',
        feeNameLike: '',
        payerObjName: '',
        ownerNameLike: '',
        roomName: '',
        carNumLike: '',
        contractCode: ''
      }
      this.changeTab(this.operateDataLogInfo._currentTab)
    },
    _moreCondition() {
      this.operateDataLogInfo.moreCondition = !this.operateDataLogInfo.moreCondition
    }
  }
}
</script>

<style lang="scss" scoped>
.operate-data-log-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    margin-bottom: 20px;
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
}
</style>