<template>
  <div class="room-create-pay-fee-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('roomCreatePayFee.title') }}</span>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form ref="form" :model="roomCreatePayFeeInfo" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('roomCreatePayFee.feeType')">
                  <el-select v-model="roomCreatePayFeeInfo.feeTypeCd" @change="changeFeeTypeCd" style="width:100%"
                    :placeholder="$t('roomCreatePayFee.selectFeeType')">
                    <template v-for="(item, index) in roomCreatePayFeeInfo.feeTypeCds">
                      <el-option :key="index" :label="item.name" :value="item.statusCd"
                        v-if="item.statusCd != '888800010017'">
                      </el-option>
                    </template>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('roomCreatePayFee.feeItem')">
                  <el-select v-model="roomCreatePayFeeInfo.configId" @change="roomCreatePayFeeIfOnceFee"
                    style="width:100%" :placeholder="$t('roomCreatePayFee.selectFeeItem')">
                    <template v-for="(item, index) in roomCreatePayFeeInfo.feeConfigs">
                      <el-option :key="index" :label="item.feeName" :value="item.configId">
                      </el-option>
                    </template>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('roomCreatePayFee.startTime')">
                  <el-date-picker v-model="roomCreatePayFeeInfo.startTime" type="date" style="width:100%" value-format="yyyy-MM-dd"
                    :placeholder="$t('roomCreatePayFee.selectStartTime')">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('roomCreatePayFee.endTime')">
                  <el-date-picker v-model="roomCreatePayFeeInfo.endTime" type="date" style="width:100%" value-format="yyyy-MM-dd"
                    :placeholder="$t('roomCreatePayFee.selectEndTime')">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20" v-if="roomCreatePayFeeInfo.computingFormula == '4004'">
              <el-col :span="12">
                <el-form-item :label="$t('roomCreatePayFee.amount')">
                  <el-input v-model="roomCreatePayFeeInfo.amount" :placeholder="$t('roomCreatePayFee.inputAmount')">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20" v-if="roomCreatePayFeeInfo.computingFormula == '1102'">
              <el-col :span="12">
                <el-form-item :label="$t('roomCreatePayFee.rateCycle')">
                  <el-input v-model="roomCreatePayFeeInfo.rateCycle"
                    :placeholder="$t('roomCreatePayFee.inputRateCycle')">
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('roomCreatePayFee.rate')">
                  <el-input v-model="roomCreatePayFeeInfo.rate" :placeholder="$t('roomCreatePayFee.inputRate')">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20" v-show="roomCreatePayFeeInfo.computingFormula == '1102'">
              <el-col :span="12">
                <el-form-item :label="$t('roomCreatePayFee.rateStartTime')">
                  <el-date-picker v-model="roomCreatePayFeeInfo.rateStartTime" type="date" style="width:100%"
                    value-format="yyyy-MM-dd" :placeholder="$t('roomCreatePayFee.selectRateStartTime')">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('roomCreatePayFee.building')">
                  <el-select v-model="roomCreatePayFeeInfo.floorId" @change="loadFloorRooms" style="width:100%"
                    :placeholder="$t('roomCreatePayFee.selectBuilding')">
                    <el-option v-for="(item, index) in roomCreatePayFeeInfo.floors" :key="index" :label="item.floorNum"
                      :value="item.floorId">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('roomCreatePayFee.roomState')">
                  <el-checkbox-group v-model="roomCreatePayFeeInfo.roomState" @change="changeRoomState">
                    <el-checkbox label="2001">{{ $t('roomCreatePayFee.occupied') }}</el-checkbox>
                    <el-checkbox label="2003">{{ $t('roomCreatePayFee.delivered') }}</el-checkbox>
                    <el-checkbox label="2005">{{ $t('roomCreatePayFee.decorated') }}</el-checkbox>
                    <el-checkbox label="2004">{{ $t('roomCreatePayFee.vacant') }}</el-checkbox>
                    <el-checkbox label="2006">{{ $t('roomCreatePayFee.rented') }}</el-checkbox>
                    <el-checkbox label="2007">{{ $t('roomCreatePayFee.sold') }}</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24" class="text-left">
                <el-form-item :label="$t('roomCreatePayFee.room')">
                  <div v-show="roomCreatePayFeeInfo.rooms && roomCreatePayFeeInfo.rooms.length > 0">
                    <el-checkbox v-model="checkAllRooms" @change="checkAll">{{ $t('roomCreatePayFee.selectAll')
                    }}</el-checkbox>
                    <br />
                    <el-checkbox-group v-model="roomCreatePayFeeInfo.roomIds">
                      <el-checkbox v-for="(item, index) in roomCreatePayFeeInfo.rooms" :key="index"
                        :label="item.roomId">
                        <span v-if="item.unitNum != '0'">{{ item.roomName }}</span>
                        <span v-else>{{ item.floorNum }}-{{ item.roomNum }}</span>
                        ({{ item.stateName }})
                      </el-checkbox>
                    </el-checkbox-group>
                  </div>
                  <div v-show="!roomCreatePayFeeInfo.rooms || roomCreatePayFeeInfo.rooms.length < 1">
                    <span>{{ $t('roomCreatePayFee.noRoom') }}</span>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24" class="text-right">
                <el-button type="warning" @click="goBack">{{ $t('common.back') }}</el-button>
                <el-button type="primary" @click="saveRoomCreatePayFee">{{ $t('common.submit') }}</el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId, getDict } from '@/api/community/communityApi'
import {
  listFeeConfigs,
  queryFloors,
  queryRooms,
  saveRoomCreatePayFee
} from '@/api/fee/roomCreatePayFeeApi'
import { dateFormat, addMonth } from '@/utils/dateUtil'

export default {
  name: 'RoomCreatePayFeeList',
  data() {
    return {
      roomCreatePayFeeInfo: {
        feeTypeCds: [],
        feeConfigs: [],
        remark: '',
        floorId: '',
        communityId: '',
        feeTypeCd: '',
        configId: '',
        roomIds: [],
        rooms: [],
        hasTime: 'OFF',
        startTime: '',
        endTime: '2050-01-01',
        feeFlag: '',
        computingFormula: '',
        amount: '',
        rateCycle: '',
        rate: '',
        rateStartTime: '',
        roomState: ['2001', '2003', '2005', '2004', '2006', '2007'],
        floors: []
      },
      checkAllRooms: false
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      this.roomCreatePayFeeInfo.communityId = getCommunityId()
      await this.loadFeeTypeCds()
      await this.loadFloors()
    },
    async loadFeeTypeCds() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.roomCreatePayFeeInfo.feeTypeCds = data.filter(item =>
          item.statusCd !== "888800010015" && item.statusCd !== "888800010016"
        )
      } catch (error) {
        console.error('Failed to load fee types:', error)
      }
    },
    async loadFloors() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.roomCreatePayFeeInfo.communityId
        }
        const { apiFloorDataVoList } = await queryFloors(params)
        this.roomCreatePayFeeInfo.floors = apiFloorDataVoList
      } catch (error) {
        console.error('Failed to load floors:', error)
      }
    },
    async changeFeeTypeCd(feeTypeCd) {
      if (feeTypeCd === '888800010006' || feeTypeCd === '888800010014') {
        this.roomCreatePayFeeInfo.startTime = dateFormat(new Date())
        this.roomCreatePayFeeInfo.endTime = dateFormat(addMonth(new Date(), 1))
      }
      this.roomCreatePayFeeInfo.configId = ''

      try {
        const params = {
          page: 1,
          row: 500,
          communityId: this.roomCreatePayFeeInfo.communityId,
          feeTypeCd: feeTypeCd,
          isDefault: 'F',
          valid: '1'
        }
        const { feeConfigs } = await listFeeConfigs(params)
        this.roomCreatePayFeeInfo.feeConfigs = feeConfigs
      } catch (error) {
        console.error('Failed to load fee configs:', error)
      }
    },
    async loadFloorRooms() {
      this.roomCreatePayFeeInfo.rooms = []
      this.roomCreatePayFeeInfo.roomIds = []

      if (!this.roomCreatePayFeeInfo.floorId) return

      try {
        const params = {
          page: 1,
          row: 500,
          communityId: this.roomCreatePayFeeInfo.communityId,
          floorId: this.roomCreatePayFeeInfo.floorId,
          roomState: this.roomCreatePayFeeInfo.roomState.join(',')
        }
        const { rooms } = await queryRooms(params)
        this.roomCreatePayFeeInfo.rooms = rooms
        if (rooms && rooms.length > 0) {
          this.roomCreatePayFeeInfo.roomIds = rooms.map(room => room.roomId)
        }
      } catch (error) {
        console.error('Failed to load rooms:', error)
      }
    },
    changeRoomState() {
      setTimeout(() => {
        this.loadFloorRooms()
      }, 1000)
    },
    roomCreatePayFeeIfOnceFee(configId) {
      if (this.roomCreatePayFeeInfo.feeTypeCd !== '888800010006' &&
        this.roomCreatePayFeeInfo.feeTypeCd !== '888800010014' &&
        this.roomCreatePayFeeInfo.feeFlag === '1003006') {
        this.roomCreatePayFeeInfo.endTime = ''
      }

      const config = this.roomCreatePayFeeInfo.feeConfigs.find(item => item.configId === configId)
      if (config) {
        this.roomCreatePayFeeInfo.feeFlag = config.feeFlag
        this.roomCreatePayFeeInfo.computingFormula = config.computingFormula
      }
    },
    checkAll(checked) {
      if (checked) {
        this.roomCreatePayFeeInfo.roomIds = this.roomCreatePayFeeInfo.rooms.map(room => room.roomId)
      } else {
        this.roomCreatePayFeeInfo.roomIds = []
      }
    },
    async saveRoomCreatePayFee() {
      if (!this.validate()) {
        this.$message.error(this.$t('roomCreatePayFee.validateError'))
        return
      }

      if (!this.roomCreatePayFeeInfo.roomIds || this.roomCreatePayFeeInfo.roomIds.length < 1) {
        this.$message.error(this.$t('roomCreatePayFee.noRoomSelected'))
        return
      }

      try {
        await saveRoomCreatePayFee(this.roomCreatePayFeeInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        this.$message.error(error.message || this.$t('common.saveFailed'))
      }
    },
    validate() {
      if (!this.roomCreatePayFeeInfo.configId) {
        return false
      }
      // 日期统一字符串化，防止 Date 对象序列化产生 UTC 时间偏移
      if (this.roomCreatePayFeeInfo.startTime && typeof this.roomCreatePayFeeInfo.startTime !== 'string') {
        this.roomCreatePayFeeInfo.startTime = dateFormat(this.roomCreatePayFeeInfo.startTime)
      }
      if (this.roomCreatePayFeeInfo.endTime && typeof this.roomCreatePayFeeInfo.endTime !== 'string') {
        this.roomCreatePayFeeInfo.endTime = dateFormat(this.roomCreatePayFeeInfo.endTime)
      }
      if (this.roomCreatePayFeeInfo.rateStartTime && typeof this.roomCreatePayFeeInfo.rateStartTime !== 'string') {
        this.roomCreatePayFeeInfo.rateStartTime = dateFormat(this.roomCreatePayFeeInfo.rateStartTime)
      }
      return true
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.room-create-pay-fee-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .text-right {
    text-align: right;
  }

  .el-checkbox-group {
    margin-top: 10px;

    .el-checkbox {
      margin-right: 15px;
      margin-bottom: 10px;
    }
  }
}
</style>