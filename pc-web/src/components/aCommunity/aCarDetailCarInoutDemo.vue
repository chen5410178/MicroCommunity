<template>
  <a-car-detail-car-inout ref="carDetailCarInout" />
</template>

<script>
import ACarDetailCarInout from './aCarDetailCarInout'

export default {
  components: {
    ACarDetailCarInout
  },
  methods: {
    open(data) {
      this.$refs.carDetailCarInout.handleSwitch(data)
    },
    refresh() {
      this.$refs.carDetailCarInout.handleNotify()
    }
  }
}
</script>