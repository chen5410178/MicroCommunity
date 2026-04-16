<template>
  <a-owner-detail-visit 
    ref="visitComponent" 
    @viewImage="handleViewImage"
  />
</template>

<script>
import AOwnerDetailVisit from './aOwnerDetailVisit'

export default {
  components: {
    AOwnerDetailVisit
  },
  methods: {
    open(data) {
      this.$refs.visitComponent.handleSwitch(data)
    },
    handleViewImage(url) {
      // Handle image view logic here
    }
  }
}
</script>