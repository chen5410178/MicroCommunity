<template>
  <div class="owner-exit-room-container">
    <!-- 业主信息 -->
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('ownerExitRoom.ownerInfo') }}</span>
        <div class="card-header-actions">
          <el-button type="primary" size="small" @click="selectOwner">{{ $t('common.select') }}</el-button>
          <el-button size="small" @click="goBack">{{ $t('common.back') }}</el-button>
        </div>
      </div>
      <el-row :gutter="20" class="text-left">
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('ownerExitRoom.name') }}:</label>
            <span>{{ ownerExitRoomInfo.name }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('ownerExitRoom.idCard') }}:</label>
            <span>{{ ownerExitRoomInfo.idCard }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>{{ $t('ownerExitRoom.phone') }}:</label>
            <span>{{ ownerExitRoomInfo.link }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 业主房屋 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('ownerExitRoom.ownerRoom') }}</span>
      </div>
      <el-table :data="ownerExitRoomInfo.rooms" border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="roomName" :label="$t('ownerExitRoom.room')" align="center">
          <template slot-scope="scope">
            {{ scope.row.floorNum }}-{{ scope.row.unitNum }}-{{ scope.row.roomNum }}
          </template>
        </el-table-column>
        <el-table-column prop="builtUpArea" :label="$t('ownerExitRoom.builtUpArea')" align="center">
          <template slot-scope="scope">
            {{ scope.row.builtUpArea }} {{ $t('ownerExitRoom.squareMeter') }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 费用信息 -->
    <el-card v-if="ownerExitRoomInfo.fees && ownerExitRoomInfo.fees.length > 0" class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('ownerExitRoom.feeInfo') }}</span>
      </div>
      <el-table :data="ownerExitRoomInfo.fees" border style="width: 100%">
        <el-table-column prop="feeName" :label="$t('ownerExitRoom.feeItem')" align="center"></el-table-column>
        <el-table-column prop="payerObjName" :label="$t('ownerExitRoom.room')" align="center"></el-table-column>
        <el-table-column prop="amountOwed" :label="$t('ownerExitRoom.arrears')" align="center"></el-table-column>
        <el-table-column prop="endTime" :label="$t('ownerExitRoom.startTime')" align="center"></el-table-column>
        <el-table-column prop="deadlineTime" :label="$t('ownerExitRoom.endTime')" align="center">
          <template slot-scope="scope">
            {{ scope.row.deadlineTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('ownerExitRoom.feeStatus')" align="center"></el-table-column>
      </el-table>
    </el-card>

    <!-- 提交按钮 -->
    <div class="submit-btn">
      <el-button type="primary" @click="saveOwnerExitRoomInfo">
        <i class="el-icon-check"></i>&nbsp;{{ $t('common.submit') }}
      </el-button>
    </div>

    <!-- 子组件 -->
    <search-owner ref="searchOwner" @chooseOwner="handleChooseOwner"></search-owner>
    <select-fee-config ref="selectFeeConfig"></select-fee-config>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { ownerExitRoom, queryRoomsByOwner, listFee } from '@/api/room/ownerExitRoomApi'
import SearchOwner from '@/components/owner/SearchOwner'
import SelectFeeConfig from '@/components/room/selectFeeConfig'

export default {
  name: 'OwnerExitRoomList',
  components: {
    SearchOwner,
    SelectFeeConfig
  },
  data() {
    return {
      ownerExitRoomInfo: {
        name: '',
        link: '',
        ownerId: '',
        idCard: '',
        rooms: [],
        selectRooms: [],
        batchFees: [],
        fees: [],
        communityId: ''
      }
    }
  },
  created() {
    this.ownerExitRoomInfo.communityId = getCommunityId()
  },
  methods: {
    selectOwner() {
      this.$refs.searchOwner.open()
    },
    goBack() {
      this.$router.go(-1)
    },
    handleChooseOwner(owner) {
      this.ownerExitRoomInfo = {
        ...this.ownerExitRoomInfo,
        name: owner.name,
        link: owner.link,
        ownerId: owner.ownerId,
        idCard: owner.idCard
      }
      this.listRoom()
    },
    handleSelectionChange(val) {
      this.ownerExitRoomInfo.selectRooms = val.map(item => item.roomId)
      this.dealFees()
    },
    async saveOwnerExitRoomInfo() {
      if (!this.ownerExitRoomInfo.ownerId) {
        this.$message.error(this.$t('ownerExitRoom.validate.ownerRequired'))
        return
      }

      if (!this.ownerExitRoomInfo.selectRooms || this.ownerExitRoomInfo.selectRooms.length < 1) {
        this.$message.error(this.$t('ownerExitRoom.validate.roomRequired'))
        return
      }

      const hasUnpaidFee = this.ownerExitRoomInfo.fees.some(fee => fee.state === '2008001')
      if (hasUnpaidFee) {
        this.$message.error(this.$t('ownerExitRoom.validate.unpaidFee'))
        return
      }

      try {
        const res = await ownerExitRoom(this.ownerExitRoomInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.submitFailed'))
      }
    },
    async listRoom() {
      try {
        const params = {
          ownerId: this.ownerExitRoomInfo.ownerId,
          communityId: this.ownerExitRoomInfo.communityId,
          page: 1,
          row: 100
        }
        const res = await queryRoomsByOwner(params)
        this.ownerExitRoomInfo.rooms = res.rooms
        this.ownerExitRoomInfo.selectRooms = res.rooms.map(room => room.roomId)
        this.loadBatchFees()
      } catch (error) {
        console.error('获取房屋列表失败:', error)
      }
    },
    async loadBatchFees() {
      try {
        const params = {
          communityId: this.ownerExitRoomInfo.communityId,
          ownerId: this.ownerExitRoomInfo.ownerId,
          payerObjType: '3333',
          state: '2008001',
          page: 1,
          row: 500
        }
        const res = await listFee(params)
        this.ownerExitRoomInfo.batchFees = res.fees || []
        this.dealFees()
      } catch (error) {
        console.error('获取费用列表失败:', error)
      }
    },
    dealFees() {
      const selectedRoomIds = this.ownerExitRoomInfo.selectRooms
      this.ownerExitRoomInfo.fees = this.ownerExitRoomInfo.batchFees.filter(fee =>
        selectedRoomIds.includes(fee.payerObjId)
      )
    }
  }
}
</script>

<style lang="scss" scoped>
.owner-exit-room-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .clearfix {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .info-item {
    margin-bottom: 10px;

    label {
      margin-right: 10px;
    }
  }

  .submit-btn {
    margin-top: 20px;
    text-align: right;
  }
}
</style>