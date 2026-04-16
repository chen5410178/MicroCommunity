<template>
  <owner-detail-room ref="ownerDetailRoom" />
</template>

<script>
import OwnerDetailRoom from '@/components/owner/ownerDetailRoom'

export default {
  components: {
    OwnerDetailRoom
  },
  methods: {
    open(ownerId) {
      this.$refs.ownerDetailRoom.ownerDetailRoomInfo.ownerId = ownerId
      this.$refs.ownerDetailRoom._loadOwnerDetailRoomData(1, 10)
    }
  }
}
</script>