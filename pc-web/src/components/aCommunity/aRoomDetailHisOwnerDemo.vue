<template>
  <a-room-detail-his-owner ref="hisOwnerComponent" />
</template>

<script>
import ARoomDetailHisOwner from './aRoomDetailHisOwner'

export default {
  components: {
    ARoomDetailHisOwner
  },
  methods: {
    open(data) {
      this.$refs.hisOwnerComponent.handleSwitch(data)
    }
  }
}
</script>