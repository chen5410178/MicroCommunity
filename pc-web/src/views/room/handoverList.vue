<template>
  <div class="handover-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('handover.ownerInfo') }}</span>
        <div class="card-header-right">
          <el-button type="primary" size="small" @click="selectOwner">{{ $t('handover.select') }}</el-button>
          <el-button size="small" @click="goBack">{{ $t('handover.back') }}</el-button>
        </div>
      </div>
      <el-form>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('handover.name')">
              <el-input v-model="handoverInfo.name" :placeholder="$t('handover.namePlaceholder')" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('handover.gender')">
              <el-select v-model="handoverInfo.sex" :placeholder="$t('handover.genderPlaceholder')" style="width:100%">
                <el-option :label="$t('handover.male')" value="0" />
                <el-option :label="$t('handover.female')" value="1" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('handover.age')">
              <el-input v-model="handoverInfo.age" type="number" :placeholder="$t('handover.agePlaceholder')"
                clearable />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('handover.idCard')">
              <el-input v-model="handoverInfo.idCard" :placeholder="$t('handover.idCardPlaceholder')" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('handover.phone')">
              <el-input v-model="handoverInfo.link" type="number" :placeholder="$t('handover.phonePlaceholder')"
                clearable />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('handover.remark')">
              <el-input v-model="handoverInfo.remark" :placeholder="$t('handover.remarkPlaceholder')" clearable />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="box-card" style="margin-top:20px">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('handover.houseInfo') }}</span>
        <div class="card-header-right">
          <el-button type="primary" size="small" @click="selectRoom">{{ $t('handover.selectHouse') }}</el-button>
          <el-button size="small" @click="createFeeOrder">{{ $t('handover.createFee') }}</el-button>
        </div>
      </div>
      <el-table :data="handoverInfo.rooms" border style="width:100%">
        <el-table-column prop="roomInfo" :label="$t('handover.house')" align="center">
          <template slot-scope="scope">
            {{ scope.row.floorNum }}-{{ scope.row.unitNum }}-{{ scope.row.roomNum }}
          </template>
        </el-table-column>
        <el-table-column prop="builtUpArea" :label="$t('handover.area')" align="center">
          <template slot-scope="scope">
            {{ scope.row.builtUpArea }} {{ $t('handover.squareMeter') }}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('handover.houseStatus')" align="center" />
        <el-table-column :label="$t('handover.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="openDelRoomModel(scope.row)">
              {{ $t('handover.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card v-if="handoverInfo.fees && handoverInfo.fees.length > 0" class="box-card" style="margin-top:20px">
      <div slot="header" class="clearfix">
        <span>{{ $t('handover.feeInfo') }}</span>
      </div>
      <el-table :data="handoverInfo.fees" border style="width:100%">
        <el-table-column prop="feeName" :label="$t('handover.feeItem')" align="center" />
        <el-table-column prop="startTime" :label="$t('handover.startTime')" align="center" />
        <el-table-column prop="endTime" :label="$t('handover.endTime')" align="center">
          <template slot-scope="scope">
            {{ scope.row.endTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('handover.operation')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="openDelFeeModel(scope.row)">
              {{ $t('handover.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <div class="submit-btn">
      <el-button type="primary" @click="saveHandoverInfo">
        <i class="el-icon-check"></i>&nbsp;{{ $t('handover.submit') }}
      </el-button>
    </div>

    <search-owner ref="searchOwner" @chooseOwner="handleChooseOwner" />
    <select-fee-config ref="selectFeeConfig" @notifyFeeConfig="handleNotifyFeeConfig" />
    <room-tree ref="roomTree" @selectRoom="handleChooseRoom" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { saveHandover, queryRooms } from '@/api/room/handoverApi'
import SearchOwner from '@/components/owner/SearchOwner'
import SelectFeeConfig from '@/components/room/selectFeeConfig'
import RoomTree from '@/components/room/roomTree'

export default {
  name: 'HandoverList',
  components: {
    SearchOwner,
    SelectFeeConfig,
    RoomTree
  },
  data() {
    return {
      handoverInfo: {
        name: '',
        age: '',
        link: '',
        sex: '',
        remark: '',
        ownerId: '',
        idCard: '',
        rooms: [],
        fees: [],
        communityId: ''
      }
    }
  },
  created() {
    this.handoverInfo.communityId = getCommunityId()
  },
  methods: {
    selectOwner() {
      this.$refs.searchOwner.open()
    },
    selectRoom() {
      this.$refs.roomTree.open()
    },
    createFeeOrder() {
      this.$refs.selectFeeConfig.open({
        call: 'handover'
      })
    },
    goBack() {
      this.$router.go(-1)
    },
    handleChooseOwner(owner) {
      this.handoverInfo = { ...this.handoverInfo, ...owner }
    },
    handleNotifyFeeConfig(fee) {
      this.handoverInfo.fees.push(fee)
    },
    openDelRoomModel(room) {
      this.handoverInfo.rooms = this.handoverInfo.rooms.filter(item => item.roomId !== room.roomId)
    },
    openDelFeeModel(fee) {
      this.handoverInfo.fees = this.handoverInfo.fees.filter(item => item.configId !== fee.configId)
    },
    async saveHandoverInfo() {
      try {
        const res = await saveHandover(this.handoverInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('handover.submitError'))
      }
    },
    handleChooseRoom(room) {
      this.listRoom(room.roomId)
    },
    async listRoom(roomId) {
      try {
        const params = {
          page: 1,
          row: 1,
          roomId,
          communityId: this.handoverInfo.communityId
        }
        const { data } = await queryRooms(params)
        const room = data.rooms[0]
        if (room.state !== '2002') {
          this.$message.error(this.$t('handover.houseNotUnsold'))
          return
        }
        this.handoverInfo.rooms.push(room)
      } catch (error) {
        this.$message.error(this.$t('handover.fetchRoomError'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.handover-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .card-header-right {
    float: right;
  }

  .submit-btn {
    margin-top: 20px;
    text-align: right;
  }

  .el-row {
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>