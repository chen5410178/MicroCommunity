<template>
  <div 
    class="vc-input-search text-left"
    v-if="rooms && rooms.length>0"
  >
    <div class="close">
      <i class="el-icon-close" @click="doInputSearchRoomByOwnerClose"></i>
    </div>
    <div 
      class="padding-left-sm padding-top-sm" 
      style="cursor:pointer;"
      v-for="(item,index) in rooms" 
      :key="index"
      @click="inputSearchRoomChooseRoomByOwner(item)"
    >
      <span>{{item.floorNum}}-{{item.unitNum}}-{{item.roomNum}}({{item.ownerName}})</span>
    </div>
  </div>
</template>

<script>
import { queryRoomsByOwner } from '@/api/fee/inputSearchRoomByOwnerApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'InputSearchRoomByOwner',
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
      if (!param.ownerName) return
      this.loadRoomByOwnerInfo(param.ownerName)
      this.callComponent = param.callComponent
    },
    handleClose() {
      this.rooms = []
    },
    async loadRoomByOwnerInfo(ownerName) {
      try {
        const res = await queryRoomsByOwner({
          page: 1,
          row: 10,
          ownerNameLike: ownerName,
          communityId: this.communityId
        })
        this.rooms = res.rooms
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    inputSearchRoomChooseRoomByOwner(room) {
      this.$emit(this.callComponent, 'notifyRoomByOwner', room)
      this.rooms = []
    },
    doInputSearchRoomByOwnerClose() {
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