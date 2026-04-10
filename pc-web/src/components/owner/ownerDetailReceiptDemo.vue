<template>
  <owner-detail-receipt ref="ownerDetailReceipt" />
</template>

<script>
import OwnerDetailReceipt from '@/components/owner/ownerDetailReceipt'

export default {
  components: {
    OwnerDetailReceipt
  },
  methods: {
    open(ownerId) {
      this.$refs.ownerDetailReceipt.open(ownerId)
    }
  }
}
</script>