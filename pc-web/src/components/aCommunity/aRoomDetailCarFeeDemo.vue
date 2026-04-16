<template>
  <a-room-detail-car-fee ref="carFeeComponent" />
</template>

<script>
import ARoomDetailCarFee from './aRoomDetailCarFee'

export default {
  components: {
    ARoomDetailCarFee
  },
  methods: {
    open(data) {
      this.$refs.carFeeComponent.handleSwitch(data)
    },
    refresh() {
      this.$refs.carFeeComponent.handleNotify()
    }
  }
}
</script>