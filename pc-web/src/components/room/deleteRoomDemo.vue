<template>
  <div>
    <el-button @click="openDeleteRoom">删除房屋</el-button>
    <delete-room ref="deleteRoom" @room-deleted="handleRoomDeleted" />
  </div>
</template>

<script>
import DeleteRoom from '@/components/room/deleteRoom'

export default {
  components: {
    DeleteRoom
  },
  methods: {
    openDeleteRoom() {
      // 模拟房屋数据
      const roomData = {
        roomId: '123',
        floorId: '456'
      }
      this.$refs.deleteRoom.open(roomData)
    },
    handleRoomDeleted() {
      console.log('房屋删除成功')
    }
  }
}
</script>