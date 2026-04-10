<template>
  <div class="show-owner-room-container">
    <el-row v-for="(roomInfo, index) in showOwnerRoomInfo.rooms" :key="index" class="room-row">
      <el-col :span="24">
        <el-card class="room-card">
          <div slot="header" class=" flex justify-between">
            <span>{{ $t('showOwnerRoom.info') }}</span>
            <div class="card-tools">
              <el-button v-if="showOwnerRoomInfo.deleteOwnerRoomFlag == false" type="primary" size="small"
                icon="el-icon-plus" @click="showPropertyFee(roomInfo)">
                {{ $t('showOwnerRoom.propertyFee') }}
              </el-button>
              <el-button v-if="showOwnerRoomInfo.deleteOwnerRoomFlag == true" type="danger" size="small"
                icon="el-icon-close" @click="ownerExitRoomModel(roomInfo)">
                {{ $t('showOwnerRoom.exitRoom') }}
              </el-button>
            </div>
          </div>
          <div class="card-content">
            <el-row :gutter="20" class="text-left">
              <el-col v-if="roomInfo.roomType === '2020602'" :span="8">
                <div class="form-item">
                  <label>{{ $t('showOwnerRoom.shopNum') }}：</label>
                  <span>{{ roomInfo.floorNum }}-{{ roomInfo.roomNum }}</span>
                </div>
              </el-col>
              <el-col v-else :span="8">
                <div class="form-item">
                  <label>{{ $t('showOwnerRoom.roomNum') }}：</label>
                  <span>{{ roomInfo.floorNum }}-{{ roomInfo.unitNum }}-{{ roomInfo.roomNum }}</span>
                </div>
              </el-col>

              <el-col :span="8">
                <div class="form-item">
                  <label>{{ $t('showOwnerRoom.floor') }}：</label>
                  <span>{{ roomInfo.layer }}{{ $t('showOwnerRoom.layer') }}</span>
                </div>
              </el-col>

              <el-col v-if="roomInfo.roomType === '2020602'" :span="8">
                <div class="form-item">
                  <label>{{ $t('showOwnerRoom.shopId') }}：</label>
                  <span>{{ roomInfo.roomId }}</span>
                </div>
              </el-col>
              <el-col v-else :span="8">
                <div class="form-item">
                  <label>{{ $t('showOwnerRoom.roomId') }}：</label>
                  <span>{{ roomInfo.roomId }}</span>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20" class="text-left">
              <el-col :span="8">
                <div class="form-item">
                  <label>{{ $t('showOwnerRoom.builtUpArea') }}：</label>
                  <span>{{ roomInfo.builtUpArea }}{{ $t('showOwnerRoom.squareMeters') }}</span>
                </div>
              </el-col>

              <el-col v-if="roomInfo.roomType === '1010301'" :span="8">
                <div class="form-item">
                  <label>{{ $t('showOwnerRoom.apartmentLayout') }}：</label>
                  <span>{{ roomInfo.apartment }}</span>
                </div>
              </el-col>

              <el-col v-if="roomInfo.roomType === '1010301'" :span="8">
                <div class="form-item">
                  <label>{{ $t('showOwnerRoom.roomCount') }}：</label>
                  <span>{{ roomInfo.section }}</span>
                </div>
              </el-col>
            </el-row>

            <div v-for="(item, attrIndex) in roomInfo.roomAttrDto" :key="attrIndex">
              <el-row v-if="attrIndex % 3 === 0" :gutter="20">
                <el-col :span="8">
                  <div class="form-item">
                    <label>{{ item.specName }}：</label>
                    <span>{{ item.valueName }}</span>
                  </div>
                </el-col>

                <el-col v-if="attrIndex < roomInfo.roomAttrDto.length - 1" :span="8">
                  <div class="form-item">
                    <label>{{ roomInfo.roomAttrDto[attrIndex + 1].specName }}：</label>
                    <span>{{ roomInfo.roomAttrDto[attrIndex + 1].valueName }}</span>
                  </div>
                </el-col>

                <el-col v-if="attrIndex < roomInfo.roomAttrDto.length - 2" :span="8">
                  <div class="form-item">
                    <label>{{ roomInfo.roomAttrDto[attrIndex + 2].specName }}：</label>
                    <span>{{ roomInfo.roomAttrDto[attrIndex + 2].valueName }}</span>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <owner-exit-room ref="exitRoomDialog" @success="handleExitSuccess" />
  </div>
</template>

<script>
import { queryRoomsByOwner } from '@/api/owner/showOwnerRoomApi'
import OwnerExitRoom from '@/components/owner/ownerExitRoom'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ShowOwnerRoomList',
  components: {
    OwnerExitRoom
  },
  props: {
    deleteOwnerRoomFlag: {
      type: String,
      default: 'false'
    },
    ownerId: {
      type: String,
      required: true
    },
    ownerName: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      showOwnerRoomInfo: {
        ownerId: '',
        ownerName: '',
        rooms: [],
        deleteOwnerRoomFlag: this.deleteOwnerRoomFlag
      }
    }
  },
  watch: {
    ownerId(newVal) {
      if (newVal) {
        this.showOwnerRoomInfo.ownerId = newVal
        this.loadRooms()
      }
    }
  },
  mounted() {
    this.showOwnerRoomInfo.ownerId = this.ownerId
    this.showOwnerRoomInfo.ownerName = this.ownerName
    this.loadRooms()
  },
  methods: {
    async loadRooms() {
      try {
        const params = {
          communityId: getCommunityId(),
          ownerId: this.showOwnerRoomInfo.ownerId
        }

        const res = await queryRoomsByOwner(params)
        this.showOwnerRoomInfo.rooms = res.rooms || []

      } catch (error) {
        this.$message.error('请求失败')
      }
    },

    ownerExitRoomModel(roomInfo) {
      this.$refs.exitRoomDialog.open({
        ownerId: this.showOwnerRoomInfo.ownerId,
        roomId: roomInfo.roomId
      })
    },

    showPropertyFee(room) {
      const queryParams = {
        ...room,
        ownerName: this.showOwnerRoomInfo.ownerName
      }
      this.$router.push({
        path: '/property/listRoomFee',
        query: queryParams
      })
    },

    handleExitSuccess() {
      this.loadRooms()
      this.$emit('room-exited')
    },

    showState(state) {
      const stateMap = {
        '2001': this.$t('showOwnerRoom.sold'),
        '2002': this.$t('showOwnerRoom.unsold'),
        '2003': this.$t('showOwnerRoom.depositPaid'),
        '2004': this.$t('showOwnerRoom.rented'),
        default: this.$t('showOwnerRoom.unknown')
      }
      return stateMap[state] || stateMap.default
    }
  }
}
</script>

<style scoped>


.room-card {
  margin-bottom: 20px;
}

.card-tools {
  position: absolute;
  right: 20px;
  top: 15px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}

.card-content {
  padding: 20px;
}

.form-item {
  margin-bottom: 15px;
  line-height: 36px;
  color: #606266;
}

.form-item label {
  margin-right: 5px;
}

.form-item span {
  display: inline-block;
}
</style>