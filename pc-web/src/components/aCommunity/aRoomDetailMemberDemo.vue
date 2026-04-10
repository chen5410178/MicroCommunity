<template>
  <a-room-detail-member ref="memberComponent" @viewImage="handleViewImage" />
</template>

<script>
import ARoomDetailMember from './aRoomDetailMember'

export default {
  components: {
    ARoomDetailMember
  },
  methods: {
    open(ownerId) {
      this.$refs.memberComponent.handleSwitch({ ownerId })
    },
    handleViewImage(url) {
      // Handle image view
    }
  }
}
</script>