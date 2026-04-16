<template>
  <owner-detail-his-fee ref="ownerDetailHisFee" />
</template>

<script>
import OwnerDetailHisFee from '@/components/owner/ownerDetailHisFee'

export default {
  components: {
    OwnerDetailHisFee
  },
  methods: {
    open(ownerId) {
      this.$refs.ownerDetailHisFee.open(ownerId)
    }
  }
}
</script>