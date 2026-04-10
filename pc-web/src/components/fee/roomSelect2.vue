<template>
  <el-select
    v-model="selectedRoom"
    :placeholder="$t('meterWater.selectRoom')"
    filterable
    clearable
    style="width: 100%"
    :disabled="!unitId"
    @change="handleChange"
  >
    <el-option
      v-for="item in rooms"
      :key="item.roomId"
      :label="getRoomLabel(item)"
      :value="item.roomId"
    />
  </el-select>
</template>

<script>
import { queryRooms } from '@/api/fee/meterWaterManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'RoomSelect2',
  props: {
    value: {
      type: [String, Number],
      default: ''
    },
    unitId: {
      type: [String, Number],
      default: ''
    }
  },
  data() {
    return {
      rooms: [],
      selectedRoom: this.value,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  watch: {
    unitId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.loadRooms(newVal)
        } else {
          this.rooms = []
          this.selectedRoom = ''
        }
      }
    },
    value(newVal) {
      this.selectedRoom = newVal
    }
  },
  methods: {
    async loadRooms(unitId) {
      try {
        const { data } = await queryRooms({
          unitId,
          communityId: this.communityId,
          page: 1,
          row: 200
        })
        this.rooms = data.rooms || []
      } catch (error) {
        console.error('Failed to load rooms:', error)
      }
    },
    getRoomLabel(room) {
      return room.ownerName 
        ? `${room.roomNum}室(${room.ownerName})` 
        : `${room.roomNum}室`
    },
    handleChange(value) {
      const room = this.rooms.find(item => item.roomId === value)
      if (room) {
        this.$emit('change', {
          roomId: value,
          name: `${room.floorNum}-${room.unitNum}-${room.roomNum}`,
          link: this.getRoomLabel(room)
        })
      } else {
        this.$emit('change', null)
      }
    }
  }
}
</script>