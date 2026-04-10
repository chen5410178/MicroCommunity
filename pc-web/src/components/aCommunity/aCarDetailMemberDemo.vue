<template>
  <a-car-detail-member ref="carDetailMember" />
</template>

<script>
import ACarDetailMember from './aCarDetailMember'

export default {
  components: {
    ACarDetailMember
  },
  methods: {
    open(data) {
      this.$refs.carDetailMember.handleSwitch(data)
    }
  }
}
</script>