<template>
  <div>
    <simplify-room-fee ref="simplifyRoomFee"></simplify-room-fee>
  </div>
</template>

<script>
import SimplifyRoomFee from './simplifyRoomFee'

export default {
  name: 'SimplifyRoomFeeDemo',
  components: {
    SimplifyRoomFee
  },
  mounted() {
    this.$refs.simplifyRoomFee.$emit('switch', {
      roomId: '123',
      roomName: '测试房间',
      name: '测试业主'
    })
  }
}
</script>