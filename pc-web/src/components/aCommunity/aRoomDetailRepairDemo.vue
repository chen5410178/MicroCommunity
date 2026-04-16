<template>
  <a-room-detail-repair ref="repairComponent" />
</template>

<script>
import ARoomDetailRepair from './aRoomDetailRepair'

export default {
  components: {
    ARoomDetailRepair
  },
  methods: {
    open(data) {
      this.$refs.repairComponent.handleSwitch(data)
    }
  }
}
</script>