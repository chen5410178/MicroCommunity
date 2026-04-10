<template>
  <a-owner-detail-access-control ref="accessControlComponent" />
</template>

<script>
import AOwnerDetailAccessControl from './aOwnerDetailAccessControl'

export default {
  components: {
    AOwnerDetailAccessControl
  },
  methods: {
    open(data) {
      this.$refs.accessControlComponent.handleSwitch(data)
    }
  }
}
</script>