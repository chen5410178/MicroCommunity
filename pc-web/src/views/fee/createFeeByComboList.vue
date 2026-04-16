<template>
  <div class="create-fee-by-combo-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <div>
          <span>{{ createFeeByComboInfo.payerObjName }}</span>
          <span>{{ $t('createFeeByCombo.title') }}</span>
        </div>
        <div class="header-tools">
          <el-button type="primary" size="small" @click="_chooseFeeCombo()">
            <i class="el-icon-search"></i>{{ $t('createFeeByCombo.chooseFeeCombo') }}
          </el-button>
          <el-button type="info" size="small" @click="_goBack()">
            <i class="el-icon-close"></i>{{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-table :data="createFeeByComboInfo.feeConfigs" border style="width: 100%" class="fee-config-table">
        <el-table-column type="selection" width="55" align="center" @selection-change="handleSelectionChange">
        </el-table-column>
        <el-table-column prop="feeTypeCdName" :label="$t('createFeeByCombo.feeType')" align="center"></el-table-column>
        <el-table-column prop="feeName" :label="$t('createFeeByCombo.feeItem')" align="center"></el-table-column>
        <el-table-column prop="feeFlagName" :label="$t('createFeeByCombo.feeFlag')" align="center"></el-table-column>
        <el-table-column :label="$t('createFeeByCombo.startTime')" align="center">
          <template slot-scope="scope">
            <el-date-picker v-model="scope.row.startTime" type="datetime"
              :placeholder="$t('createFeeByCombo.requiredStartTime')" value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%"></el-date-picker>
          </template>
        </el-table-column>
        <el-table-column :label="$t('createFeeByCombo.endTime')" align="center">
          <template slot-scope="scope">
            <el-date-picker v-if="scope.row.feeFlag != '1003006'" v-model="scope.row.endTime" type="datetime"
              :placeholder="$t('createFeeByCombo.requiredEndTime')" value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%" @change="validateTime(scope.row)"></el-date-picker>
            <span v-else>{{ $t('createFeeByCombo.feeItemEndTime') }}</span>
          </template>
        </el-table-column>
      </el-table>

      <div class="footer-buttons">
        <el-button type="primary" size="large" @click="_createFee()">
          {{ $t('common.submit') }}
        </el-button>
      </div>
    </el-card>

    <choose-fee-combo ref="chooseFeeCombo" @chooseFeeCombo="handleChooseFeeCombo"
      ></choose-fee-combo>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import ChooseFeeCombo from '@/components/fee/chooseFeeCombo'
import { createFeeByCombo, listFeeComboMember } from '@/api/fee/createFeeByComboApi'

export default {
  name: 'CreateFeeByComboList',
  components: {
    ChooseFeeCombo
  },
  data() {
    return {
      createFeeByComboInfo: {
        feeConfigs: [],
        selectConfigIds: [],
        communityId: '',
        payerObjId: '',
        payerObjName: '',
        payerObjType: '',
        comboId: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initData()
  },
  methods: {
    _initData() {
      this.createFeeByComboInfo.payerObjId = this.$route.query.payerObjId
      this.createFeeByComboInfo.payerObjType = this.$route.query.payerObjType
      this.createFeeByComboInfo.payerObjName = this.$route.query.payerObjName
    },
    _chooseFeeCombo() {
      this.$refs.chooseFeeCombo.open()
    },
    handleChooseFeeCombo(feeCombo) {
      this.createFeeByComboInfo.comboId = feeCombo.comboId
      this._listFeeComboMembers(feeCombo)
    },
    handleLoadData(params) {
      this._listFeeComboMembers(params)
    },
    async _listFeeComboMembers(feeCombo) {
      try {
        const params = {
          page: 1,
          row: 100,
          comboId: feeCombo.comboId
        }
        const { data } = await listFeeComboMember(params)
        this.createFeeByComboInfo.selectConfigIds = []
        console.log('data:', data)
        data.forEach(config => {
          config.startTime = ''
          config.endTime = ''
          this.createFeeByComboInfo.selectConfigIds.push(config.configId)
        })
        this.createFeeByComboInfo.feeConfigs = data
      } catch (error) {
        console.error('Failed to load fee combo members:', error)
      }
    },
    validateTime(row) {
      if (row.startTime && row.endTime) {
        const start = new Date(row.startTime).getTime()
        const end = new Date(row.endTime).getTime()
        if (start >= end) {
          this.$message.error(this.$t('createFeeByCombo.endTimeMustGreater'))
          row.endTime = ''
        }
      }
    },
    handleSelectionChange(val) {
      this.createFeeByComboInfo.selectConfigIds = val.map(item => item.configId)
    },
    async _createFee() {
      const selectedFees = this.createFeeByComboInfo.feeConfigs.filter(item =>
        this.createFeeByComboInfo.selectConfigIds.includes(item.configId)
      )

      if (selectedFees.length === 0) {
        this.$message.warning(this.$t('createFeeByCombo.noFeeSelected'))
        return
      }

      for (const fee of selectedFees) {
        if (!fee.startTime) {
          this.$message.warning(fee.feeName + this.$t('createFeeByCombo.startTimeRequired'))
          return
        }
        if (fee.feeFlag !== '1003006' && !fee.endTime) {
          this.$message.warning(fee.feeName + this.$t('createFeeByCombo.endTimeRequired'))
          return
        }
      }

      const postData = {
        communityId: this.communityId,
        configs: selectedFees,
        payerObjId: this.createFeeByComboInfo.payerObjId,
        payerObjName: this.createFeeByComboInfo.payerObjName,
        payerObjType: this.createFeeByComboInfo.payerObjType,
        comboId: this.createFeeByComboInfo.comboId
      }

      try {
        const response = await createFeeByCombo(postData)
        this.$message.success(response.msg)
        if (response.code === 0) {
          this.$router.go(-1)
        }
      } catch (error) {
        console.error('Failed to create fee:', error)
        this.$message.error(error.message || this.$t('common.operationFailed'))
      }
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.create-fee-by-combo-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        display: flex;
        gap: 10px;
      }
    }
  }

  .fee-config-table {
    margin-top: 20px;
  }

  .footer-buttons {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>