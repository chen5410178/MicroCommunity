<template>
  <owner-detail-account-receipt ref="ownerDetailAccountReceipt" />
</template>

<script>
import OwnerDetailAccountReceipt from '@/components/owner/ownerDetailAccountReceipt'

export default {
  components: {
    OwnerDetailAccountReceipt
  },
  methods: {
    open(ownerId) {
      this.$refs.ownerDetailAccountReceipt.open(ownerId)
    }
  }
}
</script>