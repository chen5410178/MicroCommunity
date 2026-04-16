<template>
  <div 
    class="vc-input-search text-left" 
    @mouseleave="doInputSearchRoomClose"
    v-if="rooms && rooms.length>0"
  >
    <div class="close">
      <i class="el-icon-close" @click="doInputSearchRoomClose"></i>
    </div>
    <div 
      class="padding-left-sm padding-top-sm" 
      style="cursor:pointer;"
      v-for="(item,index) in rooms" 
      :key="index"
      @click="inputSearchRoomChooseRoom(item)"
    >
      <span>{{item.floorNum}}-{{item.unitNum}}-{{item.roomNum}}</span>
    </div>
  </div>
</template>

<script>
import { queryRooms } from '@/api/fee/inputSearchRoomApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'InputSearchRoom',
  data() {
    return {
      rooms: [],
      callComponent: '',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.$on('searchRoom', this.handleSearchRoom)
    this.$on('close', this.handleClose)
  },
  methods: {
    handleSearchRoom(param) {
      if (!param.roomName) return
      this.loadRoomInfo(param.roomName)
      this.callComponent = param.callComponent
    },
    handleClose() {
      this.rooms = []
    },
    async loadRoomInfo(roomName) {
      try {
        const res = await queryRooms({
          page: 1,
          row: 10,
          flag: 1,
          roomNum: roomName,
          communityId: this.communityId
        })
        this.rooms = res.rooms
        if (res.rooms.length <= 0) {
          this.$message.warning(this.$t('inputSearchRoom.noRoomFound'))
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    inputSearchRoomChooseRoom(room) {
      this.$emit(this.callComponent, 'notifyRoom', room)
      this.rooms = []
    },
    doInputSearchRoomClose() {
      this.rooms = []
    }
  }
}
</script>

<style scoped>
.vc-input-search {
  position: absolute;
  z-index: 999;
  background: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  padding: 10px;
}
.close {
  text-align: right;
  cursor: pointer;
}
.padding-left-sm {
  padding-left: 10px;
}
.padding-top-sm {
  padding-top: 5px;
}
</style>