<template>
  <div class="room-structure-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <floor-unit-tree ref="floorUnitTree" @switchFloorUnit="switchFloorUnit" />
      </el-col>
      <el-col :span="20">
        <el-card v-if="roomStructureInfo.layerRoomCount < 5" class="room-list-card">
          <el-row :gutter="10">
            <el-col v-for="(room, index) in roomStructureInfo.rooms" :key="index" :xs="12" :sm="8" :md="6" :lg="4"
              :xl="3" class="room-item">
              <div class="room-card" :style="{ 'background-color': getBgColor(room) }"
                @dblclick="toSimplifyAcceptance(room)">
                <div class="room-number">{{ room.floorNum }}-{{ room.unitNum }}-{{ room.roomNum }}</div>
                <div class="room-state">{{ room.stateName }}</div>
                <div class="room-owner">{{ room.ownerName || $t('roomStructure.noOwner') }}</div>
                <div class="room-debt">
                  <span>{{ $t('roomStructure.owe') }}</span>：{{ room.oweAmount }}
                  <span>{{ $t('roomStructure.yuan') }}</span>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <el-card v-else class="room-list-card">
          <div v-for="(val, key, index) in roomStructureInfo.parkRooms" :key="index">
            <div class="floor-title">{{ key }}F</div>
            <el-row :gutter="10" class="floor-row">
              <el-col v-for="(room, index) in val" :key="index" :xs="12" :sm="8" :md="6" :lg="4" :xl="3"
                class="room-item">
                <div class="room-card" :style="{ 'background-color': getBgColor(room) }"
                  @dblclick="toSimplifyAcceptance(room)">
                  <div class="room-number">{{ room.floorNum }}-{{ room.unitNum }}-{{ room.roomNum }}</div>
                  <div class="room-state">{{ room.stateName }}</div>
                  <div class="room-owner">{{ room.ownerName || $t('roomStructure.noOwner') }}</div>
                  <div class="room-debt">
                    <span>{{ $t('roomStructure.owe') }}</span>：{{ room.oweAmount }}
                    <span>{{ $t('roomStructure.yuan') }}</span>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import floorUnitTree from '@/components/room/floorUnitTree'
import { listRoomStructure } from '@/api/room/roomStructureApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'RoomStructureList',
  components: {
    floorUnitTree
  },
  data() {
    return {
      roomStructureInfo: {
        rooms: [],
        parkRooms: {},
        layerRoomCount: 4
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    setTimeout(() => {
      this.$refs.floorUnitTree.selectFirstUnit()
    }, 1500)
  },
  methods: {
    handleSwitchUnit(params) {
      this.loadRooms(params.unitId)
    },
    switchFloorUnit(data) {
      if (!data.unitId) {
        return
      }
      this.loadRooms(data.unitId)
    },
    async loadRooms(unitId) {
      this.roomStructureInfo.rooms = []
      this.roomStructureInfo.parkRooms = {}

      try {
        const params = {
          page: 1,
          row: 100,
          unitId: unitId,
          communityId: this.communityId
        }

        const { data } = await listRoomStructure(params)
        this.roomStructureInfo.rooms = data
        this.supportPark()
      } catch (error) {
        console.error('Failed to load rooms:', error)
      }
    },
    getBgColor(room) {
      if (room.oweAmount > 0) {
        return "#DC3545"
      }
      if (!room.ownerName) {
        return "#1AB394"
      }
      if (room.state === '2001') {
        return '#1296db'
      }
      if (room.state === '2003') {
        return '#4C8CDE'
      }
      if (room.state === '2005') {
        return '#085DC9'
      }
      if (room.state === '2004') {
        return '#9DBFEA'
      }
      if (room.state === '2006') {
        return '#365A87'
      }
      if (room.state === '2007') {
        return '#1053A8'
      }
      if (room.state === '2008') {
        return '#4E79AF'
      }
      if (room.state === '2009') {
        return '#5B81B1'
      }
      return "#1296db"
    },
    toSimplifyAcceptance(room) {
      this.$router.push('/pages/property/simplifyAcceptance?tab=业务受理&searchType=1&searchValue=' + `${room.floorNum}-${room.unitNum}-${room.roomNum}`)
    },
    supportPark() {
      const parkRooms = this.roomStructureInfo.parkRooms
      if (!this.roomStructureInfo.rooms || this.roomStructureInfo.rooms.length < 1) {
        return
      }

      this.roomStructureInfo.rooms.forEach(item => {
        if (!parkRooms[item.layer]) {
          parkRooms[item.layer] = []
        }
        parkRooms[item.layer].push(item)
      })

      this.roomStructureInfo.parkRooms = parkRooms
      if (Object.keys(parkRooms).length > 0) {
        this.roomStructureInfo.layerRoomCount = parkRooms[Object.keys(parkRooms)[0]].length
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.room-structure-container {
  padding: 20px;
  height: 100%;

  .room-list-card {
    margin-bottom: 20px;
    min-height: calc(100vh - 120px);

    .floor-title {
      font-size: 16px;
      font-weight: bold;
      padding: 10px 0;
    }

    .floor-row {
      margin-bottom: 20px;
    }

    .room-item {
      margin-bottom: 10px;
    }

    .room-card {
      color: #fff;
      border-radius: 5px;
      padding: 10px;
      cursor: pointer;
      transition: all 0.3s;
      height: 100%;

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }

      .room-number {
        font-weight: bold;
        margin-bottom: 5px;
      }

      .room-state,
      .room-owner,
      .room-debt {
        margin-bottom: 3px;
        font-size: 12px;
      }
    }
  }
}
</style>