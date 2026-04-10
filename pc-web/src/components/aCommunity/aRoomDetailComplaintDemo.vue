<template>
  <a-room-detail-complaint ref="complaintComponent" />
</template>

<script>
import ARoomDetailComplaint from './aRoomDetailComplaint'

export default {
  components: {
    ARoomDetailComplaint
  },
  methods: {
    open(ownerId) {
      this.$refs.complaintComponent.handleSwitch({ ownerId })
    }
  }
}
</script>