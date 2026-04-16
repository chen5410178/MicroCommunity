<template>
  <el-card class="box-card">
    <div slot="header" class="flex justify-between">
      <span>{{ $t('viewMainFee.feeInfo') }}</span>
      <el-button type="primary" size="small" style="float: right; margin-left: 10px;" @click="_openCallBackOwner">
        {{ $t('common.back') }}
      </el-button>
    </div>

    <el-row :gutter="20" class="text-left">
      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.feeId') }}:</label>
          <div>{{ mainFeeInfo.feeId }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.feeFlag') }}:</label>
          <div>{{ mainFeeInfo.feeFlagName }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.feeType') }}:</label>
          <div>{{ mainFeeInfo.feeTypeCdName }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.payerObj') }}:</label>
          <div>
            {{ mainFeeInfo.payerObjName }}
            <view-room-data v-if="mainFeeInfo.payerObjType == '3333'" ref="viewRoomData" />
          </div>
        </div>
      </el-col>

      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.feeItem') }}:</label>
          <div class="hand" @click="_viewFeeConfig">
            {{ mainFeeInfo.feeName }}
            <view-fee-config-data ref="viewFeeConfigData" />
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.feeStatus') }}:</label>
          <div>{{ mainFeeInfo.stateName }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.startTime') }}:</label>
          <div>{{ mainFeeInfo.startTime }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.billingStartTime') }}:</label>
          <div>{{ _getEndTime(mainFeeInfo) }}</div>
        </div>
      </el-col>

      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.billingEndTime') }}:</label>
          <div>{{ _getDeadlineTime(mainFeeInfo) }}</div>
        </div>
      </el-col>
      <el-col :span="6" v-if="mainFeeInfo.amount != '-1.00'">
        <div class="form-item">
          <label>{{ $t('viewMainFee.feeAmount') }}:</label>
          <div>{{ mainFeeInfo.amount }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.batch') }}:</label>
          <div>{{ mainFeeInfo.batchId }}</div>
        </div>
      </el-col>

      <template v-for="(item, index) in mainFeeInfo.feeAttrs">
        <el-col :span="6" :key="index">
          <div class="form-item">
            <label>{{ item.specCdName }}：</label>
            <div>{{ item.value }}</div>
          </div>
        </el-col>
      </template>
    </el-row>

    <el-row :gutter="20" v-if="mainFeeInfo.payerObjType == '3333'">
      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.area') }}:</label>
          <div>{{ mainFeeInfo.builtUpArea }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.unitPrice') }}:</label>
          <div>{{ mainFeeInfo.squarePrice }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.additionalFee') }}:</label>
          <div>{{ mainFeeInfo.additionalAmount }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" v-else>
      <el-col :span="6">
        <div class="form-item">
          <label>{{ $t('viewMainFee.fixedFee') }}:</label>
          <div>{{ mainFeeInfo.additionalAmount }}</div>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listFee } from '@/api/fee/propertyFeeApi'
import ViewRoomData from './viewRoomData'
import ViewFeeConfigData from './viewFeeConfigData'

export default {
  name: 'ViewMainFee',
  components: {
    ViewRoomData,
    ViewFeeConfigData
  },
  props: {
    feeName: {
      type: String,
      required: true
    },
    feeTypeCd: {
      type: String,
      required: true
    },
    payName: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      mainFeeInfo: {
        feeName: this.feeName,
        feeId: "",
        feeTypeCd: '',
        builtUpArea: "",
        startTime: "",
        endTime: "",
        amount: "-1.00",
        feeFlagName: '',
        feeTypeCdName: '',
        configId: '',
        stateName: '',
        additionalAmount: 0.0,
        squarePrice: 0,
        deadlineTime: '',
        payerObjType: '',
        payerObjId: '',
        payerObjName: '',
        feeAttrs: [],
        batchId: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    const feeId = this.$route.query.feeId
    if (feeId) {
      this.loadMainFeeInfo({ feeId })
    }
  },
  methods: {
    async loadMainFeeInfo(params) {
      try {
        const res = await listFee({
          ...params,
          communityId: this.communityId,
          page: 1,
          row: 1
        })

        if (res.fees && res.fees.length > 0) {
          Object.assign(this.mainFeeInfo, res.fees[0])
          this.mainFeeInfo.feeAttrs = res.fees[0].feeAttrs || []

          if (this.mainFeeInfo.payerObjType === '3333') {
            this._loadRoomAndOwnerByRoomId()
          } else if (this.mainFeeInfo.payerObjType === '6666') {
            this._loadRoomAndOwnerByCarId()
          } else {
            this._loadContractAndOwnerByContractId()
          }

          this.$emit('listFeeDetail', { feeId: this.mainFeeInfo.feeId })
        }
      } catch (error) {
        console.error('加载主费用信息失败:', error)
      }
    },
    _openCallBackOwner() {
      this.$router.go(-1)
    },
    _getDeadlineTime(fee) {
      if (fee.endTime === fee.deadlineTime || fee.state === '2009001') {
        return "-"
      }
      return fee.deadlineTime
    },
    _getEndTime(fee) {
      if (fee.state === '2009001') {
        return "-"
      }
      return fee.endTime
    },
    _viewFeeConfig() {
      this.$refs.viewFeeConfigData.open({ configId: this.mainFeeInfo.configId })
    },
    _viewRoomData() {
      this.$refs.viewRoomData.open({ roomId: this.mainFeeInfo.payerObjId })
    },
    async _loadRoomAndOwnerByRoomId() {
      // 实现获取房间和业主信息的逻辑
    },
    async _loadRoomAndOwnerByCarId() {
      // 实现获取车位和业主信息的逻辑
    },
    async _loadContractAndOwnerByContractId() {
      // 实现获取合同和业主信息的逻辑
    }
  }
}
</script>

<style scoped>
.form-item {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  margin-bottom: 30px;
  align-items: center;

  div {
    margin-left: 10px;
  }
}

.hand {
  cursor: pointer;
  color: #409EFF;

  &:hover {
    text-decoration: underline;
  }
}
</style>