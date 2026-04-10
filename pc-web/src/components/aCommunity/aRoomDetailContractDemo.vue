<template>
  <a-room-detail-contract ref="contractComponent" />
</template>

<script>
import ARoomDetailContract from './aRoomDetailContract'

export default {
  components: {
    ARoomDetailContract
  },
  methods: {
    open(ownerId) {
      this.$refs.contractComponent.handleSwitch({ ownerId })
    }
  }
}
</script>