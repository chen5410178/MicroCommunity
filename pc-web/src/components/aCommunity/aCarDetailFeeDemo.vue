<template>
  <a-car-detail-fee ref="carDetailFee" />
</template>

<script>
import ACarDetailFee from './aCarDetailFee'

export default {
  components: {
    ACarDetailFee
  },
  methods: {
    open(data) {
      this.$refs.carDetailFee.handleSwitch(data)
    },
    refresh() {
      this.$refs.carDetailFee.handleNotify()
    }
  }
}
</script>