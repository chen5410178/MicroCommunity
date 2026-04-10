<template>
  <owner-detail-access-control ref="ownerDetailAccessControl" />
</template>

<script>
import OwnerDetailAccessControl from '@/components/owner/ownerDetailAccessControl'

export default {
  components: {
    OwnerDetailAccessControl
  },
  methods: {
    open(ownerId) {
      this.$refs.ownerDetailAccessControl.open(ownerId)
    }
  }
}
</script>