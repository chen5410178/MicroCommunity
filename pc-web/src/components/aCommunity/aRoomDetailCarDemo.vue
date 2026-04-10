<template>
  <a-room-detail-car ref="carComponent" />
</template>

<script>
import ARoomDetailCar from './aRoomDetailCar'

export default {
  components: {
    ARoomDetailCar
  },
  methods: {
    open(data) {
      this.$refs.carComponent.handleSwitch(data)
    },
    refresh() {
      this.$refs.carComponent.handleNotify()
    }
  }
}
</script>