<template>
  <a-owner-detail-room-fee ref="roomFeeComponent" />
</template>

<script>
import AOwnerDetailRoomFee from './aOwnerDetailRoomFee'

export default {
  components: {
    AOwnerDetailRoomFee
  },
  methods: {
    open(data) {
      this.$refs.roomFeeComponent.handleSwitch(data)
    },
    refresh() {
      this.$refs.roomFeeComponent.handleNotify()
    }
  }
}
</script>