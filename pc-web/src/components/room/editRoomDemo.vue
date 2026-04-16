<template>
  <div>
    <el-button @click="openEditRoom">编辑房屋</el-button>
    <edit-room ref="editRoom" @room-updated="handleRoomUpdated" />
  </div>
</template>

<script>
import EditRoom from '@/components/room/editRoom'

export default {
  components: {
    EditRoom
  },
  methods: {
    openEditRoom() {
      // 模拟房屋数据
      const roomData = {
        roomId: '123',
        floorId: '456',
        roomNum: '101',
        unitId: '789',
        layer: '1',
        apartment: '1020',
        builtUpArea: '100',
        feeCoefficient: '1.0',
        state: '2001',
        remark: '备注信息',
        roomArea: '80',
        roomRent: '3000',
        endTime: '2023-12-31'
      }
      this.$refs.editRoom.open(roomData)
    },
    handleRoomUpdated() {
      console.log('房屋更新成功')
    }
  }
}
</script>