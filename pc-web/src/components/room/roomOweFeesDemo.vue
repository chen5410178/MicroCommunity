<template>
  <div>
    <el-button type="primary" @click="openModal">
      {{ $t('roomOweFeesDemo.openBtn') }}
    </el-button>
    <room-owe-fees ref="roomOweFeesRef" />
  </div>
</template>

<script>
import RoomOweFees from './roomOweFees'

export default {
  components: {
    RoomOweFees
  },
  methods: {
    openModal() {
      // 模拟房屋ID
      const roomId = '67890'
      this.$refs.roomOweFeesRef.open(roomId)
    }
  }
}
</script>