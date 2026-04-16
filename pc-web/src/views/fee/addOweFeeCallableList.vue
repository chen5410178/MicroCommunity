<template>
  <div class="add-owe-fee-callable-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>{{ $t('addOweFeeCallable.title') }}</span>
      </div>

      <el-form label-position="right" label-width="120px" class="text-left">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addOweFeeCallable.callableWay')">
              <el-select v-model="addOweFeeCallableInfo.callableWay" style="width:100%"
                :placeholder="$t('addOweFeeCallable.selectCallableWay')">
                <el-option v-for="item in callableWayOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addOweFeeCallable.feeConfigs')">
              <el-checkbox-group v-model="addOweFeeCallableInfo.configIds" @change="loadOweFeeRooms">
                <el-checkbox v-for="item in addOweFeeCallableInfo.feeConfigs" :key="item.configId"
                  :label="item.configId">
                  {{ item.feeName }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addOweFeeCallable.floor')">
              <el-select v-model="addOweFeeCallableInfo.floorId" style="width:100%" @change="loadOweFeeRooms"
                :placeholder="$t('addOweFeeCallable.selectFloor')">
                <el-option v-for="item in addOweFeeCallableInfo.floors" :key="item.floorId" :label="item.floorNum"
                  :value="item.floorId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addOweFeeCallable.timeRange')">
              <el-select v-model="addOweFeeCallableInfo.hasTime" style="width:100%" @change="loadOweFeeRooms"
                :placeholder="$t('addOweFeeCallable.selectTimeRange')">
                <el-option v-for="item in timeRangeOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-show="addOweFeeCallableInfo.hasTime === 'ON'">
          <el-col :span="12">
            <el-form-item :label="$t('addOweFeeCallable.startTime')">
              <el-date-picker v-model="addOweFeeCallableInfo.startTime" type="datetime" style="width:100%"
                :placeholder="$t('addOweFeeCallable.selectStartTime')">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addOweFeeCallable.endTime')">
              <el-date-picker v-model="addOweFeeCallableInfo.endTime" type="datetime" style="width:100%"
                :placeholder="$t('addOweFeeCallable.selectEndTime')">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addOweFeeCallable.oweRooms')">
              <div v-if="addOweFeeCallableInfo.rooms && addOweFeeCallableInfo.rooms.length > 0">
                <el-checkbox v-model="checkAllStatus" @change="handleCheckAllChange">
                  {{ $t('addOweFeeCallable.checkAll') }}
                </el-checkbox>
                <el-checkbox-group v-model="addOweFeeCallableInfo.roomIds" @change="handleCheckedRoomsChange">
                  <el-checkbox v-for="item in addOweFeeCallableInfo.rooms" :key="item.payerObjId"
                    :label="item.payerObjId">
                    {{ item.payerObjName }}
                  </el-checkbox>
                </el-checkbox-group>
              </div>
              <div v-else>
                {{ $t('addOweFeeCallable.noOweRooms') }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24" class="text-right">
            <el-button @click="goBack">{{ $t('common.back') }}</el-button>
            <el-button v-if="addOweFeeCallableInfo.callableWay === 'EXCEL'" type="primary"
              @click="exportCollectionLetterExcel">
              {{ $t('common.export') }}
            </el-button>
            <el-button v-else-if="addOweFeeCallableInfo.callableWay === 'TTS'" type="primary" @click="ttsOweFee">
              {{ $t('addOweFeeCallable.ttsCall') }}
            </el-button>
            <el-button v-else type="primary" @click="saveOweFeeCallable">
              {{ $t('common.submit') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import {
  saveOweFeeCallable,
  exportCollectionLetterExcel,
  listFeeConfigs,
  queryFloors,
  queryReportOweFee
} from '@/api/fee/addOweFeeCallableApi'

export default {
  name: 'AddOweFeeCallableList',
  data() {
    return {
      addOweFeeCallableInfo: {
        callableWay: '',
        remark: '',
        floorId: '',
        communityId: '',
        configIds: [],
        roomId: '',
        roomIds: [],
        rooms: [],
        feeConfigs: [],
        hasTime: 'OFF',
        startTime: '',
        endTime: '',
        floors: []
      },
      callableWayOptions: [
        { value: 'WECHAT', label: '微信模板消息' },
        { value: 'SMS', label: '短信' },
        { value: 'EXCEL', label: '导出' },
        { value: 'TTS', label: '呼电话催缴' }
      ],
      timeRangeOptions: [
        { value: 'OFF', label: '否' },
        { value: 'ON', label: '是' }
      ],
      checkAllStatus: false,
      loading: false
    }
  },
  created() {
    this.addOweFeeCallableInfo.communityId = getCommunityId()
    this.listFeeConfigs()
    this.loadOweFeeFloors()
  },
  methods: {
    async listFeeConfigs() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.addOweFeeCallableInfo.communityId,
          isDefault: 'F'
        }
        const { feeConfigs } = await listFeeConfigs(params)
        this.addOweFeeCallableInfo.feeConfigs = feeConfigs
      } catch (error) {
        console.error('获取费用配置失败:', error)
      }
    },
    async loadOweFeeFloors() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.addOweFeeCallableInfo.communityId
        }
        const { apiFloorDataVoList } = await queryFloors(params)
        this.addOweFeeCallableInfo.floors = apiFloorDataVoList
      } catch (error) {
        console.error('获取楼栋失败:', error)
      }
    },
    async loadOweFeeRooms() {
      if (!this.addOweFeeCallableInfo.floorId ||
        !this.addOweFeeCallableInfo.configIds ||
        this.addOweFeeCallableInfo.configIds.length === 0) {
        return
      }

      try {
        const params = {
          page: 1,
          row: 500,
          configIds: this.addOweFeeCallableInfo.configIds.join(','),
          communityId: this.addOweFeeCallableInfo.communityId,
          floorId: this.addOweFeeCallableInfo.floorId
        }
        const { data } = await queryReportOweFee(params)
        this.addOweFeeCallableInfo.rooms = data || []
        this.addOweFeeCallableInfo.roomIds = this.addOweFeeCallableInfo.rooms.map(item => item.payerObjId)
        this.checkAllStatus = this.addOweFeeCallableInfo.rooms.length > 0
      } catch (error) {
        console.error('获取欠费房屋失败:', error)
      }
    },
    handleCheckAllChange(val) {
      this.addOweFeeCallableInfo.roomIds = val ?
        this.addOweFeeCallableInfo.rooms.map(item => item.payerObjId) : []
    },
    handleCheckedRoomsChange(value) {
      const checkedCount = value.length
      this.checkAllStatus = checkedCount === this.addOweFeeCallableInfo.rooms.length
    },
    validateForm() {
      if (!this.addOweFeeCallableInfo.callableWay) {
        this.$message.error(this.$t('addOweFeeCallable.callableWayRequired'))
        return false
      }
      if (!this.addOweFeeCallableInfo.roomIds || this.addOweFeeCallableInfo.roomIds.length === 0) {
        this.$message.error(this.$t('addOweFeeCallable.roomsRequired'))
        return false
      }
      return true
    },
    async saveOweFeeCallable() {
      if (!this.validateForm()) return

      try {
        await saveOweFeeCallable(this.addOweFeeCallableInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        console.error('提交失败:', error)
        this.$message.error(error.message || this.$t('common.submitFailed'))
      }
    },
    async exportCollectionLetterExcel() {
      if (!this.validateForm()) return

      try {
        const params = {
          communityId: this.addOweFeeCallableInfo.communityId,
          pagePath: 'dataFeeManualCollection',
          configIds: this.addOweFeeCallableInfo.configIds.join(','),
          roomIds: this.addOweFeeCallableInfo.roomIds.join(','),
          startTime: this.addOweFeeCallableInfo.startTime,
          endTime: this.addOweFeeCallableInfo.endTime
        }
        await exportCollectionLetterExcel(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error(error.message || this.$t('common.exportFailed'))
      }
    },
    ttsOweFee() {
      if (!this.validateForm()) return

      const oweFee = this.addOweFeeCallableInfo.rooms
        .filter(room => this.addOweFeeCallableInfo.roomIds.includes(room.payerObjId))
        .map(room => ({ ...room, state: 'W' }))

      if (oweFee.length === 0) {
        this.$message.error(this.$t('addOweFeeCallable.noOweRoomsSelected'))
        return
      }

      const data = {
        configIds: this.addOweFeeCallableInfo.configIds,
        oweFee,
        startTime: this.addOweFeeCallableInfo.startTime,
        endTime: this.addOweFeeCallableInfo.endTime
      }

      localStorage.setItem('TTS_OWE_FEE_DATA', JSON.stringify(data))
      this.$router.push('/fee/ttsOweFeeCallable')
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.add-owe-fee-callable-container {
  padding: 20px;

  .el-checkbox-group {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
  }

  .el-checkbox {
    margin-right: 0;
  }

  .text-right {
    text-align: right;
  }
}
</style>